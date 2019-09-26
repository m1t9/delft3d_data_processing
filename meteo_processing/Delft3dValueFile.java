package ReaderWriter;

// Deltares Delft3d input value file

import java.util.ArrayList;

public class Delft3dValueFile {

    private final double version = 1.03;

    private String filetype;

    private static final int NODATA_value = -999;

    private int n_cols;

    private int n_rows;

    private String grid_unit;

    private double x_llcorner;

    private double y_llcorner;

    private double dx;

    private double dy;

    private int n_quantity;

    private ArrayList<String> quantity;

    private ArrayList<String> unit;

//    n_Quantity > 1 not supported yet;
    private String quantity1;

    private String unit1;

    public Delft3dValueFile(String filetype, int n_cols, int n_rows, String grid_unit, double x_llcorner,
                            double y_llcorner, double dx, double dy, int n_quantity, String quantity1, String unit1) {
        this.filetype = filetype;
        this.n_cols = n_cols;
        this.n_rows = n_rows;
        this.grid_unit = grid_unit;
        this.x_llcorner = x_llcorner;
        this.y_llcorner = y_llcorner;
        this.dx = dx;
        this.dy = dy;
        this.n_quantity = n_quantity;
        this.quantity1 = quantity1;
        this.unit1 = unit1;

        if (n_quantity > 1) {
            throw new UnsupportedOperationException("Not supported yet");
        }
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public void setN_cols(int n_cols) {
        this.n_cols = n_cols;
    }

    public void setN_rows(int n_rows) {
        this.n_rows = n_rows;
    }

    public void setGrid_unit(String grid_unit) {
        this.grid_unit = grid_unit;
    }

    public void setX_llcorner(double x_llcorner) {
        this.x_llcorner = x_llcorner;
    }

    public void setY_llcorner(double y_llcorner) {
        this.y_llcorner = y_llcorner;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setN_quantity(int n_quantity) {
        this.n_quantity = n_quantity;
    }

    public void setQuantity1(String quantity1) {
        this.quantity1 = quantity1;
    }

    public void setUnit1(String unit1) {
        this.unit1 = unit1;
    }

    public double getVersion() {
        return version;
    }

    public String getFiletype() {
        return filetype;
    }

    public int getNODATA_value() {
        return NODATA_value;
    }

    public int getN_cols() {
        return n_cols;
    }

    public int getN_rows() {
        return n_rows;
    }

    public String getGrid_unit() {
        return grid_unit;
    }

    public double getX_llcorner() {
        return x_llcorner;
    }

    public double getY_llcorner() {
        return y_llcorner;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public int getN_quantity() {
        return n_quantity;
    }

    public String getQuantity() {
        return quantity1;
    }

    public String getUnit() {
        return unit1;
    }


}
