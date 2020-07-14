import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import matplotlib.cm as cm
import random
import glob
from matplotlib.lines import Line2D

fig = plt.figure(figsize=(10, 7))
fig.set_facecolor('#F5FBFB')

path = glob.glob("../../../../out/alumnosvsdocentes/comunasmetropolitana/*.csv")[0]
df = pd.read_csv(path, sep = ';')

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


color=(random.random(), random.random(), random.random(), 1.0)

colormap = np.array(['r', 'g', 'b'])

colors = cm.rainbow(np.linspace(0, 1, 15))

dict_markers = 	{
    13113: "P",
    13114: "s",
    13115: "X",
    13123: "^",
    13132: "D"
}

for i in range(13101,13133):
    X = df[df['cod_comuna']==i][df['year']==2013]['avg_grades_docentes']

    Y = df[df['cod_comuna']==i][df['year']==2013]['avg_grades_alumnos']
    if (i == 13113 or i == 13114 or i == 13115 or i == 13123 or i == 13132):
        marker= dict_markers[i]
        plt.scatter(X, Y,color="#5C5CC4",alpha=1, zorder=i,  marker=marker, s=80)
    else:
        marker='o'
        plt.scatter(X, Y,color="#5C5CC4",alpha=1, zorder=i,  marker=marker, s=40)


for i in range(13101,13133):
    X = df[df['cod_comuna']==i][df['year']==2017]['avg_grades_docentes']
    Y = df[df['cod_comuna']==i][df['year']==2017]['avg_grades_alumnos']
    if (i == 13113 or i == 13114 or i == 13115 or i == 13123 or i == 13132):
        marker= dict_markers[i]
        plt.scatter(X, Y, color="#E7478D",alpha=1, zorder=i,  marker=marker, s=80)
    else:
        marker='o'
        plt.scatter(X, Y, color="#E7478D",alpha=1, zorder=i,  marker=marker, s=40)

plt.xlim([2,3])
plt.ylim([5,6.5])
plt.xlabel("Promedio Docentes")
plt.ylabel("Promedio Alumnos")
plt.title("Rendimientos alumnos y evaluación docentes en Santiago (2013 y 2017)")
#plt.legend(['Santiago','Cerrillos','Cerro Navia','Conchali','El Bosque','E. Central','Huechuraba','Indepen.','La Cisterna','La Florida','La Granja','La Pintana','La Reina', 'Las Condes','Lo Barnechea','Lo Espejo','Lo Prado','Macul','Maipú','Ñuñoa','P.A.C','Peñalolen','Providencia','Pudahuel','Quilicura','Q. Normal','Recoleta','Renca', 'San Joaquín','San Miguel','San Ramon','Vitacura']
#            , framealpha=1)

custom_lines = [Line2D([0], [0], marker='P', color='w', label='Scatter',
                       markerfacecolor='gray', markersize=10),
                Line2D([0], [0], marker='^', color='w', label='Scatter',
                       markerfacecolor='gray', markersize=10),
                Line2D([0], [0], marker='D', color='w', label='Scatter',
                       markerfacecolor='gray', markersize=10),
                Line2D([0], [0], marker='s', color='w', label='Scatter',
                       markerfacecolor='gray', markersize=10),
                Line2D([0], [0], marker='X', color='w', label='Scatter',
                       markerfacecolor='gray', markersize=10),
                Line2D([0], [0], marker='o', color='w', label='Scatter',
                    markerfacecolor='gray', markersize=10)]

custom_lines2 = [Line2D([0], [0], marker='o', markerfacecolor="#5C5CC4", color='w', lw=4, markersize=10),
                 Line2D([0], [0], marker='o', markerfacecolor="#E7478D", color='w', lw=4, markersize=10)]

#octagono vitacura
#triangulo barnechea
#diamente las condes
#signo mas la reina
#x providencia


ax = plt.gca()
legend1 = ax.legend(custom_lines, ['La Reina', 'Providencia', 'Las Condes',
                         'Vitacura', 'Lo Barnechea', 'Resto de Santiago'])

legend2 = ax.legend(custom_lines2, ['2013','2017'], loc="center right")

ax.add_artist(legend1)

plt.show()