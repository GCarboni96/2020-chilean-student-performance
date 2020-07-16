import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

df = pd.read_csv(r"D:\\Documentos_U\\2020-1\\Patos\\Proyecto\\chilean-student-performance\\out\\alumnos\\genero\\establecimiento\\results-alumnos-genero-establecimiento.csv", sep=';')
df2 = pd.read_csv(r"D:\\Documentos_U\\2020-1\\Patos\\Proyecto\\chilean-student-performance\\out\\alumnos\\genero\\establecimiento\\results-alumnos-genero-establecimiento.csv", sep=';')

co = [(1, 0.5, 0),
      (0.85, 0.19, 0.23),
      (0.15, 0.23, 0.73),
      (0.13, 0.53, 0.21),
      (0.5, 0.3, 0.9)]

medias = np.zeros(16)
mediasH = np.zeros(16)
agnos = np.zeros(16).astype(int)
for i in range(2003, 2019):
    cat_3 = df[df['year']==i]
    medias[i - 2003] = cat_3['avg_grades_femenino'].mean()
    cat_32 = df2[df2['year']==i]
    mediasH[i - 2003] = cat_32['avg_grades_masculino'].mean()
    agnos[i - 2003] = i

plt.rcParams.update({'font.size': 14})


fig, ax = plt.subplots()

fig.set_facecolor('#F5FBFB')
ax.plot(agnos, medias -mediasH,  linewidth =4, color=([255/255, 109/255, 197/255]))
#ax.set_yticklabels(agnos)
ax.set_xlabel('Años')
ax.set_ylabel('Dif. Promedio Final (femenino - masculino)')
ax.set_title('Diferencia Rendimiento por género (2003-2018)')
plt.xlim([2003,2018])
plt.ylim([0,0.5])

plt.show()