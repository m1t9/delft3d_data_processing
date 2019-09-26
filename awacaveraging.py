# 
# m1t9 12-02-2018
# 
# AWAC DATA AVERAGING
# 
# STRUCTURE OF INPUT FILE:
# 1ST LINE CONTAIN INFORMATION ABOUT POINT COORDINATES AND DEPTH
# 2ND LINE CONTAIN UNIT, TIME ZONE, HORIZONT
# 3RD LINE CONTAIN HEADER OF DATA COLUMNS (DD MM YY  HH MM MODUL DIR U V)
# 
# WARNING! INPUT FILE MUST BE THE SAME LENGTH
# 

import sys

# CHECK INI FILE
# INI FILE CONTAIN LIST OF INPUT FILE NAMES
# YOU CAN CHANGE INI FILE NAME HERE
iniFileName = 'namelist.ini'
iniChck = True
try:
	iniFile = open(iniFileName, 'r')
except FileNotFoundError:
	iniChck = False

# CREATE NAME LIST IF INI FILE EXIST
if iniChck	== True:
	namelist = []
	for line in iniFile:
		namelist.append(str(line.split()[0]))

# CREATE NAME LIST IF INI FILE DOES NOT EXIST
if iniChck == False:
	namelist = []
	inputFileNames = str(input('Input file names (throw the gap): '))
	namelist.append(inputFileNames.split())
	namelist = namelist[0]
	
# GET U COMPONENT FROME ONE FILE
def getVarU(fileName):
	getU = []
	file = open(fileName)
	for i in range(3): file.readline() #  read file head
	for line in file:
		getU.append(float(line.split()[len(line.split()) - 2]))
	return getU

# GET V COMPONENT FROME ONE FILE
def getVarV(fileName):
	getV = []
	file = open(fileName)
	for i in range(3): file.readline() #  read file head
	for line in file:
		getV.append(float(line.split()[len(line.split()) - 1]))
	return getV

datU = []
datV = []

# SUMMATOR
for name in namelist:
	try:
		file = open(name, 'r')
	except FileNotFoundError:
		sys.exit('\nERROR! file not found')
	varsU = getVarU(name)
	varsV = getVarV(name)
	if len(datU) == 0:
		for var in (varsU): datU.append(var)
		for var in (varsV): datV.append(var)
	else:
		for i in range(len(varsU)): datU[i] += varsU[i]
		for i in range(len(varsV)): datV[i] += varsV[i]

# AVERAGING
for i in range(len(datU)): datU[i] = datU[i]/len(namelist)
for i in range(len(datV)): datV[i] = datV[i]/len(namelist)

# PRINT RESULTS
outFileName = 'out.dat'
outFile = open(outFileName, 'w')
for i in range(len(datU)):
	outFile.write(str(float(datU[i])) + '    ' + str(float(datV[i])) + '\n')