package com.servicelearning.sidthakur.roadsensor;

import java.util.ArrayList;

/**
 * Created by siddharth thakur on 10/23/2017.
 */

public class MapData {

    private static MapData instance;
    //arraylist for MianData
    public ArrayList<MainData> mCoordinates;

    private MapData() {
        mCoordinates = new ArrayList<>();
        // mCoordinates.add(new MainData().setLatLng(new LatLng(18.5508,73.7687)));
    }

    public static MapData getInstance() {
        if (instance == null) {
            instance = new MapData();
        }
        return instance;
    }

    public void addCoordinates(MainData point) {
        mCoordinates.add(point);
    }

    public ArrayList<MainData> getmCoordinates() {
        return mCoordinates;
    }
}
