import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import seaborn as sns


# x = regiones (codigo_region)
# y = promedio (avg_grades)

df = pd.read_csv(r"D:\\Documentos_U\\2020-1\\Patos\\Proyecto\\chilean-student-performance\\out\\docentes\\establecimiento\\results-docentes-establecimiento.csv", sep = ';')



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


fig = plt.figure(figsize=(10, 7))
fig.set_facecolor('#F5FBFB')
sns.set(font_scale=2,style='ticks')

color = ["#5C5CC4", "#E7478D", "#90C4FE", "#1BBC9D", "#FFBB4C"]
ax = sns.countplot(x="year", hue="calificacion" ,data=df,palette=color)
plt.legend(title='Clasificacion', loc='upper center', labels=['Básico', 'Competente', 'Insuficiente'])
plt.title("Cantidad Clasificaciones docentes por establecimiento (2013-2018)")
plt.xlabel("Año")

plt.ylabel("Cantidad")
plt.show()

"""
X = df['year']
#X = X + np.random.normal(size=X.shape)*0.07
Y = 1866
#Y = Y + np.random.normal(size=X.shape)*0.03
plt.bar(X, Y, width=0.8, bottom=None, align='center', data=None)


plt.xlim([2012,2019])
plt.ylim([0,10])
plt.xlabel("Años")
plt.ylabel("Cantidad")
plt.title("Cantidad Clasificaciones docentes (2013-2018)")
plt.legend(['C'
            ], framealpha=0.5)


plt.show()
"""