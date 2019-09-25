package ReaderWriter;

import java.io.*;
import java.util.ArrayList;

public class OutEdit {

    protected static void gridOutEdit() throws IOException {

        ArrayList<Double> xList = new ArrayList<>();
        ArrayList<Double> yList = new ArrayList<>();

        BufferedReader bufferedReader;
        String fileName = "";

        bufferedReader = new BufferedReader(new FileReader(fileName));
        String[] line;

        while (bufferedReader.ready()) {

            line = bufferedReader.readLine().split("\\s+");
            if (Double.parseDouble(line[0]) > 0) {
//                System.out.println(line);
                xList.add(Double.parseDouble(line[0]));
                yList.add(Double.parseDouble(line[1]));
            }
        }
        bufferedReader.close();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(""));

        for (int i = 0; i < xList.size(); i++) {
            bufferedWriter.write(xList.get(i) + " " + yList.get(i) + "\n");
        }
        bufferedWriter.close();


    }

}
