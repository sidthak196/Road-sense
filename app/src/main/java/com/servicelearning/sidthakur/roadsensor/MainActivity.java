package com.servicelearning.sidthakur.roadsensor;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Sensor gyroscopesensor;
    Sensor accelerometer;
    SensorManager sensorManager;
    SensorEventListener gyroscopeListener;
    SensorEventListener accelerometerListener;
    TextView xText, xAText;
    TextView yText, yAText;
    TextView zText, zAText, speedText;
    Button showMapButton;
    Button startService;
    Button stopService;
    GoogleApiClient mApiClient;

    private long lastUpdate = 0;
    MapData mapData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapData = MapData.getInstance();
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // WriteToFile.appendToFile("Hello world");
        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);

//        xAText = (TextView)findViewById(R.id.xAText);
//        yAText = (TextView)findViewById(R.id.yAText);
//        zAText = (TextView)findViewById(R.id.zAText);
//        speedText = (TextView)findViewById(R.id.speed);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopesensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Sensor mySensor = event.sensor;

                if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {

                    long curTime = System.currentTimeMillis();

                    if ((curTime - lastUpdate) > 100) {
                        lastUpdate = curTime;
                        xText.setText("" + event.values[0]);
                        yText.setText("" + event.values[1]);
                        zText.setText("" + event.values[2]);
                    }
                }

//              THE CODE FOR ACCELEROMETER
//               if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                    float x = event.values[0];
//                    float y = event.values[1];
//                    float z = event.values[2];
//
//                    long curTime = System.currentTimeMillis();
//
//                    if ((curTime - lastUpdate) > 100) {
//                        long diffTime = (curTime - lastUpdate);
//                        lastUpdate = curTime;
//
//                        float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
//
//                        if (speed > SHAKE_THRESHOLD) {
//                            xAText.setText("" + x);
//                            yAText.setText("" + y);
//                            zAText.setText("" + z);
//                            speedText.setText("speed : "+ speed);
//                        }
//
//                        last_x = x;
//                        last_y = y;
//                        last_z = z;
//
//                    }
//
//                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(gyroscopeListener, gyroscopesensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroscopeListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        showMapButton = (Button) findViewById(R.id.showMapButton);
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                //intent.putExtra("MapData",mapData);
                startActivity(intent);
            }
        });
        final Intent intent = new Intent(MainActivity.this, SensorService.class);
        startService = (Button) findViewById(R.id.ServiceButton);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startService(intent);
            }
        });
        stopService = (Button) findViewById(R.id.ServiceButtonStop);
        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroscopeListener);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 3000, pendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
