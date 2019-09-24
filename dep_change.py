# 
# m1t9 12-02-2018
# 
# REPLACEMENT OF DEPTHS BELOW SET VALUSE
# INPUT: FILE OF DEPTH (.DEP) FROM DELTARES QUICKIN
# 

openFileName = str(input('Input file name: '))
openFile = open(openFileName, 'r')

writeFileName = str(input('Output file name: '))
writeFile = open(writeFileName, 'w')

# SET VALUE
# dapval = float(input('Set minimum value of deph: '))

# REPLACEMENT OF VALUES
for line in openFile:
    line = line.split()
    for i in range(len(line)):
        # if (0.0 <= float(line[i]) <= 1.0):
        #     line[i] = '1.0000000E+00' #  minimum value of deph
        if (float(line[i]) < 0.1):
            line[i] = '-9.9900000E+02' #  ex. val

    # WRITE RESULTS IN FILE
    for i in range(len(line)):
        if i == 0:
            if (line[i][0] == '-'):
                writeFile.write('  ')
            else:
                writeFile.write('   ')
        if (i != len(line)-1):
            if (line[i+1][0] != '-'):
                writeFile.write(str(line[i]) + '   ')
            else:
                writeFile.write(str(line[i]) + '  ')
        else:
            writeFile.write(str(line[i]) + '\n')
