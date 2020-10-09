package com.example.walkinclinics;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivitySpecificSearch extends AppCompatActivity {
    ListView clinics;
    Button cancel;
    Button search;
    RadioGroup rG;
    List<Employee> search_result_clinics;
    Employee selected_employee;

    List<Employee> employees;
    DatabaseReference db_employee;
    String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_search);
        db_employee = Util.getAccountDR();
        employees = new ArrayList<>();
        search_result_clinics = new ArrayList<>();
        cancel = findViewById(R.id.button29);
        search = findViewById(R.id.button28);
        rG = findViewById(R.id.search_option);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clinics = findViewById(R.id.listClinics2);

        clinics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_employee = search_result_clinics.get(position);
                showConfirmClinicDialog();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_result_clinics.clear();
                int selected = rG.getCheckedRadioButtonId();
                if(selected == -1){
                    Toast.makeText(ActivitySpecificSearch.this,"please select one search type",Toast.LENGTH_LONG).show();
                }
                if(selected == R.id.radioButton8){
                    searchByRole();
                }
                if(selected == R.id.radioButton17){
                    searchByTime();
                }
                if(selected == R.id.radioButton18){
                    searchByWorkDay();
                }
                if(selected == R.id.radioButton24){
                    searchByName();
                }
                addListClinic();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        db_employee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                employees.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Person p = postSnapshot.getValue(Person.class);
                    if (p.getClassName().equals("Employee")) {
                        employees.add(postSnapshot.getValue(Employee.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void launchCompleteAppointment(){
        Intent intent = new Intent(this,ActivityCompleteAppointment.class);
        Bundle b = getIntent().getExtras();
        Util util = new Util();
        util.hold_employee(selected_employee);
        startActivity(intent);
    }
    private void showConfirmClinicDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_select_clinic, null);
        dialogBuilder.setView(dialogView);
        final Button confirm = dialogView.findViewById(R.id.button24);
        final Button cancel = dialogView.findViewById(R.id.button25);
        final TextView textView = dialogView.findViewById(R.id.textView51);
        textView.setText(selected_employee.getClinicName());
        final AlertDialog b = dialogBuilder.create();
        b.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                launchCompleteAppointment();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

    }
    private void addListClinic(){
        ListEmployee employeesAdapter = new ListEmployee(ActivitySpecificSearch.this, search_result_clinics);
        clinics.setAdapter(employeesAdapter);
        Util.setDynamicHeight(clinics);
    }

    private void searchByRole(){
        System.out.println("==================== role");

        RadioGroup roles = findViewById(R.id.rg_roles);

        String selected_role="";
        if(roles.getCheckedRadioButtonId()==-1){
            Toast.makeText(ActivitySpecificSearch.this,"please select one role",Toast.LENGTH_LONG).show();
            return;
        }

        if(roles.getCheckedRadioButtonId() == R.id.radioButton9){
            selected_role = "doctor";
        }
        if(roles.getCheckedRadioButtonId() == R.id.radioButton15){
            selected_role = "nurse";
        }
        if(roles.getCheckedRadioButtonId() == R.id.radioButton16){
            selected_role = "staff";
        }
        for (Employee employee: employees){
            if (employee.getRole().equals(selected_role)){
                search_result_clinics.add(employee);
            }
        }

    }

    private void searchByTime(){
        System.out.println("==================== time");
        EditText editHour = findViewById(R.id.editText2);
        EditText editMin = findViewById(R.id.editText12);
        String hour = editHour.getText().toString().trim();
        String min = editMin.getText().toString().trim();


        Boolean flag = true;
        if(TextUtils.isEmpty(hour) || TextUtils.isEmpty(min)){
            flag = false;
            error = "Something is not entered";
        }
        if(!isHourValid(hour) || !isMinValid(min)){
            flag = false;
        }
        if(flag){
//            Toast.makeText(ActivitySpecificSearch.this,"valid",Toast.LENGTH_LONG).show();
            if(hour.length()==1){
                hour = "0"+hour;
            }
            if(min.length()==1){
                min = "0"+min;
            }
            int toCompare = Integer.parseInt(hour+min);

            String wh;
            for(Employee employee:employees){
                wh = employee.getWh().split("@")[1];
                int lowerBound = Integer.parseInt(wh.substring(0,4));
                int upperBound = Integer.parseInt(wh.substring(4,8));
                if(lowerBound < toCompare && toCompare < upperBound){
                    search_result_clinics.add(employee);
                }
            }


        }
        else {
            flag = true;
            Toast.makeText(ActivitySpecificSearch.this,error,Toast.LENGTH_LONG).show();
        }


    }

    private void searchByWorkDay(){
        System.out.println("==================== work day");


        RadioGroup days = findViewById(R.id.rg_weekdays);

        String selected_day="";
        if(days.getCheckedRadioButtonId()==-1){
            Toast.makeText(ActivitySpecificSearch.this,"please select one day",Toast.LENGTH_LONG).show();
            return;
        }

        if(days.getCheckedRadioButtonId() == R.id.radioButton19){
            selected_day = "monday";
        }
        if(days.getCheckedRadioButtonId() == R.id.radioButton20){
            selected_day = "tuesday";
        }
        if(days.getCheckedRadioButtonId() == R.id.radioButton21){
            selected_day = "wednesday";
        }
        if(days.getCheckedRadioButtonId() == R.id.radioButton22){
            selected_day = "thursday";
        }
        if(days.getCheckedRadioButtonId() == R.id.radioButton23){
            selected_day = "friday";
        }
        for (Employee employee : employees){
            List<String> work_days = new ArrayList<>();
            String temp = employee.getWh().split("@")[0];
            for(String s:temp.split(", ")){
                work_days.add(s);
            }
            if (work_days.contains(selected_day)){
                search_result_clinics.add(employee);
            }

        }





    }

    private void searchByName(){
        System.out.println("==================== name");

        EditText et = findViewById(R.id.editText13);
        String name = et.getText().toString().trim();
        Boolean flag= true;
        if(TextUtils.isEmpty(name)){
            flag = false;
            error = "name is empty";
        }
        if(!isClinicNameValid(name)){
            flag = false;
        }

        if(flag){
            for (Employee employee:employees){
                if(employee.getClinicName().contains(name)){
                    search_result_clinics.add(employee);
                }
            }
        }
        else{
            flag = true;
            Toast.makeText(ActivitySpecificSearch.this,error,Toast.LENGTH_LONG).show();
        }

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
    private boolean isClinicNameValid (String name){
        Character ch;
        for (int i = 0; i < name.length(); i++){
            ch = name.charAt(i);
            if (!Character.isLetter(ch))
                if ((!ch.equals(' ')) && (!ch.equals('\''))){
                    error = "Clinic name is not valid. Please make sure that customer name can only contains letter or space or ' ! Please try again.";
                    return false;
                }
        }
        return true;
    }



}
