# 
# m1t9 06-09-2017
# 
# AVIABLE FUNCTIONS:
# DISPLAY INFORMATION ABOUT INPUT NETCDF FILE AND AVIABLE DATA
# READ DATA FROM NC FILE USING NETCDF4 PYTHON LIBRARY
# CREATE INPUT FILES OF METEOROLOGICAL (OPTIONALY) DATA FOR DELTARES DELFT3D MODEL IN PROPER FORMAT
# 
# AVIABLE METEO FILES DATA UNITS:
# AIR TEMPERATURE (2 METER UNDER WATER SURFACE) (.AMT)
# RELATIVE HUMIDITY (.ARN)
# CLOUDNESS (.AMC)
# U-DIRECTION WIND (2 METER UNDER WATER SURFACE) (.AMU)
# V-DIRECTION WIND (2 METER UNDER WATER SURFACE) (.AMV)
# AIR PRESSURE (.AMP)
# 
# NETCDF4 WAS DOWNLOADED FROM ECMWF ERA REANALYS DATABASE
# http://apps.ecmwf.int/datasets/data/interim-full-daily/levtype=sfc/
# 

import sys
import numpy as np
from netCDF4 import Dataset

print('\nstart read .nc file\n')

# INPUT NETCDF4 FILE NAME
nc_fileID = str(input('Input netcdf file name: '))

# VERIFY THAT FILE EXISTS
try:
    file_chk = open(nc_fileID)
except FileNotFoundError:
    sys.exit('\nERROR! file not found')

# READ NETCDF FILE AND PRINT AVIABLE VARIABLES
root = Dataset(nc_fileID)
print(root)
dims = root.dimensions
ndims = len(dims)

dic = {}
dic2 = {}
print('\navailable variables in selected .NC file:\n')
vars = root.variables
print(vars)
nvars = len(vars)
n = 0
for var in vars:
    # sys.stdout.write('-'+var+' ')
    print('#',n,'   ',var, vars[var].shape)
    dic[str(var)] = n
    l = vars[var].shape
    dic2[str(var)] = len(l)
    n += 1
print('\n')

# INPUT VARIABLES THAT YOU WANT TO READ
nc_data = []
iter_var = []
iter_var.append(input('write the variables you want to read (through the gap): ').split(' '))
iter_var = iter_var[0]
try:
    for v in iter_var:
        dic[v]
except KeyError:
    sys.exit('\nvariable name error\n')

# WARNING!
# MAX DIMENSIONS OF MASSIVE: 3

var_inter_n = {}
ni = 0
for i in iter_var:
    if (dic2[i] == 1):
        nc_data.append(np.array(root.variables[i][:], dtype=np.float32))
    if (dic2[i] == 2):
        nc_data.append(np.array(root.variables[i][:,:], dtype=np.float32))
    if (dic2[i] == 3):
        nc_data.append(np.array(root.variables[i][:,:,:], dtype=np.float32))
    var_inter_n[i] = ni
    ni += 1

print('\nread complete\n')

# WRITE GRID OF INPUT WIND NETCDF DIMENSION (REGULAR GRID WITH FIXED STEP IN SPACE\TIME)
# OUTPUT FILE CONTAIN TWO COLOMNS WITH LONGITUDE AND LATITUDE COORDINATES
chk = input('write wind grid? y/n ')
if (chk != 'n'):
    outfile2_name = 'uvsp_grd.dat'
    outfile2 = open(outfile2_name, 'w')
    for i in range((len(root.variables['longitude']))):
        for j in range(len(root.variables['latitude'])):
            outfile2.write(str(root.variables['longitude'][i])+'    '+str(root.variables['latitude'][j])+'\n')
    print('wind grid write complete')

# WRITE CHECK
check = input('start create meteo files? y/n ')
if (check == 'n'):
    sys.exit()

# USING CONST LIST
nodata_value = -999.000
grid_unit = 'degree' #  m or degree
longitude_name = 'longitude'
latitude_name = 'latitude'
time_name = 'time'
n_quantity = 1
fmt = '.dat'

