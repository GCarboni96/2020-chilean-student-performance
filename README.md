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
| Attributes  | 42       | 39           |
| Number of registers  | 57.950.225 | 106.650           |
| Size in bytes | 7.45 GB      | 115 MB           |

## Methods

Both datasets were proccesed using the analytis engine Apache Spark. Specifically, we built our project using Apache Maven and programmed the queries with the language Scala.

For both datasets, we only picked the colums of interest via a column picker method (*EvaluationDataset* and *Performance Dataset*), since most of the information would not be used in any significant way. This also makes the general proccessing of data much faster and efficient, and we can also pick data from any year range we want.

In order to run our queries, the Driver class needs to be executed, which is in charge of starting the Spark session and run the queries contained in the rest of the scala files.  Inside these classes, we first run the column picker method corresponding to the dataset and we inmediately filter the results with null values inside their columns.

Once the dataframe is filtered, we turn it into an RDD which is used to execute the MapReduce operations we need to obtain the desired information. After this, the RDD is turned back into a dataframe and finally its contents are saved on a csv file.

We didn't use all the classes for our analysis, but some of them are worth mentioning. *PerformanceYearlyAverage* calculates the average teacher evaluation score (and their grade) of all the schools in Chile through time between 2013 and 2018. *EvaluationYearlyAverage* returns the average student score and assitance of each school. In the case of Performance, it has its regional (*PerformanceYearlyAverageRegion*) and commune (*PerformanceYearlyAverageRegion*) counterpart which return the averages based on chilean region and commune of Santiago.

In order to cross the info of both datasets, we created queries similar to the last ones mentioned so we could compare scores of students and teachers directly.

Finally, we studied our results by making graphs via Matplotlib and Seaborn, Python libraries specialized for visualizations.


## Results



## Conclusion

## Contributors

Gianluca Carboni

Pablo Helguero

Danilo Moreira
