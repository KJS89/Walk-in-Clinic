package com.example.walkinclinics;

import android.content.Intent;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class ActivitySelectAddress extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap googleMap;
    Marker marker;
    Button confirm;
    TextView textAddress;
    String address;
    LatLng latlng;
    Util util;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        util = new Util();
        util.retrieveEmployee();

        confirm = findViewById(R.id.button);
        textAddress = findViewById(R.id.textSelectAddress);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address==null){
                    String msg = "Please click on the map to select one address";
                    Toast.makeText(ActivitySelectAddress.this, msg, Toast.LENGTH_LONG).show();
                }
                else{ launchEmployeeSignUp(); }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng latLng = new LatLng(45.4231, -75.6831); // ottawa coordinates
        float zoomLevel = 15.0f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMapToolbarEnabled(false); // invisible for marker's directions/information
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int actionType = ev.getAction();
        switch (actionType) {
            case MotionEvent.ACTION_UP:
                Projection proj = googleMap.getProjection();
                Point point = new Point((int)ev.getX(), (int)ev.getY()-Util.getRelativeTop(mapView));
                if(point.y>0 && point.y<Util.getRelativeBottom(mapView)-Util.getRelativeTop(mapView)){
                    // need to click on the map
                    if(!(943< point.x && point.x<1053 && 863 < point.y && point.y<1073)){
                        // if does not click on zoom button
                        latlng = proj.fromScreenLocation(point);
                        if (marker!=null){
                            marker.setVisible(false);
                        }
                        marker = googleMap.addMarker(new MarkerOptions().position(latlng).title("")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                .draggable(false).visible(true));
                        Geocoder geocoder;
                        List<Address> addresses = null;
                        geocoder = new Geocoder(this, Locale.getDefault());
                        try{
                            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1); }
                        catch (Exception e){}
                        address = addresses.get(0).getAddressLine(0);
                        textAddress.setText("You have selected: "+address);
                    }
                }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void launchEmployeeSignUp() {


        Employee employee = util.getEmployee();
        employee.setAddress(address +"@"+latlng.latitude+", "+latlng.longitude);
        util.updateEmployee(employee);



        Intent intent = new Intent(this, ActivityEmployeeSignUp.class);
        startActivity(intent);
    }

}
