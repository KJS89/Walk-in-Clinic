package com.example.walkinclinics;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActivityCompleteAppointment extends AppCompatActivity {
    Button buttonBook;
    String id;
    Button back;
    ListView availableServices;
    TextView textClinicName;
    TextView textRole;
    TextView textIns;
    TextView textPay;
    TextView textGenWh;
    TextView textMonTW;
    TextView textThursF;
    TextView textService;
    TextView textWaitShow;

    List<Service> services;
    List<String> weekdays;
    DatabaseReference dR;
    Employee employee;
    String wh;
    Service selectedService;
    String clinicName;

    Util util;

    int selected_date = 0;
    Calendar selected_calendar;
    Map<Integer,Integer> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_appointment);
        util = new Util();
        employee = util.getHold_employee();
        id = util.getHold_patient().getId();

        CalendarView calendarView = findViewById(R.id.calendar);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selected_date = Util.convertDateToInt(year,month,dayOfMonth);
                selected_calendar = Calendar.getInstance();
                selected_calendar.set(year, month, dayOfMonth);
                if (map.containsKey(selected_date)){
                    textWaitShow.setText("Time need to wait: "+(map.get(selected_date)*15)+" mins");
                }
                else{
                    textWaitShow.setText("Time need to wait: 0 mins");
                }

            }
        });

        dR = FirebaseDatabase.getInstance().getReference(Util.DATABASE_ACCOUNT+"/"+id);
        textClinicName = findViewById(R.id.textView52);
        textRole = findViewById(R.id.textView53);
        textIns = findViewById(R.id.textView54);
        textPay = findViewById(R.id.textView61);
        textGenWh = findViewById(R.id.textView30);
        textMonTW = findViewById(R.id.textView31);
        textThursF = findViewById(R.id.textView32);
        textService = findViewById(R.id.textView64);
        buttonBook = findViewById(R.id.button26);
        back = findViewById(R.id.button27);
        textWaitShow = findViewById(R.id.textView12);

        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dateIsValid()){
                    if(selectedService==null){
                        Toast.makeText(ActivityCompleteAppointment.this,"please select one service",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Patient patient = util.getHold_patient();
                        if(selected_date==0){
                            selected_date = Util.getCurrentDate();
                        }
                        Integer value;
                        if (map.containsKey(selected_date)){
                            value = map.get(selected_date);
                            value++;
                            map.put(selected_date,value);
                        }
                        else{
                            value = 1;
                            map.put(selected_date,value);
                        }
                        employee.setSchedule(Util.convertMapToSchedule(map));

                        Util.getAccountDR().child(employee.getId()).setValue(employee);

                        Appointment appointment = new Appointment(employee, selected_date, patient, value,selectedService);
                        util.addAppointment(appointment);
                        Util.goToPatient(ActivityCompleteAppointment.this,id);
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        availableServices = findViewById(R.id.listAvaliableService);
        services = new ArrayList<>();
        availableServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Service s = services.get(position);
                textService.setText("You have selected the service: "+s.getName());
                selectedService = s;
            }
        });
        updateInfo();




    }
    @Override
    protected void onStart() {
        super.onStart();

        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                employee = util.getHold_employee();
                map = Util.convertScheduleToMap(employee.getSchedule());
                textRole.setText("Role: "+ employee.getRole());
                textClinicName.setText("Clinic name: "+ employee.getClinicName());
                clinicName = employee.getClinicName();
                wh = employee.getWh();
                String[] description = getDescription(wh);
                textGenWh.setText(description[0]);
                textMonTW .setText(description[1]);
                textThursF.setText(description[2]);
                textIns.setText("Insurance accepted:  "+ employee.getInsurance());
                String temp = "";
                temp = employee.getPayment();
                if(employee.getPayment().endsWith(", ")){

                    temp = temp.substring(0,temp.length()-2);
                }
                textPay.setText("Payment accepted:  "+temp);

                services.clear();
                addList(employee.getServices());
                ListService servicesAdapter = new ListService(ActivityCompleteAppointment.this, services);
                availableServices.setAdapter(servicesAdapter);
                Util.setDynamicHeight(availableServices);
                if (map.containsKey(Util.getCurrentDate())){
                    textWaitShow.setText("Time need to wait: "+(map.get(Util.getCurrentDate())*15)+" mins");
                }
                else{
                    textWaitShow.setText("Time need to wait: 0 mins");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }
    private String[] getDescription(String wh){
        String[] toRet = new String[3];
        String[] splited = wh.split("@");
        String temp = "";
        temp+="Schedule: from "+splited[1].substring(0,2)+":"+splited[1].substring(2,4);
        temp+=" to "+splited[1].substring(4,6)+
                ":"+splited[1].substring(6,8);
        toRet[0]=temp;
        temp="";
        splited = splited[0].split(", ");
        weekdays = new ArrayList<>();
        for (String s: splited){
            weekdays.add(s);
        }
        if (weekdays.contains("monday")){ temp+="Monday works"; }
        else{ temp+="Monday rests"; }
        if (weekdays.contains("tuesday")){ temp+=", Tuesday works"; }
        else{ temp+=", Tuesday rests"; }
        if (weekdays.contains("wednesday")){ temp+=", Wednesday works."; }
        else{ temp+=", Wednesday rests."; }
        toRet[1]=temp;
        temp="";
        if (weekdays.contains("thursday")){ temp+="Thursday works."; }
        else{ temp+="Thursday rests."; }
        if (weekdays.contains("friday")){ temp+=", Friday works."; }
        else{ temp+=", Friday rests."; }
        toRet[2]=temp;
        return toRet;

    }
    private void addList(String s){
        services.clear();
        String[] splited = s.split(", ");
        Service ser;
        for(String temp : splited){
            String[] splited_by_semi = temp.split(":");
            ser = new Service("",splited_by_semi[0],"");
            ser.setHourlyRate(splited_by_semi[1]);
            ser.setExpRate(splited_by_semi[2]);
            services.add(ser);
        }
    }

    private void updateInfo(){
        employee = util.getHold_employee();
        employee = util.getHold_employee();
        textRole.setText("Role: "+ employee.getRole());
        textClinicName.setText("Clinic name: "+ employee.getClinicName());
        clinicName = employee.getClinicName();
        wh = employee.getWh();
        String[] description = getDescription(wh);
        textGenWh.setText(description[0]);
        textMonTW .setText(description[1]);
        textThursF.setText(description[2]);
        textIns.setText("Insurance accepted:  "+ employee.getInsurance());
        String temp = "";
        temp = employee.getPayment();
        if(employee.getPayment().endsWith(", ")){

            temp = temp.substring(0,temp.length()-2);
        }
        textPay.setText("Payment accepted:  "+temp);
    }

    private boolean dateIsValid(){


        if (selected_date==0){
            selected_calendar = Util.getCurrentCalendar();
            selected_date = Util.getCurrentDate();
        }

        if (selected_date< Util.getCurrentDate()){
            Toast.makeText(ActivityCompleteAppointment.this,"Cannot select past days",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        int dayOfWeek = selected_calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek){
            case Calendar.MONDAY:
                if (!weekdays.contains("monday")){
                    Toast.makeText(ActivityCompleteAppointment.this,"This clinic rests on Monday, please change",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                break;
            case Calendar.TUESDAY:
                if (!weekdays.contains("tuesday")){
                    Toast.makeText(ActivityCompleteAppointment.this,"This clinic rests on Tuesday, please change",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                break;
            case Calendar.WEDNESDAY:
                if (!weekdays.contains("wednesday")){
                    Toast.makeText(ActivityCompleteAppointment.this,"This clinic rests on Wednesday, please change",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                break;
            case Calendar.THURSDAY:
                if (!weekdays.contains("thursday")){
                    Toast.makeText(ActivityCompleteAppointment.this,"This clinic rests on Thursday, please change",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                break;
            case Calendar.FRIDAY:
                if (!weekdays.contains("friday")){
                    Toast.makeText(ActivityCompleteAppointment.this,"This clinic rests on Friday, please change",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                break;
            case Calendar.SATURDAY:
                    Toast.makeText(ActivityCompleteAppointment.this,"This clinic rests on Saturday, please change",
                            Toast.LENGTH_LONG).show();
                    return false;

            case Calendar.SUNDAY:
                    Toast.makeText(ActivityCompleteAppointment.this,"This clinic rests on Sunday, please change",
                            Toast.LENGTH_LONG).show();
                    return false;

        }
        return true;


    }


}
