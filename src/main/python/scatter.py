import matplotlib.pyplot as plt
import pandas as pd
import numpy as np


# x = regiones (codigo_region)
# y = promedio (avg_grades)

df = pd.read_csv(r"D:\\Documentos_U\\2020-1\\Patos\\Proyecto\\chilean-student-performance\\chilean-student-performance\\out\\docentes\\establecimiento\\results.csv", sep = ';')



#df.plot()  # plots all columns against index
#df.plot(kind='scatter',x='codigo_region',y='avg_grades', ) # scatter plot
#df.plot(kind='density')  # estimate density function

co = [(1, 0.5, 0),
      (0.85, 0.19, 0.23),
      (0.15, 0.23, 0.73),
      (0.13, 0.53, 0.21),
      (0.5, 0.3, 0.9)]


cat_1 = df[df['year']==2016]
print("cat 1: {0}, {1}".format(cat_1['codigo_region'].mean(), cat_1['avg_grades'].mean()))
print("cat 1: {0}, {1}".format(cat_1['codigo_region'].mean()/cat_1['avg_grades'].count(), cat_1['avg_grades'].mean()/cat_1['avg_grades'].count()))
print("cat 1:{0}".format(cat_1['codigo_region'].count()))

cat_2 = df[df['year']==2017]
print("cat 2: {0}, {1}".format(cat_2['codigo_region'].mean(), cat_2['avg_grades'].mean()))
print("cat 2: {0}, {1}".format(cat_2['codigo_region'].mean()/cat_2['avg_grades'].count(), cat_2['avg_grades'].mean()/cat_2['avg_grades'].count()))
print("cat 2:{0}".format(cat_2['codigo_region'].count()))

cat_3 = df[df['year']==2018]
print("cat 3: {0}, {1}".format(cat_3['codigo_region'].mean(), cat_3['avg_grades'].mean()))
print("cat 3: {0}, {1}".format(cat_3['codigo_region'].mean()/cat_3['avg_grades'].count(), cat_3['avg_grades'].mean()/cat_3['avg_grades'].count()))
print("cat 3:{0}".format(cat_3['codigo_region'].count()))



for i in range(2016,2019):
    X = df[df['year']==i]['codigo_region']
    #X = X + np.random.normal(size=X.shape)*0.07
    Y = df[df['year']==i]['avg_grades']
    Y = Y + np.random.normal(size=X.shape)*0.03
    plt.scatter(X, Y, alpha=0.5, label='{0}'.format(i), zorder=i,  marker='s', s=14)

plt.xlim([0,16])
plt.ylim([1,4])
plt.xlabel("Regiones")
plt.ylabel("Promedio")
plt.title("Rendimiento docentes por region")
plt.legend(['2016', '2017', '2018'
            ], framealpha=0.5)


plt.show()