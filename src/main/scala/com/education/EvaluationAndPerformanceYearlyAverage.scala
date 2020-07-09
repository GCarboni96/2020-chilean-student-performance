package com.education

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions._


//PROMEDIO GENERAL DE DOCENTES DE CADA ESTABLECIMIENTO POR AÑO, CON PROMEDIO GENERAL
case class EvaluationAndPerformanceYearlyAverage() {
  def run(spark:SparkSession, out_path:String) {



    // SACAMOS AÑO, ID_ESTB, NOMBRE_ESTB, AVG_GRADES PARA DOCENTES
    val cols = List("año_eval", "rbd", "pf_pje", "nom_rbd")
    val picked = EvaluationDataset().pick_columns(spark, cols)
    val filtered = picked.filter(picked("rbd") =!= "" || picked("pf_pje") =!= 1 || picked("pf_pje") =!=" ")
    val filtered2 = filtered.na.replace(filtered.columns,Map(" " -> "9999"))
    val filtered3 = filtered2.filter(filtered2("rbd") =!= "9999")
    val filtered4 = filtered3.filter(filtered3("pf_pje") =!= "9999")
    val filtered5 = filtered4.select(filtered4("año_eval").cast(IntegerType).as("año_eval"),
      filtered4("rbd").cast(IntegerType).as("rbd"),
      filtered4("pf_pje").cast(StringType).as("pf_pje"),
      filtered4("nom_rbd").cast(StringType).as("nom_rbd"))

    val rdd = filtered5.rdd

    val selected = rdd.map(t =>
      ((t.get(0), t.get(1), t.get(3)),
        (t.get(2).toString.replace(',','.').toDouble,
      if (t.get(2)!=0) 1 else 0)))

    val grouped = selected.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2 ))
    val averaged_docentes = grouped.map(t=> Row(t._1._1, t._1._2, t._1._3 ,t._2._1/t._2._2))


    val schema_docentes = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="rbd", dataType = IntegerType, nullable = false),
        StructField(name="nom_est", dataType = StringType, nullable = false),
        StructField(name="avg_grades_docentes", dataType = DoubleType, nullable = true)
      )
    )
    val out_docentes = spark.sqlContext.createDataFrame(averaged_docentes, schema_docentes)

    // SACAMOS AÑO, ID_ESTB, NOMBRE_ESTB, AVG_GRADES, ASISTENCIA PARA ALUMNOS

    val cols2 = List("agno", "rbd", "nom_rbd", "prom_gral", "asistencia")
    val picked2 = PerformanceDatasetForCross().pick_columns(spark, cols2)
    val filtered10 = picked2.filter(picked2("prom_gral") =!= 0 || picked2("asistencia") =!= 0)
    val rdd2 = filtered10.rdd

    val selected2 = rdd2.map(t =>
      ((t.get(0), t.get(1),t.get(2)),
        (t.get(3).toString.replace(',','.').toDouble,
          if (t.get(3)!=0) 1 else 0,
          t.get(4).toString.replace(',','.').toDouble,
          if (t.get(4)!=0) 1 else 0)))

    val grouped2 = selected2.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2, a._3 + b._3 ,a._4 + b._4))
    val averaged_alumnos = grouped2.map(t=> Row(t._1._1, t._1._2, t._1._3, t._2._1/t._2._2, t._2._3/t._2._4))

    val schema_alumnos = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="rbd", dataType = IntegerType, nullable = false),
        StructField(name="nom_est", dataType = StringType, nullable = false),
        StructField(name="avg_grades_alumnos", dataType = DoubleType, nullable = true),
        StructField(name="asistencia", dataType = DoubleType, nullable = true)
      )
    )
    val out_alumnos = spark.sqlContext.createDataFrame(averaged_alumnos, schema_alumnos)



    //HACEMOS JOIN

    val joined= out_docentes.join(out_alumnos,Seq("year", "rbd"), "inner")
      .select(col("year"), col("rbd"), out_docentes.col("avg_grades_docentes") ,out_alumnos.col("avg_grades_alumnos"))
    joined.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}