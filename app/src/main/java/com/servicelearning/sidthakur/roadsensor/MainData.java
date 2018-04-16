package com.servicelearning.sidthakur.roadsensor;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by siddharth thakur on 10/24/2017.
 */

public class MainData {
    private LatLng latLng;
    private double gyroX;
    private double gyroY;
    private double gyroZ;
    private double deltaBump;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public double getGyroX() {
        return gyroX;
    }

    public void setGyroX(double gyroX) {
        this.gyroX = gyroX;
    }

    public double getGyroY() {
        return gyroY;
    }

    public void setGyroY(double gyroY) {
        this.gyroY = gyroY;
    }

    public double getGyroZ() {
        return gyroZ;
    }

    public void setGyroZ(double gyroZ) {
        this.gyroZ = gyroZ;
    }

    public double getDeltaBump() {
        return deltaBump;
    }

    public void setDeltaBump(double deltaBump) {
        this.deltaBump = deltaBump;
    }

}
