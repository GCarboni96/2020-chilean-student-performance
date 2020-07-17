package com.education

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}


//PROMEDIO GENERAL DE DOCENTES DE CADA ESTABLECIMIENTO POR AÑO, CON PROMEDIO GENERAL
case class EvaluationAndPerformanceYearlyAverageMetroComuna() {
  def run(spark:SparkSession, out_path:String) {

    // SACAMOS AÑO, ID_ESTB, NOMBRE_ESTB, AVG_GRADES PARA DOCENTES
    val cols = List("año_eval", "cod_reg_rbd","cod_com_rbd", "nom_com_rbd","pf_pje")
    val picked = EvaluationDataset().pick_columns(spark, cols)
    val filtered = picked.filter((picked("cod_reg_rbd") =!=" " || picked("pf_pje") =!= 1 || picked("pf_pje") =!=" ") && (picked("cod_reg_rbd") ===13))
    val rdd = filtered.rdd

    val selected = rdd.map(t =>
      ((t.get(0), t.get(2), t.get(3)),
        (t.get(4).toString.replace(',','.').toDouble,
      if (t.get(4)!=0) 1 else 0)))

    val grouped = selected.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2 ))
    val averaged_docentes = grouped.map(t=> Row(t._1._1, t._1._2 , t._1._3 ,t._2._1/t._2._2))


    val schema_docentes = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="cod_comuna", dataType = IntegerType, nullable = false),
        StructField(name="nomb_comuna", dataType = StringType, nullable = false),
        StructField(name="avg_grades_docentes", dataType = DoubleType, nullable = true)
      )
    )
    val out_docentes = spark.sqlContext.createDataFrame(averaged_docentes, schema_docentes)

    // SACAMOS AÑO, ID_ESTB, NOMBRE_ESTB, AVG_GRADES, ASISTENCIA PARA ALUMNOS

    val cols2 = List("agno", "cod_reg_rbd", "cod_com_rbd", "nom_com_rbd","prom_gral", "asistencia")
    val picked2 = PerformanceDatasetForCross().pick_columns(spark, cols2)
    val filtered2 = picked2.filter((picked2("prom_gral") =!= 0 || picked2("asistencia") =!= 0)&& (picked2("cod_reg_rbd") ===13))
    val rdd2 = filtered2.rdd

    val selected2 = rdd2.map(t =>
      ((t.get(0), t.get(2), t.get(3)),
        (t.get(4).toString.replace(',','.').toDouble,
          if (t.get(4)!=0) 1 else 0,
          t.get(5).toString.replace(',','.').toDouble,
          if (t.get(5)!=0) 1 else 0)))

    val grouped2 = selected2.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2, a._3 + b._3 ,a._4 + b._4))
    val averaged_alumnos = grouped2.map(t=> Row(t._1._1, t._1._2, t._1._3,t._2._1/t._2._2, t._2._3/t._2._4))

    val schema_alumnos = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="cod_comuna", dataType = IntegerType, nullable = false),
        StructField(name="nomb_comuna", dataType = StringType, nullable = false),
        StructField(name="avg_grades_alumnos", dataType = DoubleType, nullable = true),
        StructField(name="asistencia", dataType = DoubleType, nullable = true)
      )
    )
    val out_alumnos = spark.sqlContext.createDataFrame(averaged_alumnos, schema_alumnos)
    //HACEMOS JOIN
    val joined= out_docentes.join(out_alumnos,Seq("year", "cod_comuna"), "inner")
      .select(col("year"), col("cod_comuna"), out_docentes.col("nomb_comuna"),out_docentes.col("avg_grades_docentes") ,out_alumnos.col("avg_grades_alumnos"))

    val sorted = joined.sort(asc("year"), asc("cod_comuna"))
    sorted.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}