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


plt.rcParams.update({'font.size': 14})
X = df['year']
#X = X + np.random.normal(size=X.shape)*0.07
Y = df['avg_assistance']
#Y = Y + np.random.normal(size=X.shape)*0.03


fig = plt.figure(figsize=(10, 7))
fig.set_facecolor('#F5FBFB')
plt.bar(X, Y, width=0.8, bottom=None, align='center', data=None, color= ([84/255, 88/255, 255/255]))

ax = plt.gca()
ax.set_facecolor('#F5FBFB')

ax.set_xticks([2003,2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015,2016,2017,2018])
plt.xlim([2002,2019])
plt.ylim([70,100])
plt.xlabel("Años")
plt.ylabel("Asistencia")
plt.title("Asistencia alumnos por año (2003-2018)")


plt.show()