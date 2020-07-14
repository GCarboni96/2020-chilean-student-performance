import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import matplotlib.cm as cm
import random


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


color=(random.random(), random.random(), random.random(), 1.0)

colormap = np.array(['r', 'g', 'b'])

colors = cm.rainbow(np.linspace(0, 1, 15))

for i in range(13101,13133):
    X = df[df['cod_comuna']==i][df['year']==2013]['avg_grades_docentes']

    #X = X + np.random.normal(size=X.shape)*0.07
    Y = df[df['cod_comuna']==i][df['year']==2013]['avg_grades_alumnos']
    #Y = Y + np.random.normal(size=X.shape)*0.03
    if (i == 13113 or i == 13114 or i == 13115 or i == 13123 or i == 13132):
        marker='*'
    else:
        marker='.'
    plt.scatter(X, Y,color="b",alpha=0.5, label=df['nomb_comuna'], zorder=i,  marker=marker, s=80)


for i in range(13101,13133):
    X = df[df['cod_comuna']==i][df['year']==2017]['avg_grades_docentes']

    #X = X + np.random.normal(size=X.shape)*0.07
    Y = df[df['cod_comuna']==i][df['year']==2017]['avg_grades_alumnos']
    #Y = Y + np.random.normal(size=X.shape)*0.03
    if (i == 13113 or i == 13114 or i == 13115 or i == 13123 or i == 13132):
        marker='*'
    else:
        marker='.'
    plt.scatter(X, Y,color = "r",alpha=0.5, label=df['nomb_comuna'], zorder=i,  marker=marker, s=80)




plt.xlim([2,3])
plt.ylim([5,6.5])
plt.xlabel("Promedio Docentes")
plt.ylabel("Promedio Alumnos")
plt.title("Rendimientos alumnos y docentes por comunas de santiago (2017)")
plt.legend(['Santiago','Cerrillos','Cerro Navia','Conchali','El Bosque','E. Central','Huechuraba','Indepen.','La Cisterna','La Florida','La Granja','La Pintana','La Reina', 'Las Condes','Lo Barnechea','Lo Espejo','Lo Prado','Macul','Maipú','Ñuñoa','P.A.C','Peñalolen','Providencia','Pudahuel','Quilicura','Q. Normal','Recoleta','Renca', 'San Joaquín','San Miguel','San Ramon','Vitacura']
            , framealpha=1)



plt.show()