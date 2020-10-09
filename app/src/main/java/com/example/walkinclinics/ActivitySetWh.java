package com.example.walkinclinics;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivitySetWh extends AppCompatActivity {

    CheckBox checkMonday;
    CheckBox checkTuesday;
    CheckBox checkWednesday;
    CheckBox checkThursday;
    CheckBox checkFriday;
    EditText startTimeHour;
    EditText startTimeMin;
    EditText endTimeHour;
    EditText endTimeMin;
    Button confirm;

    String startHour;
    String startMin;
    String endHour;
    String endMin;

    String role;
    Boolean flag = true;
    String error;
    String workingHours;
    Util util;
    String op;
    String wh;
    List<String> weekdays;
    List<String> times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setwh);
        util = new Util();
        util.retrieveEmployee();
        Bundle b = getIntent().getExtras();
        role = b.getString("role");
        op = b.getString("op");

        checkMonday = findViewById(R.id.cb_monday);
        checkTuesday = findViewById(R.id.cb_tuesday);
        checkWednesday = findViewById(R.id.cb_wednesday);
        checkThursday = findViewById(R.id.cb_thursday);
        checkFriday = findViewById(R.id.cb_friday);
        startTimeHour = findViewById(R.id.start_hour);
        startTimeMin = findViewById(R.id.start_min);
        endTimeHour = findViewById(R.id.end_hour);
        endTimeMin = findViewById(R.id.end_min);
        confirm = findViewById(R.id.setWhConfirm);

        if(op!=null){
            wh = b.getString("wh");
            initialize();
            if (weekdays.contains("monday")){ checkMonday.setChecked(true); }
            if (weekdays.contains("tuesday")){ checkTuesday.setChecked(true); }
            if (weekdays.contains("wednesday")){ checkWednesday.setChecked(true); }
            if (weekdays.contains("thursday")){ checkThursday.setChecked(true); }
            if (weekdays.contains("friday")){ checkFriday.setChecked(true); }
            startTimeHour.setText(times.get(0));
            startTimeMin.setText(times.get(1));
            endTimeHour.setText(times.get(2));
            endTimeMin.setText(times.get(3));
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startHour = startTimeHour.getText().toString().trim();
                startMin = startTimeMin.getText().toString().trim();
                endHour = endTimeHour.getText().toString().trim();
                endMin = endTimeMin.getText().toString().trim();

                startHour = addZero(startHour);
                startMin = addZero(startMin);
                endHour = addZero(endHour);
                endMin = addZero(endMin);

                if (!isHourValid(startHour) || !isHourValid(endHour) ){
                    flag = false;
                }
                if( !isMinValid(startMin) || !isMinValid(endMin) ){
                    flag = false;
                }

                Boolean allCheckBoxEmpty = !checkMonday.isChecked() && !checkTuesday.isChecked() &&
                        !checkWednesday.isChecked() && !checkThursday.isChecked() && !checkFriday.isChecked();
                if(TextUtils.isEmpty(startHour) || TextUtils.isEmpty(startMin) || TextUtils.isEmpty(endHour)
                || TextUtils.isEmpty(endMin) || allCheckBoxEmpty){
                    flag = false;
                    if(allCheckBoxEmpty){
                        error = "can't rest all weekdays :(";
                    }
                    else{
                        error = "Missing fields";
                    }
                }
                if(flag){
                    if ( Integer.parseInt(startHour+startMin)>=Integer.parseInt(endHour+endMin)) {
                        flag = false;
                        error = "end time ends earlier or equal than start time";
                    }
                }


                if(flag){
                    workingHours = "";
                    if(checkMonday.isChecked()){workingHours += "monday, ";}
                    if(checkTuesday.isChecked()){workingHours += "tuesday, ";}
                    if(checkWednesday.isChecked()){workingHours += "wednesday, ";}
                    if(checkThursday.isChecked()){workingHours += "thursday, ";}
                    if(checkFriday.isChecked()){workingHours += "friday, ";}

                    workingHours+="@"+startHour+startMin+endHour+endMin;
                    if(op==null){
                        launchSelectService();
                    }
                    else{
                        Employee employee = util.getEmployee();
                        employee.setWh(workingHours);
                        util.updateEmployee(employee);
                        finish();
                    }

                }
                else {
                    flag = true;
                    Toast.makeText(ActivitySetWh.this,error,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isHourValid(String s){
        Integer integer=null;
        try{
            integer = Integer.parseInt(s);
        }
        catch (Exception e){}
        if(integer == null){
            error = "one of the hour entered is not an integer";
            return false;
        }
        else if(integer<6 || integer>23){
            error = "one of the hour entered is not in the range [6,23]";
            return false;
        }
        else {
            return true;
        }
    }
    private boolean isMinValid(String s){
        Integer integer=null;
        try{
            integer = Integer.parseInt(s);
        }
        catch (Exception e){}
        if(integer == null){
            error = "one of the minute entered is not an integer";
            return false;
        }
        else if(integer<0 || integer>59){
            error = "one of the minute entered is not in the range [0,59]";
            return false;
        }
        else {
            return true;
        }
    }
    private String addZero(String s){
        if (s.length()==1){
            return "0"+s;
        }
        return s;
    }
    private void initialize(){
        String[] splited = wh.split("@");
        times = new ArrayList<>();
        times.add(splited[1].substring(0,2));
        times.add(splited[1].substring(2,4));
        times.add(splited[1].substring(4,6));
        times.add(splited[1].substring(6,8));
        splited = splited[0].split(", ");
        weekdays = new ArrayList<>();
        for (String s: splited){
            weekdays.add(s);
        }
    }
    private void launchSelectService(){
        Employee employee = util.getEmployee();
        employee.setWh(workingHours);
        util.updateEmployee(employee);

        Intent intent = new Intent(this, ActivitySelectService.class);
        intent.putExtra("role",role);
        startActivity(intent);
    }

}
