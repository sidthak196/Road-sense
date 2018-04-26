package com.servicelearning.sidthakur.roadsensor;

/**
 * Created by siddharth thakur on 4/18/2018.
 */

public class ReactiveAgent {

    // TODO: 4/18/2018 *set values
    // TODO: 4/18/2018 *set default mechanism
    //// TODO: 4/18/2018  add funtion to check and validate data
    private static double TH_IN_VEHICLE = 0.2;
    private static double TH_IN_BICYCLE = 0.3;

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
}
