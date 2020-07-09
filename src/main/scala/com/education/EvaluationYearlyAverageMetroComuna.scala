package com.education

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions._


//PROMEDIO GENERAL DE DOCENTES DE CADA ESTABLECIMIENTO POR AÑO, CON PROMEDIO GENERAL
case class EvaluationYearlyAverageMetroComuna() {
  def run(spark:SparkSession, out_path:String) {
    val cols = List("año_eval", "cod_reg_rbd","cod_com_rbd", "nom_com_rbd","pf_pje")
    val picked = EvaluationDataset().pick_columns(spark, cols)
    val filtered = picked.filter((picked("cod_reg_rbd") =!=" " || picked("pf_pje") =!= 1 || picked("pf_pje") =!=" ") && (picked("cod_reg_rbd") ===13))
    val rdd = filtered.rdd

    val selected = rdd.map(t =>
      ((t.get(0), t.get(2), t.get(3)),
        (t.get(4).toString.replace(',','.').toDouble,
          if (t.get(4)!=0) 1 else 0)))

    val grouped = selected.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2 ))
    val averaged_docentes = grouped.map(t=> (t._1._1, t._1._2 , t._1._3 ,t._2._1/t._2._2))

    val classed = averaged_docentes.map(t=> Row(t._1,t._2, t._3,t._4,
      if (t._4 < 2.0){"I"}
      else if (t._4 >= 2.0 && t._4 < 2.5) {"B"}
      else if (t._4 >= 2.5 && t._4 < 3.0) {"C"}
      else {"D"}
    ))


    val schema_docentes = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="cod_comuna", dataType = IntegerType, nullable = false),
        StructField(name="nomb_comuna", dataType = StringType, nullable = false),
        StructField(name="avg_grades_docentes", dataType = DoubleType, nullable = true),
        StructField(name="avg_grades_rank", dataType = StringType, nullable = true)
      )
    )
    val out = spark.sqlContext.createDataFrame(classed, schema_docentes)
    out.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}