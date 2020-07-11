import matplotlib.pyplot as plt
import pandas as pd
import numpy as np


# x = regiones (codigo_region)
# y = promedio (avg_grades)

df = pd.read_csv(r"D:\\Documentos_U\\2020-1\\Patos\\Proyecto\\chilean-student-performance\\out\\alumnosvsdocentes\\establecimiento\\results-alumnosvsdocentes-establecimiento.csv", sep = ';')



#df.plot()  # plots all columns against index
#df.plot(kind='scatter',x='codigo_region',y='avg_grades', ) # scatter plot
#df.plot(kind='density')  # estimate density function

co = [(1, 0.5, 0),
      (0.85, 0.19, 0.23),
      (0.15, 0.23, 0.73),
      (0.13, 0.53, 0.21),
      (0.5, 0.3, 0.9)]


cat_1 = df[df['year']==2013]


cat_2 = df[df['year']==2014]


cat_3 = df[df['year']==2015]


cat_4 = df[df['year']==2016]


cat_5 = df[df['year']==2017]


cat_6 = df[df['year']==2018]





for i in range(2018,2019):
    X = df[df['year']==i]['avg_grades_docentes']
    #X = X + np.random.normal(size=X.shape)*0.07
    Y = df[df['year']==i]['avg_grades_alumnos']
    Y = Y + np.random.normal(size=X.shape)*0.03
    plt.scatter(X, Y, alpha=0.5, label='{0}'.format(i), zorder=i,  marker='s', s=14)

plt.xlim([1,4])
plt.ylim([1,7])
plt.xlabel("Promedio Docentes")
plt.ylabel("Promedio Alumnos")
plt.title("Rendimientos alumnos y docentes por establecimiento")
plt.legend(['2018'
            ], framealpha=0.5)


plt.show()