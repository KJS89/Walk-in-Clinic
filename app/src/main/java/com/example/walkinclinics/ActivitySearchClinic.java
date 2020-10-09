package com.example.walkinclinics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivitySearchClinic extends AppCompatActivity implements OnMapReadyCallback {
    Button cancel;
    Button specificSearch;
    GoogleMap gM;
    TextView text_info;
    String info;
    String payments;
    String own_payment;
    String id;
    Employee em;

    List<Employee> employees;

    DatabaseReference database_employee;
    Button confirm_booking;
    Util util;

    private MapView mapView;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_clinic);
        util = new Util();

        System.out.println("==============================="+util.getHold_patient());


        Bundle b = getIntent().getExtras();
        own_payment = b.getString("payment");
        cancel = findViewById(R.id.button23);
        text_info = findViewById(R.id.textView44);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        specificSearch = findViewById(R.id.button17);
        confirm_booking = findViewById(R.id.button2);
        confirm_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(em==null){
                    Toast.makeText(ActivitySearchClinic.this,"Please select one clinic",Toast.LENGTH_LONG).show();
                }
                else if(payments.contains(own_payment)){
                    util.hold_employee(em);
                    launchCompleteAppointment();
                }
                else{
                    Toast.makeText(ActivitySearchClinic.this,
                            "The clinic does not accpet your payment method, please change",Toast.LENGTH_LONG).show();
                }
            }
        });
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.mapView5);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);


        database_employee = Util.getAccountDR();

        specificSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSpecificSearch();
            }
        });
        employees = new ArrayList<>();





    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        database_employee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                employees.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Person p = postSnapshot.getValue(Person.class);
                    if(p.getClassName().equals("Employee")){
                        Employee e = postSnapshot.getValue(Employee.class);
                        employees.add(e);
                        String[] temp = e.getAddress().split("@")[1].split(", ");
                        LatLng latLng = new LatLng(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]));
                        gM.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(e.getClinicName())
                                .snippet(e.getRole()));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gM = googleMap;
        LatLng latLng = new LatLng(45.4231, -75.6831);
        float zoomLevel = 15.0f; //This goes up to 21
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        System.out.println("======================== "+employees.size());
        gM.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                updateTextView(marker.getTitle());
                return true;
            }
        });
    }

    private void updateTextView(String clinicName){
        for(Employee e: employees){
            if (e.getClinicName().equals(clinicName)){
                info = "Clinic name: "+e.getClinicName()+", Role: "+e.getRole()+", ";
                info+= "Insurance accepted: " + e.getInsurance()+", Payment accepted: "+e.getPayment()+", ";
                String[] temp = e.getWh().split("@");
                info+="Walk in hours: "+temp[0] +" at ";
                info+=temp[1].substring(0,2)+":"+temp[1].substring(2,4)+" to ";
                info+=temp[1].substring(4,6)+":"+temp[1].substring(6,8);
                text_info.setText(info);
                payments = e.getPayment();
                id = e.getId();
                em = e;
            }
        }

    }
    private void launchSpecificSearch(){
        Intent intent = new Intent(this,ActivitySpecificSearch.class);
        Bundle b = getIntent().getExtras();
        intent.putExtra("id",b.getString("id"));
        startActivity(intent);
    }
    private void launchCompleteAppointment(){
        Intent intent = new Intent(this,ActivityCompleteAppointment.class);
        startActivity(intent);
    }

}
