package ReaderWriter;

import java.io.*;
import java.util.ArrayList;

public class BlnRewrite {

    static ArrayList<String[][]> data;

    protected static ArrayList<String[][]> readData(String fileName) throws IOException {

        data = new ArrayList<>();

        int additionalFormatNumber = 0;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

        String line = bufferedReader.readLine().replaceAll(",", " ");

        if (line.split("\\s+")[0].equals("")) {
            additionalFormatNumber = 1;
        }
        int countBlock = Integer.parseInt(line.split("\\s+")[0 + additionalFormatNumber]);

        String[][] block = new String[countBlock][2];
        int count = 0;

        while (bufferedReader.ready()) {

            if (count == countBlock) {
                data.add(block);
                line = bufferedReader.readLine().replaceAll(",", " ");
                if (line.split("\\s+")[0].equals("")) {
                    additionalFormatNumber = 1;
                }
                countBlock = Integer.parseInt(line.split("\\s+")[0 + additionalFormatNumber]);
                block = new String[countBlock][2];
                count = 0;
            }

            line = bufferedReader.readLine().replaceAll(",", " ");
            if (line.split("\\s+")[0].equals("")) {
                additionalFormatNumber = 1;
            } else {
                additionalFormatNumber = 0;
            }

            block[count][0] = line.split("\\s+")[0 + additionalFormatNumber];
            block[count][1] = line.split("\\s+")[1 + additionalFormatNumber];
            count++;

        }
        data.add(block);

        return data;
    }


    public static void main(String[] args) throws IOException {

        String folderPath = "";
        File folder = new File(folderPath);

        File[] listOfFiles = folder.listFiles();

        ArrayList<String[][]> dataBlock;

        for (File file : listOfFiles) {

            String outputFileName = file.getName().replace(".bln", "");

            if (!file.getName().contains(".bln")) {
                continue;
            }
            System.out.println(file.getName());
            dataBlock = readData(folderPath + "\\" + file.getName());

            int count = 1;
            String folderWritePath = "";

            BufferedWriter bufferedWriter;

            String writeFileName = folderWritePath + outputFileName + ".ldb";
            bufferedWriter = new BufferedWriter(new FileWriter(writeFileName));

            bufferedWriter.write("*\n" +
                    "* Deltares, RGFGRID Version 5.01.00.45779, May 04 2016, 13:40:57\n" +
                    "* File creation date: 2016-12-26, 23:57:32\n" +
                    "*\n" +
                    "* Coordinate System = Spherical\n" +
                    "*\n" +
                    "*\n");

            for (String[][] data : dataBlock) {

                bufferedWriter.write("    L000" + count + "\n");
                bufferedWriter.write( "    " + data.length + "    2\n");

                for (int i = 0; i < data.length; i++) {
                    bufferedWriter.append("    " + data[i][0] + " " + data[i][1] + "\n");
                }

                count++;
            }

            bufferedWriter.close();

        }

    }

}
