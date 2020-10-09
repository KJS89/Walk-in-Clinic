package com.example.walkinclinics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivitySelectService extends AppCompatActivity {

    List<Service> services;
    List<Service> selectedServices;
    List<String> selected;
    List<String> hourlyrate;
    List<String> expRate;

    Button confirm;
    String role;
    ChipGroup chipGroup;
    DatabaseReference databaseServices;
    Util util;
    String op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);
        util = new Util();
        util.retrieveEmployee();
        Bundle b = getIntent().getExtras();
        role = b.getString("role");
        op=b.getString("op");
        selected = new ArrayList<>();
        hourlyrate = new ArrayList<>();
        expRate = new ArrayList<>();
        if(op!=null){
            if(b.getString("service").length()!= 0){
                String[] temp = b.getString("service").split(", ");
                for (String s : temp){
                    selected.add(s.split(":")[0]);
                    hourlyrate.add(s.split(":")[1]);
                    expRate.add(s.split(":")[2]);
                }
            }
        }

        databaseServices = FirebaseDatabase.getInstance().getReference(Util.DATABASE_SERVICE);
        confirm = findViewById(R.id.button10);
        chipGroup = findViewById(R.id.chip_group);

        services = new ArrayList<>();
        selectedServices = new ArrayList<>();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedServices.clear();
                for (int i = 0; i<chipGroup.getChildCount(); i++){
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    if(chip.isChecked()){
                        selectedServices.add(services.get(i));
                    }
                }
                if (selectedServices.size()==0){
                    String msg = "Please select at least one service";
                    Toast.makeText(ActivitySelectService.this,msg,Toast.LENGTH_LONG).show();
                }
                else{
                    launchRate();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        databaseServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                services.clear();
                chipGroup.removeAllViews();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Service s = postSnapshot.getValue(Service.class);
                    if (role.equals(s.getRole())){
                        services.add(s);
                        LayoutInflater inflater = LayoutInflater.from(ActivitySelectService.this);
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item,null,false);
                        chip.setText(s.getName());
                        if(selected.contains(s.getName())){
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void launchRate(){
        Employee employee = util.getEmployee();
        String temp = "";
        for(Service s : selectedServices){

            if (selected.contains(s.getName())){
                temp+=s.getName();
                temp+=":"+hourlyrate.get(selected.indexOf(s.getName()));
                temp+=":"+expRate.get(selected.indexOf(s.getName()))+", ";
            }
            else{
                temp+=s.getName()+":0:0, ";
            }


        }
        employee.setServices(temp);
        util.updateEmployee(employee);


        Intent intent = new Intent(this, ActivityEmployeeSetRate.class);
        intent.putExtra("services",temp);
        if(op!=null){
            intent.putExtra("op",op);
        }
        startActivity(intent);
    }


}
