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

    val cols = List("agno", "rbd", "nom_rbd", "cod_reg_rbd", "nom_reg_rbd_a", "cod_pro_rbd", "cod_com_rbd", "nom_com_rbd",
    "rural_rbd", "cod_ense2", "cod_grado", "gen_alu", "prom_gral", "asistencia")

    //ColumnPicker.run(spark, "out/performance_cleaning/")
    YearlyAverage.run(spark, "out/performance_cleaning/*.csv", "out/yearly")
  }

}
