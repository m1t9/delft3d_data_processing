package ReaderWriter;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Boundary {

    static ArrayList<String> name;
    static ArrayList<String> ampl;
    static ArrayList<String> phase;

    public static void ReadBoundData(String fileName) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

        name = new ArrayList<>();
        ampl = new ArrayList<>();
        phase = new ArrayList<>();

        String line = "";

        bufferedReader.readLine();
        bufferedReader.readLine();

        while (bufferedReader.ready()) {

            line = bufferedReader.readLine();

            name.add(line.split("\\s+")[4]);
            ampl.add(line.split("\\s+")[5]);
            phase.add(line.split("\\s+")[6]);

        }

        bufferedReader.close();

    }

    public static void writeData(String fileName) throws IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));

        int tideCount = 8;
        int localPointCount = 0;
        int globalCount = 0;

        bufferedWriter.write("point" + globalCount + "\n");
        globalCount++;
        for (int i = 0; i < name.size(); i++ ) {
            bufferedWriter.write(buildString( name.get(i), ampl.get(i), phase.get(i)) );
            localPointCount++;
            if (localPointCount == tideCount && (name.size() / 8 != globalCount)) {
                bufferedWriter.write("point" + globalCount + "\n");
                globalCount++;
                localPointCount = 0;
            }
        }

        bufferedWriter.close();


    }

    private static String buildString(String name, String ampl, String phase) {

        return name.toUpperCase() +
                "        " + String.format("%1.7e", Double.parseDouble(ampl)) +
                "  " + String.format("%1.7e", Double.parseDouble(phase)) + "\n";
    }

    public static void writeBND(String fileName, int mDotEnd, int start, int end) throws IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));

//        int mDotEnd = 136;
//        int start = 5;
        int count = start;
//        int end = 120;
        int nameCount = 0;

        while (count + 1 < end) {

            bufferedWriter.write(String.format("%-21s %s %s%6s%6s%6s%6s%s%-13s%s\n", ("BNDp" + nameCount), "Z", "A",
                    count, mDotEnd, (count + 2), mDotEnd,
                    "  0.0000000e+000 ", ("point" + nameCount), ("point" + (nameCount + 1))));

            nameCount += 2;
            count += 2;

        }

        bufferedWriter.close();

    }


    public static void main(String[] args) throws IOException {

        ReadBoundData("D:\\d3d_workdir\\1009_PROJECT\\BOUNDARY\\bound1_amp_phs.out");
        writeData("D:\\d3d_workdir\\1009_PROJECT\\BOUNDARY\\out_grd1");
        writeBND("D:\\d3d_workdir\\1009_PROJECT\\BOUNDARY\\BND_1grd.bnd", 134, 6, 121);

        ReadBoundData("D:\\d3d_workdir\\1009_PROJECT\\BOUNDARY\\bound2_amp_phs.out");
        writeData("D:\\d3d_workdir\\1009_PROJECT\\BOUNDARY\\out_grd2");
        writeBND("D:\\d3d_workdir\\1009_PROJECT\\BOUNDARY\\BND_2grd.bnd", 267, 148, 218);

    }

}
