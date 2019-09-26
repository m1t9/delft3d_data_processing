# 
# m1t9 23-10-2017
# 
# PRINT COORDINATES OF GRID POINTS GENERATED FROM DELTARES RGRFGRID TO DATA FILE THAT
# CONTAIN TWO COLOMNS OF LATITUDE AND LONGITUDE
# 
# INPUT: GRID FILE (.GRD) CREATED BY USING DELTARES RGRFGRID
# SEE MORE INFORMATION IN DELFT3D DOCUMENTATION OR .GRD SAMPLE
# 

import numpy as np

# INPUT GRID FILENAME
fname = input('input file name -> ')
# fname = 'Grid_001.grd'

# READ .GRD FILE AND PARAMETERS
grd_s = []
file = open(fname)
for line in file:
    grd_s.append(line)
n = 0
crd_inf = grd_s[6].split()
lng = int(crd_inf[0])
lt = int(crd_inf[1])
head_L = 8
for i in range(head_L):
    del grd_s[0]
m = lng//5
if (lng%5 != 0):
    m += 1

# CREATE WORK ARRAYS
x = []
x_app = []
y = []
y_app = []
stps_get = m*lt

# TRANSFORMATION READED COORDINATES
for i in range(stps_get):
    if (i%m == 0):
        cx = grd_s[i].split()
        cy = grd_s[i + stps_get].split()
        for j in range(len(cx)):
            x_app.append(cx[j])
        for j in range(len(cy)):
            y_app.append(cy[j])
        for k in range(2):
            del x_app[0]
            del y_app[0]
        for j in range(len(x_app)):
            x.append(x_app[j])
        for j in range(len(y_app)):
            y.append(y_app[j])
        x_app.clear()
        y_app.clear()
    else:
        cx = grd_s[i].split()
        cy = grd_s[i + stps_get].split()
        for j in range(len(cx)):
            x_app.append(cx[j])
        for j in range(len(cy)):
            y_app.append(cy[j])
        for j in range(len(x_app)):
            x.append(x_app[j])
        for j in range(len(y_app)):
            y.append(y_app[j])
        x_app.clear()
        y_app.clear()
x_crd = np.zeros(len(x))
y_crd = np.zeros(len(y))
for i in range(len(x)):
    x_crd[i] = float(x[i])
for i in range(len(y)):
    y_crd[i] = float(y[i])
# fgrid_out_name = 'grd_out1.dat'
fgrid_out_name = str(input('Out file name: '))
fgrid_out = open(fgrid_out_name, 'w')

# WRITE IN FILE
for i in range(len(x_crd)):
    fgrid_out.write(str(x_crd[i]) + ' ' + str(y_crd[i]) + '\n')
print('complete')