for i in iter_var:

    # LIST OF AVIABLE OUTPUT DATA FOR DELFT3D METEO INPUT FILES
    if (i == 'u10'): fmt = '.amu'
    if (i == 'v10'): fmt = '.amv'
    if (i == 'sp'): fmt = '.amp'
    if (i == 't2m'): fmt = '.amt'
    if (i == 'mcc'): fmt = '.amc'
    if (i == 'rh'): fmt == '.amr'

    # FOR SINGLE MONTH CHOOSE ONE:
    mnth_chck = False 
    # mnth_chck = True
    if mnth_chck == True:
        month = str(input('Using month: '))
        if (month == 'jan'):
            monthtime = '01'
        if (month == 'feb'):
            monthtime = '02'
        if (month == 'mar'):
            monthtime = '03'
        if (month == 'apr'):
            monthtime = '04'
        if (month == 'may'):
            monthtime = '05'
        if (month == 'jun'):
            monthtime = '06'
        if (month == 'jul'):
            monthtime = '07'
        if (month == 'aug'):
            monthtime = '08'
        if (month == 'sep'):
            monthtime = '09'
        if (month == 'oct'):
            monthtime = '10'
        if (month == 'nov'):
            monthtime = '11'
        if (month == 'dec'):
            monthtime = '12' 

    outfile_name = str(i)+'_'+month+fmt
    outfile = open(outfile_name, 'w')
    outfile.write('FileVersion = 1.03\n')
    outfile.write('filetype = meteo_on_equidistant_grid\n')
    outfile.write('NODATA_value = '+str(nodata_value)+'\n')
    n_cols = vars[i].shape[2]
    outfile.write('n_cols = '+str(n_cols)+'\n')
    n_rows = vars[i].shape[1]
    outfile.write('n_rows = '+str(n_rows)+'\n')
    outfile.write('grid_unit = '+str(grid_unit)+'\n')
    x_llcorner = root.variables[longitude_name][0]
    y_llcorner = root.variables[latitude_name][-1]
    outfile.write('x_llcorner = '+str(x_llcorner)+'\n')
    outfile.write('y_llcorner = '+str(y_llcorner)+'\n')
    dy = (root.variables[longitude_name][-1] - root.variables[longitude_name][0]) / (n_cols - 1)
    dx = (root.variables[latitude_name][0] - root.variables[latitude_name][-1]) / (n_rows - 1)
    outfile.write('dx = '+str(dx)+'\n')
    outfile.write('dy = '+str(dy)+'\n')
    outfile.write('n_quantity = '+str(n_quantity)+'\n')
    quantity1 = '???'
    unit1 = '???'
    if (i == 'u10'):
        quantity1 = 'x_wind'
        unit1 = 'm s-1'
    elif (i == 'v10'):
        quantity1 = 'y_wind'
        unit1 = 'm s-1'
    elif (i == 'sp'):
        quantity1 = 'air_pressure'
        unit1 = 'Pa'
    if (i == 't2m'):
        quantity1 = 'air_temperature'
        unit1 = 'Celsius'
    if (i == 'mcc'):
        quantity1 = 'cloudiness'
        unit1 = '%'
    outfile.write('quantity1 = '+quantity1+'\n')
    outfile.write('unit1 = '+unit1+'\n')
    time1 = 0

    # WRITE DATA IN FILE
    for t in range(int(input('time to write '+i+'(time size ' + str(vars['time'].shape) + '): '))):
        # time1 = root.variables[time_name][t]
        outfile.write('TIME = ' + str(root.variables[time_name][t]) + ' hours since 1900-01-01 00:00:00 +00:00\n')
        # FOR SINGLE MONTH WRITE USE THIS:
        # outfile.write('TIME = ' + str(time1) + ' hours since 2016-' + monthtime + '-01 00:00:00 +00:00\n')
        for n in range(int(vars[i].shape[1])):
            for m in range(int(vars[i].shape[2])):
                if i == 't2m':
                    outfile.write(str(nc_data[var_inter_n[i]][t, n, m] - 273.150)+' ')
                else:
                    outfile.write(str(nc_data[var_inter_n[i]][t, n, m])+' ')
            outfile.write('\n')
        time1 += 6
    print('done')

