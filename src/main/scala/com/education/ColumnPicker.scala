package com.education

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.functions.{lit, col}

import scala.util.Try

object ColumnPicker{
  def run(spark: SparkSession, out:String, cols: List[String]) {
    def hasColumn(df: DataFrame, path: String) = Try(df(path)).isSuccess

    val dfs = new Array[DataFrame](2019-2002+1)

    for(i<- 2002 to 2019) {
      val path = s"D:\\Proyecto Ministerio Educación\\src\\main\\resources\\performance\\performance_$i.csv"
      var df = spark.read.format("csv")
        .option("header", "true").option("inferschema", "true")
        .option("sep", ";")
        .load(path)

      for (name<-cols){
        if (!hasColumn(df, name)) {
          df = df.withColumn(name, lit(null).cast(StringType))
        }
      }
     
      val picked = df.select(cols.map(col): _*)

      val filtered = picked.filter(t => t.get(11) != "0" || t.get(12) != "0")

      dfs(i-2002) = filtered
    }

    val total = dfs.reduce(_ union _)

    total.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out)



    // val df = spark.read.format("csv").option("header","true").option("inferschema", "true").option("sep",";").load(path)
    //val picked = df.select("agno","nom_rbd", "cod_reg_rbd", "nom_reg_rbd_a", "cod_pro_rbd", "cod_com_rbd", "nom_com_rbd",
    //  "rural_rbd", "cod_ense2", "cod_grado", "gen_alu", "prom_gral", "asistencia")
    //picked.coalesce(1).write.option("header", "true").option("delimiter",";").csv("out/performance_cleaning/")

    // "agno","nom_rbd", "cod_reg_rbd", "nom_reg_rbd_a",
    // "cod_pro_rbd", "cod_com_rbd", "nom_com_rbd",
    // "rural_rbd", "cod_ense2", "cod_grado", "gen_alu"
    // prom_gral", "asistencia"

  /*
    val rdd = spark.read.option("sep", ";").csv(args(0)).rdd
    val schema = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="school_name", dataType = StringType, nullable = false),
        StructField(name="reg_code", dataType = IntegerType, nullable = true),
        StructField(name="reg_name", dataType = StringType, nullable = true),
        StructField(name="prov_code", dataType = IntegerType, nullable = true),
        StructField(name="com_code", dataType = IntegerType, nullable = true),
        StructField(name="com_name", dataType = StringType, nullable = true),
        StructField(name="rural", dataType = IntegerType, nullable = true),
        StructField(name="level", dataType = IntegerType, nullable = true),
        StructField(name="grade_code", dataType = IntegerType, nullable = true),
        StructField(name="gender", dataType = IntegerType, nullable = true),
        StructField(name="average_grade", dataType = IntegerType, nullable = false),
        StructField(name="assistance", dataType = IntegerType, nullable = false)
      )
    )
    val picked = rdd.map(t => Row(t.get(0),t.get(3),t.get(6),t.get(7),t.get(8),
      t.get(9),t.get(10),t.get(15),t.get(18),t.get(19),t.get(25),t.get(37),t.get(38)))

    val df = spark.createDataFrame(picked, schema)
    df.coalesce(1).write.option("header", "true").csv(args(1))
    */
  }
}