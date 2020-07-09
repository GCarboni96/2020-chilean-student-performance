package com.education

import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{lit, col}

import scala.util.Try

case class PerformanceDatasetForCross(){
  def hasColumn(df: DataFrame, path: String): Boolean = Try(df(path)).isSuccess

  def pick_columns(spark: SparkSession, cols: List[String]): DataFrame = {
    val dfs = new Array[DataFrame](2018 - 2013 + 1)

    for (i <- 2013 to 2018) {
      val path = s"D:\\Documentos_U\\2020-1\\Patos\\Proyecto\\chilean-student-performance\\chilean-student-performance\\src\\main\\resources\\performance\\performance_$i.csv"
      var df = spark.read.format("csv")
        .option("header", "true").option("inferschema", "true")
        .option("sep", ";")
        .load(path)

      for (name <- cols) {
        if (!hasColumn(df, name)) {
          df = df.withColumn(name, lit(null).cast(StringType))
        }
      }

      val picked = df.select(cols.map(col): _*)
      dfs(i - 2013) = picked
    }

    val total = dfs.reduce(_ union _)
    return total
  }
}