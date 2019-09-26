# 
# m1t9 10-02-2018
# 
# TRANSLATE XY COMPONENTS TO MAGNITUDE AND ANGLE
# 
# INPUT FILE STRUCTURE:
# 1ST COLOMN X COMPONENT, 2ND COLOMN Y COMPONENT
# 

import math

# INPUT/OUTPUT FILE NAMES
fileNameInput = str(input('Input file name: '))
fileInput = open(fileNameInput, 'r')
fileNameOut = str(input('Output file name: '))
try:
    fileOutput = open(fileNameOut, 'w')
except FileNotFoundError:
    sys.exit('\nERROR! file not found')

fileOutput.write('MAGNITUDE            ANGLE\n')

# COMPUTATION BLOCK
for line in fileInput:
	Xcomp = float(line.split()[0])
	Ycomp = float(line.split()[1])

	# COMPUTATION MAGNITUDE
	mag = math.sqrt(abs(Xcomp)**2 + abs(Ycomp)**2)

	# COMPUTATION DIRECTION ANGLE
	if (Xcomp > 0):
		if (Ycomp > 0):
			# 1 SECTOR
			ang = math.acos(Xcomp/mag) * 180/math.pi
		else:
			# 2 SECTOR
			ang = 90.0 + math.acos(Xcomp/mag) * 180/math.pi
	else:
		if (Ycomp < 0):
			# 3 SECTOR
			ang = 270.0 - math.acos(abs(Xcomp)/mag) * 180/math.pi
		else:
			# 4 SECTOR
			ang = 270.0 + math.acos(abs(Xcomp)/mag) * 180/math.pi
	
	# OUTPUT RESULTS
	fileOutput.write(str(mag) + '   ' + str(ang) + '\n')
