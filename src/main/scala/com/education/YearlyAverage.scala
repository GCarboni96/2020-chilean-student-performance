package com.education

import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

object YearlyAverage {
  def run(spark:SparkSession, in: String, out:String) {
    val rdd = spark.read.format("csv")
    .option("header", "true").option("inferschema", "true")
    .option("sep", ";")
    .load(in).rdd
    // "agno", "rbd", "nom_rbd", "cod_reg_rbd",
    // "nom_reg_rbd_a", "cod_pro_rbd", "cod_com_rbd",
    // "nom_com_rbd", "rural_rbd", "cod_ense2",
    // "cod_grado" "gen_alu", "prom_gral",
    // "asistencia"
    val selected = rdd.map(t =>
      ((t.get(0), t.get(1)),
        (t.get(12).toString.replace(',','.').toDouble,
      if (t.get(12)!=0) 1 else 0,
          t.get(13).toString.replace(',','.').toDouble,
          if (t.get(13)!=0) 1 else 0)))
    val grouped = selected.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2, a._3 + b._3 ,a._4 + b._4))
    val averaged = grouped.map(t=> Row(t._1._1, t._1._2, t._2._1/t._2._2, t._2._3/t._2._4))
    val schema = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="rbd", dataType = IntegerType, nullable = false),
        StructField(name="avg_grades", dataType = DoubleType, nullable = true),
        StructField(name="avg_assistance", dataType = DoubleType, nullable = true)
      )
    )

    val df = spark.sqlContext.createDataFrame(averaged, schema)
    df.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out)

    //averaged.saveAsTextFile(out)
  }
}
