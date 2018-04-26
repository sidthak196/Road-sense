package com.servicelearning.sidthakur.roadsensor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class SensorService extends Service implements SensorEventListener, LocationListener {

    private static final double DELTHRESHOLD = 0.2;


    //class variables
    Sensor gyroscopesensor;
    Sensor accelerometer;
    SensorManager sensorManager;
    MapData mapData;
    MainData dataObj;
    LocationManager locationManager;
    ReactiveAgent agent;
    Context context;

    private double lastUpdate = 0;

    public SensorService() {
        agent = new ReactiveAgent();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager.registerListener(this, gyroscopesensor, SensorManager.SENSOR_DELAY_GAME);
        // sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        mapData = MapData.getInstance();
        dataObj = new MainData();
        context = this;
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SENSORSERVICE", "service destroyed");
        sensorManager.unregisterListener(this);

    }

    @Override
    public boolean stopService(Intent name) {

        return super.stopService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        gyroscopesensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.d("SENSORSERVICE", "service created");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {

            // Log.d("SENSORSERVICE" , "inside onSensorChanged");
            //send data only when z rotation is greater than the threshold defined
            double difference = Math.abs(lastUpdate - event.values[2]);
            if (difference > DELTHRESHOLD) {
                dataObj.setGyroX(event.values[0]);
                dataObj.setGyroY(event.values[1]);
                dataObj.setGyroZ(event.values[2]);

                lastUpdate = event.values[2];
                Boolean shouldsend = agent.checkValidity(dataObj);
                //// TODO: 4/17/2018  check if the data agrees with the agent
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);

            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("SENSORSERVICE", "location recieved :" + location.getLatitude() + "  " + location.getLongitude());
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        dataObj.setLatLng(latLng);
        mapData.addCoordinates(dataObj);
        // sendData();
        new SendPostRequest(dataObj).execute();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class SendPostRequest extends AsyncTask<Void, Void, String> {

        MainData data;

        SendPostRequest(MainData data) {
            this.data = data;
        }

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... arg0) {
            HttpURLConnection conn = null;
            try {

                URL url = new URL("https://road-sense.herokuapp.com");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("Latitude", data.getLatLng().latitude);
                postDataParams.put("Longitude", data.getLatLng().longitude);
                postDataParams.put("GyroX", data.getGyroX());
                postDataParams.put("GyroY", data.getGyroY());
                postDataParams.put("GyroZ", data.getGyroZ());
                Log.e("params", postDataParams.toString());

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                //write to file
                int result = WriteToFile.appendToFile(postDataParams.toString());
                int responseCode = conn.getResponseCode();
                if (result == 2) {
                    agent.analyzeRoad(context);
                }
                return Integer.toString(responseCode);
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("SENSORSERVICE", "Data sent status , response code : " + result);
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
