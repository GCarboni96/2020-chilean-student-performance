import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import glob

path = glob.glob("../../../../out/alumnos/establecimiento/*.csv")[0]
df = pd.read_csv(path, sep=';')

# df.plot()  # plots all columns against index
# df.plot(kind='scatter',x='codigo_region',y='avg_grades', ) # scatter plot
# df.plot(kind='density')  # estimate density function

co = [(1, 0.5, 0),
      (0.85, 0.19, 0.23),
      (0.15, 0.23, 0.73),
      (0.13, 0.53, 0.21),
      (0.5, 0.3, 0.9)]

cat_1 = df[df['year'] == 2013]
cat_2 = df[df['year'] == 2014]
cat_3 = df[df['year'] == 2015]
cat_4 = df[df['year'] == 2016]
cat_5 = df[df['year'] == 2017]
cat_6 = df[df['year'] == 2018]

fig = plt.figure(figsize=(10, 7))
fig.set_facecolor('#F5FBFB')

for i in range(2018, 2019):
    X = df[df['year'] == i]['avg_grades']
    Y = df[df['year'] == i]['avg_assistance']
    plt.scatter(X, Y, alpha=0.4, label='{0}'.format(i), zorder=i, s=40, color="#E7478D")

ax = plt.gca()
ax.set_facecolor('#F5FBFB')
plt.xlim([3, 7])
plt.ylim([1, 105])
plt.xlabel("Promedio Final Alumnos")

plt.ylabel("Asistencia promedio")
plt.title("Rendimientos alumnos y su asistencia por establecimiento 2018")

plt.show()
