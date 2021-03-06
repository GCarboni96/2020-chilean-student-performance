package com.education

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions._


//PROMEDIO GENERAL DE DOCENTES DE CADA COMUNA DE SANTIAGO
case class EvaluationYearlyAverageMetroComuna() {
  def run(spark:SparkSession, out_path:String) {
    // selección de columnas año, id_region, id_comuna, nomb_comuna y pje
    val cols = List("año_eval", "cod_reg_rbd","cod_com_rbd", "nom_com_rbd","pf_pje")
    // retrieve de data y seleccion de columnas 'cols'
    val picked = EvaluationDataset().pick_columns(spark, cols)
    // exclusion de registros con co_reg_rbd vacío, y que no sean de la region metropolitana
    val filtered = picked.filter((picked("cod_reg_rbd") =!=" " || picked("pf_pje") =!= 1 || picked("pf_pje") =!=" ") && (picked("cod_reg_rbd") ===13))
    val rdd = filtered.rdd
    // mapeo a ( (año_eval, cod_com_rbd, nom_com_rbd), (pf_pje, varcontador) )

    val selected = rdd.map(t =>
      ((t.get(0), t.get(2), t.get(3)),
        (t.get(4).toString.replace(',','.').toDouble,
          if (t.get(4)!=0) 1 else 0)))

    // reduccion por key ( (año_eval, cod_com_rbd, nom_com_rbd), (pf_pje, varcontador) )
    val grouped = selected.reduceByKey((a,b) =>  (a._1 + b._1 , a._2 + b._2 ))
    // mapeo a ( año_eval, cod_com_rbd, nom_com_rbd, (sum(pf_pje)/ sum(varcontador)) )
    val averaged_docentes = grouped.map(t=> (t._1._1, t._1._2 , t._1._3 ,t._2._1/t._2._2))
    // mapeo a ( año_eval, cod_com_rbd, nom_com_rbd, (sum(pf_pje)/ sum(varcontador)), clasificacion )
    val classed = averaged_docentes.map(t=> Row(t._1,t._2, t._3,t._4,
      if (t._4 < 2.0){"I"}
      else if (t._4 >= 2.0 && t._4 < 2.5) {"B"}
      else if (t._4 >= 2.5 && t._4 < 3.0) {"C"}
      else {"D"}
    ))

    // schemas de salida
    val schema_docentes = StructType(
      List(
        StructField(name="year", dataType = IntegerType, nullable = true),
        StructField(name="cod_comuna", dataType = IntegerType, nullable = false),
        StructField(name="nomb_comuna", dataType = StringType, nullable = false),
        StructField(name="avg_grades_docentes", dataType = DoubleType, nullable = true),
        StructField(name="avg_grades_rank", dataType = StringType, nullable = true)
      )
    )
    val out = spark.sqlContext.createDataFrame(classed, schema_docentes)
    // guardado
    out.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}