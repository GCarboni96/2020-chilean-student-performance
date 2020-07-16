package com.education

import org.apache.spark.sql.types.{DoubleType, IntegerType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

//PROMEDIO GENERAL DE CADA ESTABLECIMIENTO POR AÑO, CON PROMEDIO GENERAL Y % DE ASISTENCIA
case class PerformanceYearlyAverage() {
  def run(spark:SparkSession, out_path:String) {
    // selección de columnas año, id_estab, genero_alumno, promedio_final y % asistencia
    val cols = List("agno", "rbd", "gen_alu", "prom_gral", "asistencia")
    // retrieve de data y seleccion de columnas 'cols'
    val picked = PerformanceDataset().pick_columns(spark, cols)
    // exclusion de registros sin datos de asistencia o sin promedio gral
    val filtered = picked.filter(picked("prom_gral") =!= 0 || picked("asistencia") =!= 0)
    val rdd = filtered.rdd

    // mapeo a ( (agno, rbd), (prom_gral, varcontador, asistencia, varcontador2) )
    val selected = rdd.map(t =>
      ((t.get(0), t.get(1)),
        (t.get(3).toString.replace(',','.').toDouble,
      if (t.get(3)!=0) 1 else 0,
          t.get(4).toString.replace(',','.').toDouble,
          if (t.get(4)!=0) 1 else 0)))
    // reduccion por key ( (agno, rbd), (prom_gral, varcontador, asistencia, varcontador2) )
    val grouped = selected.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2, a._3 + b._3 ,a._4 + b._4))
    // mapeo a( agno, rbd, sum(prom_gral) / sum(varcontador), sum(asistencia)/sum(varcontador2))
    val averaged = grouped.map(t=> Row(t._1._1, t._1._2, t._2._1/t._2._2, t._2._3/t._2._4))
    // schemas de salida
    val schema = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="rbd", dataType = IntegerType, nullable = false),
        StructField(name="avg_grades", dataType = DoubleType, nullable = true),
        StructField(name="avg_assistance", dataType = DoubleType, nullable = true)
      )
    )
    val out = spark.sqlContext.createDataFrame(averaged, schema)
    // guardado
    out.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}