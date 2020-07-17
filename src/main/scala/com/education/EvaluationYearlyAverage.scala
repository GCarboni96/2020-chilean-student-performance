package com.education

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}



//PROMEDIO GENERAL DE DOCENTES DE CADA ESTABLECIMIENTO POR AÑO, CON PROMEDIO GENERAL
case class EvaluationYearlyAverage() {
  def run(spark:SparkSession, out_path:String) {
    // selección de columnas año, id_estab, ptje_eval y nomb_estb
    val cols = List("año_eval", "rbd", "pf_pje", "nom_rbd")
    // retrieve de data y seleccion de columnas 'cols'
    val picked = EvaluationDataset().pick_columns(spark, cols)
    // exclusion de registros con id_estb vacío o ptje vacío
    val filtered = picked.filter(picked("rbd") =!= "" || picked("pf_pje") =!= 1 || picked("pf_pje") =!=" ")
    val filtered2 = filtered.na.replace(filtered.columns,Map(" " -> "9999"))
    val filtered3 = filtered2.filter(filtered2("rbd") =!= "9999")
    val filtered4 = filtered3.filter(filtered3("pf_pje") =!= "9999")
    val filtered5 = filtered4.select(filtered4("año_eval").cast(IntegerType).as("año_eval"),
      filtered4("rbd").cast(IntegerType).as("rbd"),
      filtered4("pf_pje").cast(StringType).as("pf_pje"),
      filtered4("nom_rbd").cast(StringType).as("nom_rbd"))

    val rdd = filtered5.rdd
    // mapeo a ( (año_eval, rbd, nom_rbd), (pf_pje, varcontador) )
    val selected = rdd.map(t =>
      ((t.get(0), t.get(1), t.get(3)),
        (t.get(2).toString.replace(',','.').toDouble,
      if (t.get(2)!=0) 1 else 0)))
    // reduccion por key ( (año_eval, rbd, nom_rbd), (pf_pje, varcontador) )
    val grouped = selected.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2 ))
    // mapeo a ( año_eval, rbd, nom_rbd, (sum(pf_pje)/ sum(varcontador)) )
    val averaged = grouped.map(t=> (t._1._1, t._1._2, t._1._3 ,t._2._1/t._2._2))
    // mapeo a ( año_eval, rbd, nom_rbd, (sum(pf_pje)/ sum(varcontador)), clasificacion )

    val classed = averaged.map(t=> Row(t._1,t._2, t._3, t._4,
    if (t._4 < 2.0){"I"}
    else if (t._4 >= 2.0 && t._4 < 2.5) {"B"}
    else {"C"}
    ))
    // schemas de salida
    val schema = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="rbd", dataType = IntegerType, nullable = false),
        StructField(name="nom_est", dataType = StringType, nullable = false),
        StructField(name="avg_grades", dataType = DoubleType, nullable = true),
        StructField(name="avg_grades_rank", dataType = StringType, nullable = true)
      )
    )
    val out = spark.sqlContext.createDataFrame(classed, schema)
    // guardado
    out.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}