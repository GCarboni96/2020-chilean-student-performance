# Chilean Student Performance

## Requirements

## Run instructions

## Overview

The goal of the project is to explore and analyze two datasets that are closely related: student's performance and teacher's evaluation. Specifically, we want to know if there is any correlation between these results, in order to confirm if there is any influence from the teacher's performance on the final grades of the students. We would also like to compare the results depending on their region, commune, etc....

## Dataset

We use two datasets from the Ministry of Education of the Government of Chile: Academic Performance and Professor Evaluation. These can be found on the following links:

+ [Academic Performance](http://datos.mineduc.cl/dashboards/19881/rendimiento-academico-por-estudiantes/).

+ [Professor Evaluation](http://datos.mineduc.cl/dashboards/19754/bases-de-datos-de-evaluacion-docente/).

|     | Academic performance | Professor evaluation |
|-----|----------|--------------|
| Attributes | 42 | 39 |
| Number of registers | 57.950.225 | 106.650 |
| Size in bytes | 7.45 GB | 115 MB |

## Methods

Both datasets were proccesed using the analytis engine Apache Spark. Specifically, we built our project using Apache Maven and programmed the queries with the language Scala.

For both datasets, we only picked the colums of interest via a column picker method (*EvaluationDataset* and *Performance Dataset*), since most of the information would not be used in any significant way. This also makes the general proccessing of data much faster and efficient, and we can also pick data from any year range we want.

In order to run our queries, the Driver class needs to be executed, which is in charge of starting the Spark session and run the queries contained in the rest of the scala files.  Inside these classes, we first run the column picker method corresponding to the dataset and we inmediately filter the results with null values inside their columns.

Once the dataframe is filtered, we turn it into an RDD which is used to execute the MapReduce operations we need to obtain the desired information. After this, the RDD is turned back into a dataframe and finally its contents are saved on a csv file.

We didn't use all the classes for our analysis, but some of them are worth mentioning. *PerformanceYearlyAverage* calculates the average teacher evaluation score (and their grade) of all the schools in Chile through time between 2013 and 2018. *EvaluationYearlyAverage* returns the average student score and assitance of each school. In the case of Performance, it has its regional (*PerformanceYearlyAverageRegion*) and commune (*PerformanceYearlyAverageRegion*) counterpart which return the averages based on chilean region and commune of Santiago.

In order to cross the info of both datasets, we created queries similar to the last ones mentioned so we could compare scores of students and teachers directly.

Finally, we studied our results by making graphs via Matplotlib and Seaborn, Python libraries specialized for visualizations.


## Results

**Annual student reprobation by dependency**
![Reprobacion Anual segun dependencia](https://i.imgur.com/sB46fNB.png)

**Annual student reprobation by rurality**
![Reprobacion anual segun tipo de localidad](https://i.imgur.com/890X2uu.png)

**Student's performance and assistance by school (2018)**
![Rendimiento alumnos y su asistencia por establecimiento (2018)](https://i.imgur.com/lzM9lBU.png)

**Student's yearly assistance**
![Asistencia alumnos por año](https://i.imgur.com/K0IQ02C.png)

**Student's yearly performance**
![Rendimiento alumnos por año](https://i.imgur.com/tOaS1s0.png)

**Teacher's grades by school**
![Cantidad clasificaciones docentes por establecimiento](https://i.imgur.com/slKR7dR.png)

**Teacher's grades by commune of Santiago**
![Cantidad clasificaciones docentes por comuna de Santiago](https://i.imgur.com/k5Usd9q.png)

**Student's performance and teacher's evaluation by region (2017)**
![Rendimiento alumnos y docentes por region (2017)](https://i.imgur.com/mATReIf.png)

**Student's performance and teacher's evaluation by commune of Santiago (2013 vs 2017)**
![Rendimiento alumnos y docentes en Santiago (2013 vs 2017)](https://i.imgur.com/UUi9FsZ.jpg)



## Conclusion

1. se puede ver que la tasa de reprobacion baja a partir del año 2010-2012, y el efecto de la revolcion pinguina y movilizacion estuaintil 2006 y 2011

2. se nota la mayor reprobacion en los urbanos a partir del 2009 y como el movimiento no afecta a los rurales

3. se concentra en 80-100 asistencia y 5-6.5 de promedio. esta conecntracion es esperada

4. la asistencia no presenta tantas bajas ni subidas (2011 es bajisima)

5. en general esta sube a lo largo de los años

6 y 7, se nota que como en los años 2016 y 2017 los profes aumentan su rendimiento, y por alguna razon esta baja el 2018

8. el promedio de los alumnos no cambia mucho a lo largo de las regiones. el de los docentes tambien es homogeneo pero no tanto

9. en general se puede apreciar que en santiago hay un amuento tantp de los profesores como de los alumnos. y los outliers mantienen su jerarquia frente a las demas comunas.

## Contributors

Gianluca Carboni

Pablo Helguero

Danilo Moreira
