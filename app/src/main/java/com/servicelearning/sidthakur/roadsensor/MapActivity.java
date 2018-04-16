package com.servicelearning.sidthakur.roadsensor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import static com.servicelearning.sidthakur.roadsensor.R.id.map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MapData mapData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapData = MapData.getInstance();
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        Iterator iterator = mapData.getmCoordinates().iterator();
//        while(iterator.hasNext())
//        {
//            googleMap.addMarker(new MarkerOptions().position((LatLng) iterator.next())
//                    .title("Pothole"));
//        }
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(18.5, 73.7), 10));

    }
}
