package ReaderWriter;

import java.io.*;
import java.util.ArrayList;

public class CreateValueInputFile {

    protected static ArrayList<String> readMeteoData(String fileName) throws IOException {

        ArrayList<String> data = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

        while (bufferedReader.ready()) {
            data.add(bufferedReader.readLine());
        }

        bufferedReader.close();

        return data;

    }

    protected static void writeMeteoData(String inputFileName, String outputFileName, Delft3dValueFile delft3dValueFile,
                                         int year)
            throws IOException {

        ArrayList<String> data = null;

        try {
            data = readMeteoData(inputFileName);
        } catch (IOException e) {
            System.out.println("Error when try to read input file data");
            e.printStackTrace();
        }

        String startTime = String.valueOf(year) + "-05-01";
        int timeStep = 1; // in hours

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName));

        bufferedWriter.write(
                "FileVersion = " + delft3dValueFile.getVersion() + "\n" +
                        "filetype = " + delft3dValueFile.getFiletype() + "\n" +
                        "NODATA_value = " + delft3dValueFile.getNODATA_value() + "\n" +
                        "n_cols = " + delft3dValueFile.getN_cols() + "\n" +
                        "n_rows = " + delft3dValueFile.getN_rows() + "\n" +
                        "grid_unit = " + delft3dValueFile.getGrid_unit() + "\n" +
                        "x_llcorner = " + delft3dValueFile.getX_llcorner() + "\n" +
                        "y_llcorner = " + delft3dValueFile.getY_llcorner() + "\n" +
                        "dx = " + delft3dValueFile.getDx() + "\n" +
                        "dy = " + delft3dValueFile.getDy() + "\n" +
                        "n_quantity = " + delft3dValueFile.getN_quantity() + "\n" +
                        "quantity1 = " + delft3dValueFile.getQuantity() + "\n" +
                        "unit1 = " + delft3dValueFile.getUnit()  + "\n"
        );

        int mainLoop = delft3dValueFile.getN_rows();
        int hourCount = 0;

        for (String line : data) {

            if (mainLoop == delft3dValueFile.getN_rows()) {
                bufferedWriter.write(String.format("TIME = %s hours since %s 00:00:00 +00:00\n",
                        String.valueOf(hourCount), startTime));
                mainLoop = 0;
                hourCount += timeStep;
            }

            if (delft3dValueFile.getFactor() == 0) {
                bufferedWriter.write(line + "\n");
            } else {
                for (String value : line.split("\\s+")) {
                     Double num = Double.parseDouble(value) * delft3dValueFile.getFactor();
                     bufferedWriter.write(String.valueOf(num) + " ");
                }
                bufferedWriter.write("\n");
            }

            mainLoop++;

        }

        System.out.println(delft3dValueFile.getQuantity() + " " + year + " complete");

        bufferedWriter.close();

    }

    public static void main(String[] args) throws IOException {

    }

}
