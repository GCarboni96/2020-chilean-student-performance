import matplotlib.pyplot as plt
import pandas as pd
import numpy as np


# x = regiones (codigo_region)
# y = promedio (avg_grades)

df = pd.read_csv(r"D:\\Documentos_U\\2020-1\\Patos\\Proyecto\\chilean-student-performance\\out\\alumnos\\anual\\results-alumnos-anual.csv", sep = ';')



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



X = df['year']
#X = X + np.random.normal(size=X.shape)*0.07
Y = df['avg_grades']
#Y = Y + np.random.normal(size=X.shape)*0.03
plt.xticks(range(2002, 2020, 2))
plt.bar(X, Y, width=0.8, bottom=None, align='center', data=None, color =([255/255, 109/255, 197/255]))


plt.xlim([2002,2019])
plt.ylim([5,7])
plt.xlabel("Años")
plt.ylabel("Promedio Final Alumnos")
plt.title("Rendimientos alumnos por año (2003-2018)")
plt.legend(['promedio final'
            ], framealpha=0.5)


plt.show()