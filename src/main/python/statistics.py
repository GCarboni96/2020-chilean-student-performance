import matplotlib.pyplot as plt
import pandas as pd
import glob
import seaborn as sns
import os
sns.set(style='ticks')

path = os.path.abspath(os.getcwd())
depe_path = glob.glob(path+"/out/stats/*.csv")[0]

df = pd.read_csv("./out/stats/*.csv")
print(df)