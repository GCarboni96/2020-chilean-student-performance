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

    EvaluationYearlyAverage().run(spark, "out/docentes/establecimiento")
    EvaluationYearlyAverageCount().run(spark, "out/docentes/count/establecimiento")
    EvaluationYearlyAverageRegion().run(spark, "out/docentes/regiones")
    EvaluationYearlyAverageMetroComuna().run(spark, "out/docentes/comunasmetropolitana")
    PerformanceYearlyAverage().run(spark, "out/alumnos/establecimiento")
    PerformanceYearlyAverageByYear().run(spark, "out/alumnos/anual")
    EvaluationAndPerformanceYearlyAverage().run(spark, "out/alumnosvsdocentes/establecimiento")
    EvaluationAndPerformanceYearlyAverageRegion().run(spark, "out/alumnosvsdocentes/regiones")
    EvaluationAndPerformanceYearlyAverageMetroComuna().run(spark, "out/alumnosvsdocentes/comunasmetropolitana")
    PerformanceReprobation().run(spark,"out/reprobation")
    PerformanceYearlyAverage().run(spark, "out/yearly")
    PerformanceStats().run(spark, "out/stats/performance")
    EvaluationStats().run(spark, "out/stats/evaluation")
    //PerformanceYearlyAverageByGender().run(spark, "out/alumnos/genero/establecimiento")
    //PerformanceYearlyAverageRegionByGender().run(spark, "out/alumnos/genero/regiones")
  }
}
