package ReaderWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

class ReadWrite {


    static ArrayList<Double> getFileData(String fileName) {

        BufferedReader bufferedReader;

        ArrayList<Double> result = new ArrayList<>();

        try {

            bufferedReader = new BufferedReader(new FileReader(fileName));
            String[] line;

            while (bufferedReader.ready()) {
                line = bufferedReader.readLine().split("\\s+");
                for (String aLine : line) {
                    if (aLine.equals("NaN")) {
                        result.add(999.999);
                    } else if (isNumber(aLine)) {
                        result.add(Double.parseDouble(aLine));
                    }
                }
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    private static boolean isNumber(String item) {

        try {
            Double.parseDouble(item);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    protected static void writeFullFile(ArrayList<Double> xCoord, ArrayList<Double> yCoord, ArrayList<Double> uvData,
                                        String outputFileName) throws IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName));



    }

    protected static HashSet<String> readFileNames(String folderPath) {

        File folder = new File(folderPath);

        File[] listOfFiles = folder.listFiles();

        for (File file: listOfFiles) {
            System.out.println(file.getName());
        }

        return null;
    }

}
