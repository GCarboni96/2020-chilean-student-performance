import matplotlib.pyplot as plt
import matplotlib.ticker as mtick
import pandas as pd
import glob
import seaborn as sns

sns.set(style='ticks')

depe_path = glob.glob("../../../out/reprobation/depe/*.csv")[0]
rural_path = glob.glob("../../../out/reprobation/rural/*.csv")[0]

# Dependencias
df = pd.read_csv(depe_path, sep=';')
df['cod_depe'] = df['cod_depe'].astype('category')

color = ["#5C5CC4", "#E7478D", "#90C4FE", "#1BBC9D", "#FFBB4C"]

fig = plt.figure(figsize=(10, 7))
fig.set_facecolor('#F5FBFB')

ax = sns.lineplot(x="agno", y="depe_reprobation", palette=color, hue="cod_depe", data=df, linewidth=2)
ax.set_facecolor('#F5FBFB')
ax.yaxis.set_major_formatter(mtick.PercentFormatter(1.0))

handles, labels = ax.get_legend_handles_labels()
L = ax.legend(handles=handles[1:], labels=labels[1:])

plt.axvline(2011, color='gray', linestyle='--')
plt.text(2012.8, 0.005, 'Movilizaciones del 2011',
         ha='center', va='center',rotation='horizontal', color='gray')

for line in L.get_lines():
    line.set_linewidth(3.0)

legends = ["Municipal",
           "Particular\nSubvencionado",
           "Particular\nPagado",
           "Corporación de\nAdministración\nDelegada",
           "Servicio Local\nde Educación"]

for i in range(5):
    L.get_texts()[i].set_text(legends[i])

box = ax.get_position()
plt.xticks(range(2002, 2020, 2))
plt.xlim([2002, 2019])
plt.xlabel("Año")
plt.ylabel("Tasa de reprobación")
plt.title("Reprobación anual según dependencia")

# Rural
fig = plt.figure(figsize=(10,7))
fig.set_facecolor('#F5FBFB')
df = pd.read_csv(rural_path, sep=';')

color = ["#5C5CC4", "#E7478D"]

ax = sns.lineplot(x="agno", y="rural_reprobation", hue="rural_rbd", data=df, palette=color, linewidth=2)
ax.set_facecolor('#F5FBFB')
ax.yaxis.set_major_formatter(mtick.PercentFormatter(1.0))
handles, labels = ax.get_legend_handles_labels()
L = ax.legend(handles=handles[1:], labels=labels[1:])

for line in L.get_lines():
    line.set_linewidth(3.0)

legends = ["Urbano", "Rural"]
for i in range(2):
    L.get_texts()[i].set_text(legends[i])

plt.axvline(2011, color='gray', linestyle='--')
plt.text(2012.8, 0.0246, 'Movilizaciones del 2011',
         ha='center', va='center',rotation='horizontal', color='gray')

plt.xticks(range(2002, 2020, 2))
plt.xlim([2002, 2019])
plt.xlabel("Año")
plt.ylabel("Tasa de reprobación")
plt.title("Reprobación anual según tipo de localidad")
plt.show()
