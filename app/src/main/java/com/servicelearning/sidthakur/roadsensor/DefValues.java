package com.servicelearning.sidthakur.roadsensor;

/**
 * Created by siddharth thakur on 4/26/2018.
 */

public class DefValues {

    private static DefValues object;

    public double getThX() {
        return thX;
    }

    public void setThX(double thX) {
        this.thX = thX;
    }

    public double getThY() {
        return thY;
    }

    public void setThY(double thY) {
        this.thY = thY;
    }

    private double thX;
    private double thY;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    private int mode;

    private DefValues() {
        //private constructor
    }

    public static DefValues getInstance() {
        if (object == null)
            object = new DefValues();
        return object;
    }
}
