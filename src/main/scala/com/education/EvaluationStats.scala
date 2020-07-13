package com.education

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, count, lit}

case class EvaluationStats(){
  def run(spark:SparkSession, output:String): Unit ={
    // Pick columns
    val cols = List("año_eval")
    val picked = EvaluationDataset().pick_columns(spark, cols).cache()

    val year_agg1 = picked.groupBy("año_eval").agg(count(lit(1)).alias("count")).cache()
    val total_year = year_agg1.groupBy().sum("count").first.get(0)
    val year_agg2 = year_agg1.select(col("año_eval"), (col("count")/lit(total_year)).alias("proportion"))
    year_agg2.coalesce(1).write.option("header", "true").option("delimiter",";").csv(output)
  }
}
