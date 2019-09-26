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

    protected static void writeMeteoData(String inputFileName, String outputFileName, Delft3dValueFile delft3dValueFile)
            throws IOException {

        ArrayList<String> data = null;

        try {
            data = readMeteoData(inputFileName);
        } catch (IOException e) {
            System.out.println("Error when try to read input file data");
            e.printStackTrace();
        }

        String startTime = "1988-05-1";
        String timeStep = "1"; // in hours

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
                hourCount += 6;
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

        System.out.println(delft3dValueFile.getQuantity() + " complete");

        bufferedWriter.close();

    }



    public static void main(String[] args) throws IOException {

        Delft3dValueFile delft3dValueFile =
                new Delft3dValueFile("meteo_on_equidistant_grid", 265, 49,
                        "degree", 27.0, 77.5, -0.25, 0.25, 1,
                        "x_wind", "m s-1");

        writeMeteoData("D:\\d3d_workdir\\1009_PROJECT\\Данные реанализа ERA5 (Чанцеву_ВЮ)\\U_1979-2018\\1988_U.txt",
                "D:\\d3d_workdir\\30yearsProject\\METEO\\1988_d3d.amu",
                delft3dValueFile);

        delft3dValueFile.setQuantity("y_wind");

        writeMeteoData("D:\\d3d_workdir\\1009_PROJECT\\Данные реанализа ERA5 (Чанцеву_ВЮ)\\V_1979-2018\\1988_V.txt",
                "D:\\d3d_workdir\\30yearsProject\\METEO\\1988_d3d.amv",
                delft3dValueFile);

        delft3dValueFile.setQuantity("air_pressure");
        delft3dValueFile.setUnit("Pa");
        delft3dValueFile.setFactor(1000);

        writeMeteoData("D:\\d3d_workdir\\1009_PROJECT\\Данные реанализа ERA5 (Чанцеву_ВЮ)\\MSL_1979-2018\\1988_MSL.txt",
                "D:\\d3d_workdir\\30yearsProject\\METEO\\1988_MSL.amp",
                delft3dValueFile);


    }

}
