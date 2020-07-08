package com.education

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}



//PROMEDIO GENERAL DE DOCENTES DE CADA ESTABLECIMIENTO POR AÑO, CON PROMEDIO GENERAL
case class EvaluationYearlyAverage() {
  def run(spark:SparkSession, out_path:String) {
    val cols = List("año_eval", "rbd", "pf_pje", "nom_rbd")
    val picked = EvaluationDataset().pick_columns(spark, cols)
    val filtered = picked.filter(picked("rbd") =!= "" || picked("pf_pje") =!= 1 || picked("pf_pje") =!=" ")
    filtered.show()
    val filtered2 = filtered.na.replace(filtered.columns,Map(" " -> "9999"))
    filtered2.show()
    val filtered3 = filtered2.filter(filtered2("rbd") =!= "9999")
    val filtered4 = filtered3.filter(filtered3("pf_pje") =!= "9999")
    filtered4.show()

    val filtered5 = filtered4.select(filtered4("año_eval").cast(IntegerType).as("año_eval"),
      filtered4("rbd").cast(IntegerType).as("rbd"),
      filtered4("pf_pje").cast(StringType).as("pf_pje"),
      filtered4("nom_rbd").cast(StringType).as("nom_rbd"))

    filtered5.show()


    val rdd = filtered5.rdd

    val selected = rdd.map(t =>
      ((t.get(0), t.get(1), t.get(3)),
        (t.get(2).toString.replace(',','.').toDouble,
      if (t.get(2)!=0) 1 else 0)))

    val grouped = selected.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2 ))
    val averaged = grouped.map(t=> (t._1._1, t._1._2, t._1._3 ,t._2._1/t._2._2))

    val classed = averaged.map(t=> Row(t._1,t._2, t._3, t._4,
    if (t._4 < 2.0){"I"}
    else if (t._4 >= 2.0 && t._4 < 2.5) {"B"}
    else if (t._4 >= 2.5 && t._4 < 3.0) {"C"}
    else {"D"}
    ))
    val schema = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="rbd", dataType = IntegerType, nullable = false),
        StructField(name="nom_est", dataType = StringType, nullable = false),
        StructField(name="avg_grades", dataType = DoubleType, nullable = true),
        StructField(name="avg_grades_rank", dataType = StringType, nullable = true)
      )
    )
    val out = spark.sqlContext.createDataFrame(classed, schema)
    out.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}