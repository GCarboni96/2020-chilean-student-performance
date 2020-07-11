import matplotlib.pyplot as plt
import pandas as pd
import glob
import seaborn as sns

sns.set(style='ticks')

depe_path = glob.glob("../../../out/reprobation/depe/*.csv")[0]
rural_path = glob.glob("../../../out/reprobation/rural/*.csv")[0]

df = pd.read_csv(depe_path, sep=';')
df['cod_depe'] = df['cod_depe'].astype('category')
print(df.dtypes)
ax = sns.lineplot(x="agno", y="depe_reprobation", hue="cod_depe", data=df)
dependencies = ["Municipal", 
                "Particular Subvencionado", 
                "Particular Pagado (o no subvencionado)", 
                "Corporación de Administración Delegada", 
                "Servicio Local de Educación"]
plt.show()

plt.figure()
df = pd.read_csv(rural_path, sep=';')
ax = sns.lineplot(x="agno", y="rural_reprobation", hue="rural_rbd", data=df)
plt.show()