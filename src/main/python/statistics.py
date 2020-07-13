import matplotlib.pyplot as plt
import matplotlib.ticker as mtick
import pandas as pd
import glob
import seaborn as sns
import os
sns.set(style='ticks')

fig = plt.figure(figsize=(10, 7))
fig.set_facecolor('#F5FBFB')

path = glob.glob("../../../out/stats/performance/*.csv")[0]
df = pd.read_csv(path, sep=';')

ax = sns.barplot(x="agno", y="proportion", data=df, palette=["#90C4FE"])
ax.set_facecolor('#F5FBFB')
plt.xlabel("Año")
plt.ylabel("Distribución")
plt.title("Distribución de registros por año Rendimiento Académico")
ax.yaxis.set_major_formatter(mtick.PercentFormatter(1.0))

fig = plt.figure(figsize=(10, 7))
fig.set_facecolor('#F5FBFB')

path = glob.glob("../../../out/stats/evaluation/*.csv")[0]
df = pd.read_csv(path, sep=';')

ax = sns.barplot(x="año_eval", y="proportion", data=df, palette=["#90C4FE"])
ax.set_facecolor('#F5FBFB')
plt.xlabel("Año")
plt.ylabel("Distribución")
plt.title("Distribución de registros por año Evaluación Docente")
ax.yaxis.set_major_formatter(mtick.PercentFormatter(1.0))

plt.show()