# THERE IS NO RELETIVE HUMIDITY ON ECMWF ERA REANALYS DATABASE SO LOWER YOU CAN WRITE
# IT IN FILE FOR DELTARES DELFT3D FLOW WITH CONSTATN VALUE (STAT_RH)
# FORMAT OF OUTPUT FILE FOR THE SIMILAR PREVIOUS
chk = input('Print relative humidity? y/n ')
if (chk == 'n'):
    sys.exit()

stat_rh = 70.0 #  RELATIVE HUMIDITY CONST VALUE
itr = iter_var[0]
iter_var = []
iter_var.append(itr)

for i in iter_var:
    fmt = '.amr'
    
    # FOR SINGLE MONTH CHOOSE ONE:
    mnth_chck == False
    # mnth_chck == True
    if mnth_chck == True:
        month = str(input('Using month: '))
        if (month == 'jan'):
            monthtime = '01'
        if (month == 'feb'):
            monthtime = '02'
        if (month == 'mar'):
            monthtime = '03'
        if (month == 'apr'):
            monthtime = '04'
        if (month == 'may'):
            monthtime = '05'
        if (month == 'jun'):
            monthtime = '06'
        if (month == 'jul'):
            monthtime = '07'
        if (month == 'aug'):
            monthtime = '08'
        if (month == 'sep'):
            monthtime = '09'
        if (month == 'oct'):
            monthtime = '10'
        if (month == 'nov'):
            monthtime = '11'
        if (month == 'dec'):
            monthtime = '12' 

    outfile_name = 'rh_'+month+fmt
    outfile = open(outfile_name, 'w')
    outfile.write('FileVersion = 1.03\n')
    outfile.write('filetype = meteo_on_equidistant_grid\n')
    outfile.write('NODATA_value = '+str(nodata_value)+'\n')
    n_cols = vars[i].shape[2]
    outfile.write('n_cols = '+str(n_cols)+'\n')
    n_rows = vars[i].shape[1]
    outfile.write('n_rows = '+str(n_rows)+'\n')
    outfile.write('grid_unit = '+str(grid_unit)+'\n')
    x_llcorner = root.variables[longitude_name][0]
    y_llcorner = root.variables[latitude_name][-1]
    outfile.write('x_llcorner = '+str(x_llcorner)+'\n')
    outfile.write('y_llcorner = '+str(y_llcorner)+'\n')
    dy = (root.variables[longitude_name][-1] - root.variables[longitude_name][0]) / (n_rows - 1)
    dx = (root.variables[latitude_name][0] - root.variables[latitude_name][-1]) / (n_cols - 1)
    outfile.write('dx = '+str(dx)+'\n')
    outfile.write('dy = '+str(dy)+'\n')
    outfile.write('n_quantity = '+str(n_quantity)+'\n')
    outfile.write('quantity1 = relative_humidity'+'\n')
    outfile.write('unit1 = %'+'\n')
    time1 = 0

    # WRITE DATA IN FILE
    for t in range(int(input('time to write : '))):
        # time1 = root.variables[time_name][t]
        outfile.write('TIME = ' + str(root.variables[time_name][t]) + ' hours since 1900-01-01 00:00:00 +00:00\n')
        # FOR SINGLE MONTH WRITE USE THIS:
        # outfile.write('TIME = ' + str(time1) + ' hours since 2016-' + monthtime + '-01 00:00:00 +00:00\n')
        for m in range(int(vars[i].shape[2])):
            for n in range(int(vars[i].shape[1])):
                outfile.write(str(stat_rh)+' ')
            outfile.write('\n')
        time1 += 6
    print('Done.')
    