import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import matplotlib.cm as cm
import random
import seaborn as sns
import itertools
# x = regiones (codigo_region)
# y = promedio (avg_grades)

df = pd.read_csv(r"D:\\Documentos_U\\2020-1\\Patos\\Proyecto\\chilean-student-performance\\out\\alumnosvsdocentes\\regiones\\results-alumnosvsdocentes-region.csv", sep = ';')



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


#sns.set(style="darkgrid")






labels =['I','II','III','IV','V','VI','VII','VIII','IX','X','XI','XII','RM', 'XIV','XV']

x = np.arange(15)

fig, ax = plt.subplots()



width = 0.35

m=df[df['year']==2016]

m.avg_grades_alumnos.plot(kind='bar', color=([255/255, 109/255, 197/255]), ax=ax, width=width, position=1)



#ax.bar(x=df[df['year']==2017]['region']-width, y=df[df['year']==2017]['avg_grades_alumnos'], color=([255/255, 109/255, 197/255]),width= width, height = 10)
# set x-axis label
ax.set_xlabel("Regiones",fontsize=14)
# set y-axis label
ax.set_ylabel("Promedio Alumnos",color=([255/255, 109/255, 197/255]),fontsize=14)
ax.set_ylim([1, 7])


# twin object for two different y-axis on the sample plot
ax2=ax.twinx()
# make a plot with different y-axis using second axis object
m.avg_grades_docentes.plot(kind='bar', color=([84/255, 88/255, 255/255]), ax=ax2, width=width, position=0)
#ax2.bar(x=df[df['year']==2017]['region']+width, y=df[df['year']==2017]['avg_grades_docentes'],color=([84/255, 88/255, 255/255]),width= width, height = 10)

ax2.set_ylabel("Promedio Docentes",color=([84/255, 88/255, 255/255]),fontsize=14)
ax2.set_ylim([1, 4])
plt.xlabel("Regiones")
ax.set_xticks(x)
ax.set_xticklabels(labels)
plt.xlim([-1,15])
plt.title("Rendimiento alumnos y docentes por region (2016)")


plt.show()
# save the plot as a file

"""

X = df[df['year']==2017]['region']
color=(random.random(), random.random(), random.random(), 1.0)
#X = X + np.random.normal(size=X.shape)*0.07
Y = df[df['year']==2017]['avg_grades_alumnos']
#Y = Y + np.random.normal(size=X.shape)*0.03



X2 = df[df['year']==2017]['region']
#X = X + np.random.normal(size=X.shape)*0.07
Y2 = df[df['year']==2017]['avg_grades_docentes']
#Y = Y + np.random.normal(size=X.shape)*0.03
plt.bar(X, Y,secondary_y= Y2, alpha=0.5)


plt.xlabel("Regiones")
plt.ylabel("Promedios")
plt.title("Rendimientos alumnos y docentes por region (2017)")
plt.legend(['Alumnos','Regiones','III','IV','V','VI','VII','VIII','IX','X','XI','XIII','RM', 'XIV','XV'
            ], framealpha=1)


plt.show()
"""