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

    // Count every (year, rural indicator, final situation, dependance code) tuple
    val aggregated = coalesced.groupBy("agno", "rural_rbd", "sit_fin", "cod_depe").agg(count(lit(1)).alias("count"))
    aggregated.cache()
    val totals_rural = aggregated.drop("sit_fin").groupBy("agno", "rural_rbd").agg(sum("count").alias("total_rural"))
    val totals_depe = aggregated.drop("sit_fin").groupBy("agno", "cod_depe").agg(sum("count").alias("total_depe"))

    // Filter aggregated to get reprobated students
    val filtered = aggregated.filter(aggregated("sit_fin") === "R").drop("sit_fin")
    filtered.cache()
    val rep_rural = filtered.groupBy("agno", "rural_rbd").agg(sum("count").alias("reprobated"))
    val rep_depe = filtered.groupBy("agno", "cod_depe").agg(sum("count").alias("reprobated"))

    // Join with totals to get ration of reprobated students and total students
    val joined_rural = rep_rural.join(totals_rural, Seq("agno", "rural_rbd"))
      .select(col("agno"), col("rural_rbd"), (col("reprobated") / col("total_rural")).alias("rural_reprobation"))
      .filter(col("rural_rbd") =!= "\"\"")
    val joined_depe = rep_depe.join(totals_depe, Seq("agno", "cod_depe"))
      .select(col("agno"), col("cod_depe"), (col("reprobated") / col("total_depe")).alias("depe_reprobation"))

    joined_rural.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path+"/rural")
    joined_depe.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path+"/depe")
  }
}