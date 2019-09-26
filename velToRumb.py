# 
# m1t9 29-01-2018
# 
# DISTRIBUTION VELOCITY ANGLES TO RUMBS
# INPUT FILE STRUCTURE CONTAIN ONE COLOMN OF ANGLES
# 

import numpy as np
import sys

# INPUT FILE NAME
inputFileName = str(input('Input file name: '))
try:
    inputFile = open(inputFileName, 'r')
except FileNotFoundError:
    sys.exit('\nERROR! file not found')

# FUNCTION OF DISTRIBUTION TO 8 RUMBS
def rumb_filter_8(val):
    return {
                337.50   <= val <   360.00  : 0.00      ,
                0.00     <= val <   22.50   : 0.00      ,
                22.50    <= val <   67.50   : 45.00     ,
                67.50    <= val <   112.50  : 90.00     ,
                112.50   <= val <   157.50  : 135.00    ,
                157.50   <= val <   202.50  : 180.00    ,
                202.50   <= val <   247.50  : 225.00    ,
                247.50   <= val <   292.50  : 270.00    ,
                292.50   <= val <   337.50  : 315.00
} [True]

# FUNCTION OF DISTRIBUTION TO 16 RUMBS
def rumb_filter_16(val):
    return {
                348.75  <= val <    360.00  :   0.00    ,
                0.00    <= val <    11.25   :   0.00    ,
                11.25	<= val <	33.75	:	22.5	,
                22.50	<= val <	45.00   :	33.75	,
                33.75	<= val <	56.25	:	45   	,
                45.00   <= val <	67.50   :	56.25	,
                56.25	<= val <	78.75	:	67.5	,
                67.50	<= val <	90.00   :	78.75	,
                78.75	<= val <	101.25	:	90	    ,
                90.00   <= val <	112.50	:	101.25	,
                101.25	<= val <	123.75	:	112.5	,
                112.50	<= val <	135.00  :	123.75	,
                123.75	<= val <	146.25	:	135     ,
                135.00  <= val <	157.50	:	146.25	,
                146.25	<= val <	168.75	:	157.5	,
                157.5	<= val <	180.00  :	168.75	,
                168.75	<= val <	191.25	:	180	    ,
                180.00  <= val <	202.50	:	191.25	,
                191.25	<= val <	213.75	:	202.5	,
                202.50	<= val <	225.00	:	213.75	,
                213.75	<= val <	236.25	:	225	    ,
                225.00  <= val <	247.50	:	236.25	,
                236.25	<= val <	258.75	:	247.5	,
                247.50	<= val <	270.00  :	258.75	,
                258.75	<= val <	281.25	:	270	    ,
                270.00  <= val <	292.50  :	281.25	,
                281.25	<= val <	303.75	:	292.5	,
                292.50  <= val <	315.00  :	303.75	,
                303.75	<= val <	326.25	:	315	    ,
                315.00  <= val <	337.50  :	326.25	,
                326.25	<= val <	348.75	:	337.5	,
                337.50	<= val <	360.00  :	348.75	,

} [True]

# OUTPUT FILE NAME
outFileName = str(input('Output file name: '))
outFile = open(outFileName, 'w')

# WRITE RESULTS (USE FUNCTION THAT YOU NEED HERE)
for dirctn in inputFile:
    outFile.write(str(rumb_filter_16(float(dirctn))) + '\n')
    