package com.education

import org.apache.spark.rdd
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.coalesce

case class Reprobation(){
  def run(spark:SparkSession, out_path:String): Unit ={
    val cols = List("agno", "rural_rbd", "sit_fin", "sit_fin_r")
    val picked = ColumnPicker().pick_columns(spark, cols)
    val filtered = picked.filter(picked("sit_fin") === 'R' || picked("sit_fin_r") === 'R')
    val coalesced = filtered.select(filtered.col("agno"),
      filtered.col("rural_rbd"),
      coalesce(filtered.col("sit_fin"), filtered.col("sit_fin_r")).alias("sit_fin")).drop("sit_fin_r")
    coalesced.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}