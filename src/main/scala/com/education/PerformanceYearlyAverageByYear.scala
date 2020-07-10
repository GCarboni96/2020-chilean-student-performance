package com.education

import org.apache.spark.sql.types.{DoubleType, IntegerType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

//PROMEDIO GENERAL DE CADA AÑO, CON PROMEDIO GENERAL Y % DE ASISTENCIA
case class PerformanceYearlyAverageByYear() {
  def run(spark:SparkSession, out_path:String) {
    val cols = List("agno", "rbd", "gen_alu", "prom_gral", "asistencia")
    val picked = PerformanceDataset().pick_columns(spark, cols)
    val filtered = picked.filter(picked("prom_gral") =!= 0 || picked("asistencia") =!= 0)
    val rdd = filtered.rdd

    val selected = rdd.map(t =>
      (t.get(0),
        (t.get(3).toString.replace(',','.').toDouble,
      if (t.get(3)!=0) 1 else 0,
          t.get(4).toString.replace(',','.').toDouble,
          if (t.get(4)!=0) 1 else 0)))

    val grouped = selected.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2, a._3 + b._3 ,a._4 + b._4))
    val averaged = grouped.map(t=> Row(t._1, t._2._1/t._2._2, t._2._3/t._2._4))
    val schema = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="avg_grades", dataType = DoubleType, nullable = true),
        StructField(name="avg_assistance", dataType = DoubleType, nullable = true)
      )
    )
    val out = spark.sqlContext.createDataFrame(averaged, schema)
    out.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}