from pathlib import Path
rootfolder = '.'  # example

for f in Path(rootfolder).rglob('Rendimiento por estudiante *.csv'):
  new_name = "performance_{}".format(f.name.split('Rendimiento por estudiante ', 1)[1])
  f.rename(f.with_name(new_name))