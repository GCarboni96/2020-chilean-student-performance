import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import matplotlib.cm as cm
import random
import seaborn as sns

# x = regiones (codigo_region)
# y = promedio (avg_grades)

df = pd.read_csv(r"D:\\Documentos_U\\2020-1\\Patos\\Proyecto\\chilean-student-performance\\out\\alumnosvsdocentes\\comunasmetropolitana\\results-alumnosvsdocentes-comunas.csv", sep = ';')



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






labels =['Santiago','Cerrillos','Cerro Navia','Conchali','El Bosque','E. Central','Huechuraba','Indepen.','La Cisterna','La Florida','La Granja','La Pintana','La Reina', 'Las Condes','Lo Barnechea','Lo Espejo','Lo Prado','Macul','Maipú','Ñuñoa','P.A.C','Peñalolen','Providencia','Pudahuel','Quilicura','Q. Normal','Recoleta','Renca', 'San Joaquín','San Miguel','San Ramon','Vitacura']

x = []
fig, ax = plt.subplots()

x = np.arange(32)





width = 0.2

m=df[df['year']==2013]

#ax.bar(x - width/2, df[df['year']==2017]['avg_grades_alumnos'], color=([255/255, 109/255, 197/255]),width= width)
# set x-axis label

m.avg_grades_alumnos.plot(kind='bar', color=([255/255, 109/255, 197/255]), ax=ax, width=width, position=1)
ax.set_xlabel("Comunas",fontsize=14)
# set y-axis label
ax.set_ylabel("Promedio Alumnos",color=([255/255, 109/255, 197/255]),fontsize=14)
ax.set_ylim([1, 7])


# twin object for two different y-axis on the sample plot
ax2=ax.twinx()
# make a plot with different y-axis using second axis object
#ax2.bar(x + width/2, df[df['year']==2017]['avg_grades_docentes'],color=([84/255, 88/255, 255/255]),width= width)
m.avg_grades_docentes.plot(kind='bar', color=([84/255, 88/255, 255/255]), ax=ax2, width=width, position=0)
ax2.set_ylabel("Promedio Docentes",color=([84/255, 88/255, 255/255]),fontsize=14)
ax2.set_ylim([1, 4])
plt.xlabel("Comunas")

plt.xlim([-1,32])
ax.set_xticks(x)
ax.set_xticklabels(labels)
plt.title("Rendimiento alumnos y docentes por comunas de Santiago (2013)")

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