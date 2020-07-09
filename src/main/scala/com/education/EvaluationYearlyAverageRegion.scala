package com.education

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions._


//PROMEDIO GENERAL DE DOCENTES DE CADA ESTABLECIMIENTO POR AÑO, CON PROMEDIO GENERAL
case class EvaluationYearlyAverageRegion() {
  def run(spark:SparkSession, out_path:String) {
    val cols = List("año_eval", "cod_reg_rbd", "pf_pje")
    val picked = EvaluationDataset().pick_columns(spark, cols)
    val filtered = picked.filter(picked("cod_reg_rbd") =!=" " || picked("pf_pje") =!= 1 || picked("pf_pje") =!=" ")
    val filtered2 = filtered.na.replace(filtered.columns,Map(" " -> "9999"))
    val filtered3 = filtered2.filter(filtered2("cod_reg_rbd") =!= "9999")
    val filtered4 = filtered3.filter(filtered3("pf_pje") =!= "9999")
    val filtered5 = filtered4.select(filtered4("año_eval").cast(IntegerType).as("año_eval"),
      filtered4("cod_reg_rbd").cast(IntegerType).as("cod_reg_rbd"),
      filtered4("pf_pje").cast(StringType).as("pf_pje")
    )

    val rdd = filtered5.rdd

    val selected = rdd.map(t =>
      ((t.get(0), t.get(1)),
        (t.get(2).toString.replace(',','.').toDouble,
      if (t.get(2)!=0) 1 else 0)))

    val grouped = selected.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2 ))
    val averaged = grouped.map(t=> (t._1._1, t._1._2 ,t._2._1/t._2._2))

    val classed = averaged.map(t=> Row(t._1,t._2, t._3,
    if (t._3 < 2.0){"I"}
    else if (t._3 >= 2.0 && t._3 < 2.5) {"B"}
    else if (t._3 >= 2.5 && t._3 < 3.0) {"C"}
    else {"D"}
    ))
    val schema = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="codigo_region", dataType = IntegerType, nullable = false),
        StructField(name="avg_grades", dataType = DoubleType, nullable = true),
        StructField(name="avg_grades_rank", dataType = StringType, nullable = true)
      )
    )
    val out = spark.sqlContext.createDataFrame(classed, schema)
    out.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}