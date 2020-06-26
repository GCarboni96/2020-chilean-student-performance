package com.education

import org.apache.spark.rdd
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

case class Reprobation(){
  def run(spark:SparkSession, out_path:String): Unit ={
    // Pick columns
    val cols = List("agno", "rural_rbd", "sit_fin", "sit_fin_r", "cod_depe", "cod_depe2")
    val picked = PerformanceDataset().pick_columns(spark, cols)

    // Coalesce student's final situation and establishment dependence code
    val coalesced = picked.select(
      col("agno"),
      col("rural_rbd"),
      coalesce(col("sit_fin"), col("sit_fin_r")).alias("sit_fin"),
      when(col("cod_depe2").isNotNull, col("cod_depe2"))
        .otherwise(when(col("cod_depe").isNull, col("cod_depe"))
          .otherwise(when(col("cod_depe")<=2, lit(1))
            .otherwise(col("cod_depe") - lit(1)))).alias("cod_depe")
    )

    // Aggregate
    val aggregated = coalesced.groupBy("agno", "rural_rbd", "sit_fin", "cod_depe").agg(count(lit(1)).alias("count"))
    aggregated.cache()

    val totals = aggregated.drop("sit_fin").groupBy("agno", "rural_rbd").agg(sum("count").alias("total"))

    val filtered = aggregated.filter(aggregated("sit_fin") === "R").drop("sit_fin")
    val joined = filtered.join(totals, Seq("agno", "rural_rbd"))

    joined.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}