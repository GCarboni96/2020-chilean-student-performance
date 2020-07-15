import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

df = pd.read_csv(r"E:\\Documentos\\Informática\Maestria - 02 Segundo Semestre\\cc5212 - Procesamiento Masivo de Datos\\Proyecto\\out\\averageGenderFem\\PerformanceYearlyAverageByGenderFem.csv", sep=';')
df2 = pd.read_csv(r"E:\\Documentos\\Informática\Maestria - 02 Segundo Semestre\\cc5212 - Procesamiento Masivo de Datos\\Proyecto\\out\\averageGenderMasc\\PerformanceYearlyAverageByGenderMasc.csv", sep=';')

co = [(1, 0.5, 0),
      (0.85, 0.19, 0.23),
      (0.15, 0.23, 0.73),
      (0.13, 0.53, 0.21),
      (0.5, 0.3, 0.9)]

medias = np.zeros(16)
mediasH = np.zeros(16)
agnos = np.zeros(16)
for i in range(2003, 2019):
    cat_3 = df[df['year']==i]
    medias[i - 2003] = cat_3['avg_grades_femenino'].mean()
    cat_32 = df2[df2['year']==i]
    mediasH[i - 2003] = cat_32['avg_grades_masculino'].mean()
    agnos[i - 2003] = i
'''
bar_width = 0.35
fig, ax = plt.subplots()
ax.barh(agnos, medias, bar_width, label="Femenino", color=([255/255, 109/255, 197/255]))
ax.barh(agnos + bar_width, mediasH, bar_width, label="Masculino", color=([84/255, 88/255, 255/255]))

ax.set_xlabel('Años')
ax.set_ylabel('Promedio final alumnos')
ax.set_title('Rendimientos alumnos por genero (2003-2018)')
ax.set_xticks(agnos + bar_width / 2)
ax.set_xticklabels(agnos)
ax.legend()
#plt.xlim([2002,2019])
#plt.ylim([2,7])

plt.show()
'''
fig, ax = plt.subplots()
bar_width = 0.35
ax.barh(agnos, medias, bar_width, label="Femenino", color=([255/255, 109/255, 197/255]))
ax.barh(agnos + bar_width, mediasH, bar_width, label="Masculino", color=([84/255, 88/255, 255/255]))
ax.set_yticks(agnos + bar_width / 2)
ax.set_yticklabels(agnos)
ax.invert_yaxis()
ax.set_xlabel('Promedio final alumnos')
ax.set_ylabel('Años')
ax.set_title('Rendimientos alumnos por genero (2003-2018)')
ax.legend()
plt.xlim([2,7])
plt.ylim([2002,2019])

plt.show()