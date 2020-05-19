package com.education

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Driver {
  def main(args: Array[String]) {
    val sparkConf: SparkConf = new SparkConf().set("spark.app.name", "Epic lab5")

    val spark = SparkSession
      .builder()
      .master("local[*]")
      .config("spark.executor.memory", "4g")
      .config("spark.driver.memory", "8g")
      .config(sparkConf)
      .getOrCreate()

    //ColumnPicker.run(spark, "out/performance_cleaning/")
    YearlyAverage.run(spark, "out/performance_cleaning/*.csv", "out/yearly")
  }

}
