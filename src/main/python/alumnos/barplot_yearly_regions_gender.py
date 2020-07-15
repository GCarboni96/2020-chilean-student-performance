import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

dfF = pd.read_csv(r"E:\\Documentos\\Informática\Maestria - 02 Segundo Semestre\\cc5212 - Procesamiento Masivo de Datos\\Proyecto\\out\\averageRegionGenderFem\\PerformanceYearlyAverageRegionByGenderFem.csv", sep=';')
dfM = pd.read_csv(r"E:\\Documentos\\Informática\Maestria - 02 Segundo Semestre\\cc5212 - Procesamiento Masivo de Datos\\Proyecto\\out\\averageRegionGenderMasc\\PerformanceYearlyAverageRegionByGenderMasc.csv", sep=';')

dfF13 = dfF[dfF['year']==2013]
dfF18 = dfF[dfF['year']==2018]
dfM13 = dfM[dfM['year']==2013]
dfM18 = dfM[dfM['year']==2018]

for i in range(15):
    tmp = dfF13[dfF13['codigo_region']]

X = dfF[dfF['year']==2013]['avg_grades_femenino']
Y = dfF[dfF['year']==2013]['codigo_region']
Y = Y / 1000
plt.scatter(X, Y, alpha=0.5, label='2013', zorder=2013,  marker='s', s=14)
X2 = dfF[dfF['year']==2018]['avg_grades_femenino']
Y2 = dfF[dfF['year']==2018]['codigo_region']
Y2 = Y2 / 1000
plt.scatter(X2, Y2, alpha=0.5, label='2018', zorder=2018,  marker='s', s=14)

#plt.xlim([1,7])
#plt.ylim([1,100])
#plt.xlabel("Promedio Final Alumnos")
#plt.ylabel("Asistencia")
#plt.title("Rendimientos alumnos y su asistencia por establecimiento (2018)")
#plt.legend(['I','II','III','IV','V','VI','VII','VIII','IX','X','XI','XIII','RM', 'XIV','XV'], framealpha=1)
plt.legend(['2013','2018'], framealpha=1)

plt.show()