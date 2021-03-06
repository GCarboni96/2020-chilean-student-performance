package com.education

import org.apache.spark.sql.types._
import org.apache.spark.sql.{ Row, SparkSession }
import org.apache.spark.sql.functions._

case class PerformanceYearlyAverageByGender() {
  def run(spark:SparkSession, out_path:String){
    // selección de columnas año, id_estab, nombre_estab, promedio_gral, genero_alum
    val cols = List("agno", "rbd", "nom_rbd", "prom_gral", "gen_alu")
    // retrieve de data y seleccion de columnas 'cols'
    val picked = PerformanceDataset().pick_columns(spark, cols)


    // exclusion de registros sin datos de id_estb(rbd) o sin promedio gral de estudiantes Masculinos
    val filteredMasc = picked.filter(picked("prom_gral") =!= 0)
    filteredMasc.cache()
    val filteredMasc2 = filteredMasc.filter(filteredMasc("gen_alu") === 1)
    filteredMasc2.cache()

    val rddMasc = filteredMasc2.rdd



    // exclusion de registros sin datos de id_estb(rbd) o sin promedio gral de estudiantes Femeninos
    val filteredFem = picked.filter(picked("prom_gral") =!= 0)
    filteredFem.cache()
    val filteredFem2 = filteredFem.filter(filteredFem("gen_alu") === 2)
    filteredFem2.cache()
    filteredFem2.show()
    val rddFem = filteredFem2.rdd



    
    // mapeo a ( (agno, rbd, nom_rbd), (prom_gral, varcontador) )
    val selectedMasc = rddMasc.map(t =>
      ((t.get(0), t.get(1), t.get(2)),
      (t.get(3).toString.replace(',','.').toDouble, if (t.get(4) != 0) 1 else 0)))
        
    // mapeo a ( (agno, rbd, nom_rbd), (prom_gral, varcontador) )
    val selectedFem = rddFem.map(t =>
      ((t.get(0), t.get(1), t.get(2)),
      (t.get(3).toString.replace(',','.').toDouble, if (t.get(4) != 0) 1 else 0)))
   
    // reducción por key (agno, prom_gral), (rbd, varcontador)
    val groupedMasc = selectedMasc.reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))
    // reducción por key (agno, prom_gral), (rbd, varcontador)
    val groupedFem = selectedFem.reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))

    // mapeo a ( agno, rbd, nom_rbd, sum(prom_gral) / sum(varcontador)[numEstudiantesMasculinos])
    val averagedMasc = groupedMasc.map(t => (t._1._1, t._1._2, t._1._3, t._2._1/t._2._2))
    // mapeo a ( agno, rbd, nom_rbd, sum(prom_gral) / sum(varcontador)[numEstudiantesFemeninos])
    val averagedFem = groupedFem.map(t => (t._1._1, t._1._2, t._1._3, t._2._1/t._2._2))
    
    // etiquetado segun promedio gral total
    val classedMasc = averagedMasc.map(t => Row(t._1, t._2, t._3, t._4,
      if (t._4 < 2.0){"I"}
      else if (t._4 >= 2.0 && t._4 < 2.5) {"B"}
      else if (t._4 >= 2.5 && t._4 < 3.0) {"C"}
      else {"D"} ))
    val classedFem = averagedFem.map(t => Row(t._1, t._2, t._3, t._4,
      if (t._4 < 2.0){"I"}
      else if (t._4 >= 2.0 && t._4 < 2.5) {"B"}
      else if (t._4 >= 2.5 && t._4 < 3.0) {"C"}
      else {"D"} ))
    // schemas de salida
    val schemaMasc = StructType(
        List(
            StructField(name="year", dataType = IntegerType, nullable = true),
            StructField(name="rbd", dataType = IntegerType, nullable = false),
            StructField(name="nom_est", dataType = StringType, nullable = false),
            StructField(name="avg_grades_masculino", dataType = DoubleType, nullable = true),
            StructField(name="avg_grades_rank_masculino", dataType = StringType, nullable = true)
        ))
    val schemaFem = StructType(
        List(
            StructField(name="year", dataType = IntegerType, nullable = true),
            StructField(name="rbd", dataType = IntegerType, nullable = false),
            StructField(name="nom_est", dataType = StringType, nullable = false),
            StructField(name="avg_grades_femenino", dataType = DoubleType, nullable = true),
            StructField(name="avg_grades_rank_femenino", dataType = StringType, nullable = true)
        ))
    val outMasc = spark.sqlContext.createDataFrame(classedMasc, schemaMasc)
    val outFem = spark.sqlContext.createDataFrame(classedFem, schemaFem)

    // Haciendo Join de los resultados por genero
    val outJoined = outMasc.join(outFem, Seq("year", "rbd"), "inner")
      .select(col("year"), col("rbd"),
      outFem.col("avg_grades_femenino"), outFem.col("avg_grades_rank_femenino"), 
      outMasc.col("avg_grades_masculino"), outMasc.col("avg_grades_rank_masculino"))
    
    // guardado
    outJoined.coalesce(1).write.option("header", "true").option("delimiter",";").csv(out_path)
  }
}