package com.education

import org.apache.spark.sql.functions.{col, count, lit}
import org.apache.spark.sql.SparkSession

case class PerformanceStats(){
  def run(spark:SparkSession, output:String): Unit ={
    // Pick columns
    val cols = List("agno", "rural_rbd", "sit_fin", "sit_fin_r", "cod_depe", "cod_depe2")
    val picked = PerformanceDataset().pick_columns(spark, cols).cache()

    // Count every (year, rural indicator, final situation, dependance code) tuple
    val year_agg1 = picked.groupBy("agno").agg(count(lit(1)).alias("count")).cache()
    val total_year = year_agg1.groupBy().sum("count").first.get(0)
    print(total_year)
    val year_agg2 = year_agg1.select(col("agno"), col("count")/lit(total_year))
    year_agg2.coalesce(1).write.option("header", "true").option("delimiter",";").csv(output)
  }
}