package com.servicelearning.sidthakur.roadsensor;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddharth thakur on 4/18/2018.
 */

public class ReactiveAgent {

    private static double TH_IN_VEHICLE = 0.2;
    private static double TH_IN_BICYCLE = 0.3;
    public double roadQuality;

    DefValues thValues;

    public ReactiveAgent() {
        thValues = DefValues.getInstance();

    }


    public Boolean checkValidity(MainData dataObj) {
        setModeValues();
        if (dataObj.getGyroX() > thValues.getThX() || dataObj.getGyroY() > thValues.getThY()) {
            return true;
        }
        return false;
    }

    private void setModeValues() {
        if (thValues.getMode() == 0) {
            //ie mode is in_vehicle
            thValues.setThX(TH_IN_VEHICLE);
            thValues.setThY(TH_IN_VEHICLE);
        } else {
            //ie mode is in_bicycle
            thValues.setThX(TH_IN_BICYCLE);
            thValues.setThY(TH_IN_BICYCLE);
        }
    }

    public void analyzeRoad(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<JSONObject> array = WriteToFile.readData();
                Double sumY = 0.0;
                Double sumX = 0.0;
                for (JSONObject obj : array) {
                    try {

                        sumX = sumX + obj.getDouble("GyroX");
                        sumY = sumY + obj.getDouble("GyroY");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                roadQuality = sumX / array.size() > sumY / array.size() ? sumX / array.size() : sumY / array.size();
                Toast.makeText(context, "Road Quality is " + roadQuality, Toast.LENGTH_LONG).show();
            }
        }).start();

    }
}
