import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class InterpolationBoundaryPoints {


    ArrayList<Double> lat;
    ArrayList<Double> lon;
    ArrayList<Double> dep;
    ArrayList<Double> data;
    ArrayList<Double> layerThickness;

    public InterpolationBoundaryPoints(ArrayList<Double> lat, ArrayList<Double> lon,
                                       ArrayList<Double> dep, ArrayList<Double> data,
                                       ArrayList<Double> layerThickness) {
        this.lat = lat;
        this.lon = lon;
        this.dep = dep;
        this.data = data;
        this.layerThickness = layerThickness;
    }

    /**
     * MAIN INTERPOLATION FUNCTION
     * @throws IOException
     */
    protected double[][] interpolatePoints(boolean flag, boolean flag2) throws Exception {

        /**
         * READ BOUND POINTS
         */
        ArrayList<Double> boundLat = new ArrayList<>();
        ArrayList<Double> boundLon = new ArrayList<>();
        ArrayList<Double> boundDep = new ArrayList<>();

        // READ BOUND COORDS AND DEPTH

        if (!flag) {
            boundDep = readDepthForPoints("some_path");
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader("some_path"));
            String line;
            while (bufferedReader.ready()) {
                for (String value : bufferedReader.readLine().split("\\s+")) {
                    try {
                        boundLat.add(Double.parseDouble(value));
                    } catch (Exception e) { }
                }
            }
            bufferedReader.close();
            bufferedReader = new BufferedReader(
                    new FileReader("some_path"));
            while (bufferedReader.ready()) {
                for (String value : bufferedReader.readLine().split("\\s+")) {
                    try {
                        boundLon.add(Double.parseDouble(value));
                    } catch (Exception e) { }
                }
            }
            bufferedReader.close();
        } else {
            boundLat = new ArrayList<>();
            boundLat.add(68.55);
            boundLon = new ArrayList<>();
            boundLon.add(73.95);
            boundDep = new ArrayList<>();
            boundDep.add(9.54);
        }

        double[][] interpData = new double[5][boundDep.size()];
        double horizon;
        double pointValue;

        for (int layer = 0; layer < 5; layer++) {
            for (int i = 0; i < boundLat.size(); i++) {
                if (boundLat.get(i) > -900) {
                    if (boundDep.get(i) > -900) {
                        for (int dataLat = 0; dataLat < lat.size() - 1; dataLat++) {
                            for (int dataLon = 0; dataLon < lon.size() - 1; dataLon++) {
                                if (flag2) {
                                    if (lat.get(dataLat) == 69 && lon.get(dataLon) == 74.5) {
//                                        (Y1 + X1*longitudeCount) + Z1 * longitudeCount * latitudeCount
                                        pointValue = data.get(dataLon + dataLat*265);
//                                        if (pointValue < 1.5) pointValue = 1.5;
                                        interpData[layer][i] = pointValue;
                                    }
                                } else {
                                    if (boundLat.get(i) > lat.get(dataLat) && boundLat.get(i) < lat.get(dataLat + 1) &&
                                            boundLon.get(i) > lon.get(dataLon) && boundLon.get(i) < lon.get(dataLon + 1)
                                            ) {
//                                    NOT CHECK IF COORDINATES MATCH
                                        horizon = getHorizont(layer, boundDep.get(i));
                                        for (int z = 0; z < dep.size(); z++) {
                                            if (horizon > dep.get(z) && horizon < dep.get(z + 1)) {
                                                pointValue =  interpolation(dataLat, dataLon, dataLat + 1, dataLon + 1, z, z + 1,
//                                                        boundLat.get(i), boundLon.get(i), boundDep.get(i),
                                                        boundLat.get(i), boundLon.get(i), horizon,
                                                        lat.get(dataLat), lon.get(dataLon), dep.get(z),
                                                        lat.get(dataLat + 1), lon.get(dataLon + 1), dep.get(z + 1)
                                                );
                                                interpData[layer][i] = pointValue;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        interpData[layer][i] = -999.;
                    }
                } else {
                    interpData[layer][i] = -999.;
                }
            }
        }

        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < boundDep.size(); j++) {
                if (Double.isNaN(interpData[i][j])) {
                    interpData[i][j] = interpData[i - 1][j];
                }
            }
        }
        return interpData;
    }

    protected double getHorizont(int layer, Double depth) {
        double upperThickness = 0;
        for (int i = 0; i <= layer - 1; i++) {
            upperThickness += layerThickness.get(i) * depth;
        }
        return upperThickness + (layerThickness.get(layer) * depth) / 2.;
    }

    /**
     * READ INPUT DATA
     * @param fileName
     * @return
     * @throws IOException
     */
    protected static ArrayList readAdditionalData(String fileName, boolean flag) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        ArrayList<Double> data = new ArrayList<>();
        while (bufferedReader.ready()) {
            for (String value : bufferedReader.readLine().split("\\s+")) {
                if (flag) {
                    if (Double.parseDouble(value) < 1.5) {
                        data.add(1.5);
                    } else  {
                        data.add(Double.parseDouble(value));
                    }
                } else {
                    data.add(Double.parseDouble(value));
                }
            }
        }
        bufferedReader.close();
        return data;
    }

    protected static ArrayList readThickness(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        ArrayList<Double> thickness = new ArrayList<>();
        while (bufferedReader.ready()) {
            thickness.add(Double.parseDouble(bufferedReader.readLine()));
        }

        bufferedReader.close();
        return thickness;
    }

    /**
     * READ DEPTH IN BOUND POINTS
     * @param fileName
     * @return
     * @throws IOException
     */
    private ArrayList<Double> readDepthForPoints(String fileName) throws IOException {
        ArrayList<Double> data = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        int count = 1;
        int start = 13771;
        int stop = 13805;
        String currentLine;
        while (bufferedReader.ready()) {
            currentLine = bufferedReader.readLine();
            if (count >= start && count < stop) {

                for (String value : currentLine.split("\\s+")) {
                    if (value != "")
                        try {
                            data.add(Double.parseDouble(value));
                        } catch (Exception e) { }
                }
            }
            count++;
        }
        data.remove(data.size() - 1);
        bufferedReader.close();
        return data;
    }

    public static void main(String[] args) throws Exception {

        ArrayList<Double> thick = readThickness("some_path");
        ArrayList<Double> lat = readAdditionalData("some_path", true);
        ArrayList<Double> lon = readAdditionalData("some_path", true);
        ArrayList<Double> dep = readAdditionalData("some_path", false);
        ArrayList<Double> data;
        String year;
        year = "1999";
        File folder = new File("some_path" + year);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> TNames = new ArrayList<>();
        ArrayList<String> SNames = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].getName().contains("S")) {
                SNames.add(listOfFiles[i].getName());
            } else {
                TNames.add(listOfFiles[i].getName());
            }
        }

        int fileCount = 0;
        ArrayList<String> timeFrameT = new ArrayList<>();
        ArrayList<double[][]> boundaryDataT = new ArrayList<>();
        String inputFolder = "some_path" + year + "\\";
        for (String name : TNames) {
            data = readAdditionalData(inputFolder + name, true);
            InterpolationBoundaryPoints interpolationBoundaryPoints = new InterpolationBoundaryPoints(lat, lon, dep, data, thick);
            double[][] result = interpolationBoundaryPoints.interpolatePoints(false, false);
//            double[][] result = interpolationBoundaryPoints.interpolatePoints(true, false); // for one point (OB)
//            double[][] result = interpolationBoundaryPoints.interpolatePoints(true, true); // for one point (TAZ)
            boundaryDataT.add(result);
            timeFrameT.add(name.substring(0, 10));
            System.out.println(name + " complete;");
//            fileCount++;
//            if (fileCount == 3) {
//                break;
//            }
        }

        fileCount = 0;
        ArrayList<String> timeFrameS = new ArrayList<>();
        ArrayList<double[][]> boundaryDataS = new ArrayList<>();
        for (String name : SNames) {
            data = readAdditionalData(inputFolder + name, true);
            InterpolationBoundaryPoints interpolationBoundaryPoints = new InterpolationBoundaryPoints(lat, lon, dep, data, thick);
            double[][] result = interpolationBoundaryPoints.interpolatePoints(false, false);
//            double[][] result = interpolationBoundaryPoints.interpolatePoints(true, false); // for one point (OB)
//            double[][] result = interpolationBoundaryPoints.interpolatePoints(true, true); // for one point (TAZ)
            boundaryDataS.add(result);
            timeFrameS.add(name.substring(0, 10));
            System.out.println(name + " complete;");
//            fileCount++;
//            if (fileCount == 3) {
//                break;
//            }
        }

        System.out.println("STRAT WRITE BND FILE");
        WriteBoundaryTS writeBoundaryTS = new WriteBoundaryTS(boundaryDataT, boundaryDataS);
        writeBoundaryTS.writeData(
                "some_path" +
                        year + ".bcc",
                9, 184, 1, false, false
                );
        writeBoundaryTS.writeData(
                "some_path" +
                        year + ".bcc",
                322, 406, 89, true, false
                );
//        writeBoundaryTS.writeData(
//                "some_path" + year + ".bcc",
//                0, 1, 132, false, true
//                );
//        writeBoundaryTS.writeData(
//                "some_path" + year + ".bcc",
//                0, 1, 133, false, true
//                );
        System.out.println("COMPLETE");
    }

    protected double interpolation(int X1, int Y1, int X2, int Y2, int Z1, int Z2,
                                   double X, double Y, double Z,
                                   double nX1, double nY1, double nZ1,
                                   double nX2, double nY2, double nZ2
    ) throws Exception {
        int latitudeCount = 49;
        int longitudeCount = 265;
        int horizontsCount = 42;
        int upperNaNCount;
        int downNaNCount;
        double point1 = 0, point2 = 0, point3 = 0, point4 = 0, point5 = 0, point6 = 0, point7 = 0, point8 = 0;
        upperNaNCount = 0;
        downNaNCount = 0;
        ArrayList<Double> points = new ArrayList<>();

        point1 = data.get((Y1 + X1*longitudeCount) + Z1 * longitudeCount * latitudeCount);
        points.add(point1);
        if (Double.isNaN(point1)) upperNaNCount++;
        point2 = data.get((Y1 + X1*longitudeCount) + Z2 * longitudeCount * latitudeCount);
        points.add(point2);
        if (Double.isNaN(point2)) downNaNCount++;
        point3 = data.get((Y2 + X1*longitudeCount) + Z1 * longitudeCount * latitudeCount);
        points.add(point3);
        if (Double.isNaN(point3)) upperNaNCount++;
        point4 = data.get((Y2 + X1*longitudeCount) + Z2 * longitudeCount * latitudeCount);
        points.add(point4);
        if (Double.isNaN(point4)) downNaNCount++;
        point5 = data.get((Y1 + X2*longitudeCount) + Z1 * longitudeCount * latitudeCount);
        points.add(point5);
        if (Double.isNaN(point5)) upperNaNCount++;
        point6 = data.get((Y1 + X2*longitudeCount) + Z2 * longitudeCount * latitudeCount);
        points.add(point6);
        if (Double.isNaN(point6)) downNaNCount++;
        point7 = data.get((Y2 + X2*longitudeCount) + Z1 * longitudeCount * latitudeCount);
        points.add(point7);
        if (Double.isNaN(point7)) upperNaNCount++;
        point8 = data.get((Y2 + X2*longitudeCount) + Z2 * longitudeCount * latitudeCount);
        points.add(point8);
        if (Double.isNaN(point8)) downNaNCount++;

        double part1 = 0, part2 = 0, part3 = 0, part4 = 0, part5 = 0, part6 = 0, part7 = 0, part8 = 0;
//        NOT WORKING YET
//        if (upperNaNCount == 0 && downNaNCount > 0) {
//            part1 = (point1 / (nX2 - nX1)*(nY2 - nY1)) * (nX2 - X)*(nY2 - Y);
//            part2 = (point3 / (nX2 - nX1)*(nY2 - nY1)) * (X - nX1)*(nY2 - Y);
//            part3 = (point5 / (nX2 - nX1)*(nY2 - nY1)) * (nX2 - X)*(Y - nY1);
//            part4 = (point7 / (nX2 - nX1)*(nY2 - nY1)) * (X - nX1)*(Y - nY1);
////            System.out.println((part1 + part2 + part3 + part4) + " " + part1 + " " + part2 + " " + part3 + " " + part4);
//            return part1 + part2 + part3 + part4;
//        } else {
            part1 = (point1 / ((nX2 - nX1)*(nY2 - nY1)*(nZ2 - nZ1))) * (nX2 - X)* (nY2 - Y) * (nZ2 - Z);
            part2 = (point2 / ((nX2 - nX1)*(nY2 - nY1)*(nZ2 - nZ1))) * (nX2 - X)* (nY2 - Y) * (Z - nZ1);
            part3 = (point3 / ((nX2 - nX1)*(nY2 - nY1)*(nZ2 - nZ1))) * (nX2 - X)* (Y - nY1) * (nZ2 - Z);
            part4 = (point4 / ((nX2 - nX1)*(nY2 - nY1)*(nZ2 - nZ1))) * (nX2 - X)* (Y - nY1) * (Z - nZ1);
            part5 = (point5 / ((nX2 - nX1)*(nY2 - nY1)*(nZ2 - nZ1))) * (X - nX1)* (nY2 - Y) * (nZ2 - Z);
            part6 = (point6 / ((nX2 - nX1)*(nY2 - nY1)*(nZ2 - nZ1))) * (X - nX1)* (nY2 - Y) * (Z - nZ1);
            part7 = (point7 / ((nX2 - nX1)*(nY2 - nY1)*(nZ2 - nZ1))) * (X - nX1)* (Y - nY1) * (nZ2 - Z);
            part8 = (point8 / ((nX2 - nX1)*(nY2 - nY1)*(nZ2 - nZ1))) * (X - nX1)* (Y - nY1) * (Z - nZ1);
            
            return part1 + part2 + part3 + part4 + part5 + part6 + part7 + part8;
    }
}
