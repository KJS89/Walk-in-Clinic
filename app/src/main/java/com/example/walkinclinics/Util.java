package com.example.walkinclinics;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Util {

    //address format
    // address@latitude, longitude

    // wh format
    // weekday, weekday...@15001700

    public static final String DATABASE_ACCOUNT = "testing/accounts";
    public static final String DATABASE_SERVICE = "testing/services";
    public static final String DATABASE_APPOINMENT = "testing/appointments";

    public static String _id;
    public static Employee temp_employee;
    public static Patient temp_patient;

    private Service _service;
    private Patient _patient;
    private Employee _employee;

    private DatabaseReference database_service;
    private DatabaseReference database_account;
    private DatabaseReference database_appointment;
    private DatabaseReference dR_service;
    private DatabaseReference dR_account;


    public Util(){
        database_account = getAccountDR();
        database_service = getServiceDR();
        database_appointment = getAppointmentDR();

    }
    public void setId(String id){
        _id = id;
    }
    public String getId(){
        return _id;
    }


    public void addPatient(Patient patient){
        _id =database_account.push().getKey();
        patient.setId(_id);
        database_account.child(_id).setValue(patient);
    }
    public void addAppointment(Appointment appoint){
        _id =database_account.push().getKey();
        appoint.setId(_id);
        database_appointment.child(_id).setValue(appoint);
    }
    public void updatePatient(Patient patient){
        database_account.child(_id).setValue(patient);
    }

    public void addEmployee(Employee employee){
        _id =database_account.push().getKey();
        employee.setId(_id);
        database_account.child(_id).setValue(employee);
    }
    public void updateEmployee(Employee employee){
        database_account.child(_id).setValue(employee);
    }


    public void retrieveService(){
        dR_service = FirebaseDatabase.getInstance().getReference(DATABASE_SERVICE + "/"+ _id);
        dR_service.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _service = dataSnapshot.getValue(Service.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});
    }
    public Service getService(){
        return _service;
    }

    public void retrievePatient(){
        dR_account = FirebaseDatabase.getInstance().getReference(DATABASE_ACCOUNT + "/"+ _id);
        dR_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _patient = dataSnapshot.getValue(Patient.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});
    }
    public Patient getPatient(){
        return _patient;
    }

    public void retrieveEmployee(){
        dR_account = FirebaseDatabase.getInstance().getReference(DATABASE_ACCOUNT + "/"+ _id);
        dR_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _employee = dataSnapshot.getValue(Employee.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});
    }
    public Employee getEmployee(){
        return _employee;
    }

    public void hold_patient(Patient patient){temp_patient = patient;}
    public Patient getHold_patient(){return temp_patient;}

    public void hold_employee(Employee employee){temp_employee = employee;}
    public Employee getHold_employee(){return temp_employee;}



    // ========         static methods           ==========//

    public static void serviceInitialize(){
        DatabaseReference database_services = getServiceDR();
        Service default_service;
        default_service = new Service("default_1","regular check ups", "staff");
        database_services.child("default_1").setValue(default_service);
        default_service = new Service("default_2","immunizations", "nurse");
        database_services.child("default_2").setValue(default_service);
        default_service = new Service("default_3","prescriptions renewal", "doctor");
        database_services.child("default_3").setValue(default_service);
        default_service = new Service("default_4","referrals", "doctor");
        database_services.child("default_4").setValue(default_service);
        default_service = new Service("default_5","treatment for minor ailments", "doctor");
        database_services.child("default_5").setValue(default_service);
        default_service = new Service("default_6","lab service", "staff");
        database_services.child("default_6").setValue(default_service);
        default_service = new Service("default_7","health education", "nurse");
        database_services.child("default_7").setValue(default_service);
        default_service = new Service("default_8","home visiting service", "doctor");
        database_services.child("default_8").setValue(default_service);
    }
    public static void adminInitialize(){
        DatabaseReference database = getAccountDR();
        Admin admin = null;
        try{
            admin = new Admin("admin");
        }
        catch (Throwable t){}
        database.child("admin").setValue(admin);
    }
    public static DatabaseReference getAccountDR(){
        return FirebaseDatabase.getInstance().getReference(DATABASE_ACCOUNT);
    }
    public static DatabaseReference getServiceDR(){
        return FirebaseDatabase.getInstance().getReference(DATABASE_SERVICE);
    }
    public static DatabaseReference getAppointmentDR(){
        return FirebaseDatabase.getInstance().getReference(DATABASE_APPOINMENT);
    }
    public static void setDynamicHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) { return; }
        int total = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            total += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams parameters = listView.getLayoutParams();
        parameters.height = total + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(parameters);
    }
    public static void goToMain(Context context){
        Intent intent = new Intent(context, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    public static void goToPatient(Context context, String id){
        Intent intent = new Intent(context, ActivityPatient.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    public static void goToEmployee(Context context, String id){
        Intent intent = new Intent(context, ActivityEmployee.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    public static int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }
    public static int getRelativeBottom(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getBottom();
        else
            return myView.getBottom() + getRelativeTop((View) myView.getParent());
    }



    public static int getCurrentDate(){
        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        return  Integer.parseInt(currentDate);
    }
    public static Calendar getCurrentCalendar(){
        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        int year = Integer.parseInt(currentDate.substring(0,4));
        int month = Integer.parseInt(currentDate.substring(4,6));
        month--;
        int dayOfMonth = Integer.parseInt(currentDate.substring(6,8));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar;
    }
    public static int convertDateToInt( int year, int month, int dayOfMonth){
        String date="";
        date += Integer.toString(year);
        month++;
        if (month<10){
            date+="0"+month;
        }
        else{
            date+=month;
        }
        if(dayOfMonth<10){
            date+="0"+dayOfMonth;
        }
        else {
            date+=dayOfMonth;
        }
        return Integer.parseInt(date);

    }
    public static String intDateToString(int d){
        String toRet;
        String t= Integer.toString(d);
        toRet=t.substring(0,4);
        toRet += " / "+t.substring(4,6);
        toRet += " / "+t.substring(6,8);
        return toRet;
    }
    public static Map<Integer,Integer> convertScheduleToMap(String schedule){
        String[] splited = schedule.split(", ");
        Map<Integer,Integer> map = new HashMap<>();
        if(schedule.length()==0){
            return map;
        }
        for (String s:splited){
            map.put(Integer.parseInt(s.split(":")[0]),Integer.parseInt(s.split(":")[1]));
        }
        return map;
    }
    public static String convertMapToSchedule(Map<Integer,Integer> map){
        String toRet ="";
        for(Map.Entry<Integer,Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            toRet +=Integer.toString(key)+":"+Integer.toString(value)+", ";
        }
        return  toRet;
    }
    public static String updateServiceRate(String services, String target_service, int rate){
        String toRet="";

        for(String s: services.split(", ")){
            String[] temp = s.split(":");
            if (temp[0].equals(target_service)){
                double new_rate = Double.parseDouble(temp[2]);
                if (new_rate==0){
                    new_rate = rate;
                }
                else{
                    new_rate = round((new_rate+rate)/2,1);
                }
                toRet+=temp[0]+":"+temp[1]+":"+new_rate+", ";
            }
            else {
                toRet+=s+", ";
            }
        }
        return toRet;
    }
    public static double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static void updateService(List<Appointment> appointments, List<Employee> employees,
                                     Service new_service, String old_name){
        deleteService(appointments,employees,old_name);
        Util.getServiceDR().child(new_service.getId()).setValue(new_service);


    }
    public static void deleteService(List<Appointment> appointments,List<Employee> employees,String serviceName){
        //  cancel appointment with servicename
        for(Appointment appointment:appointments){
            if(appointment.getService().getName().equals(serviceName)){
                for(Appointment temp_appointment : appointments){
                    if (temp_appointment.getEmployee().getId().equals(appointment.getEmployee().getId()) &&
                            temp_appointment.getDate()==appointment.getDate() ){
                        int queue = temp_appointment.getQueuePosition();
                        queue--;
                        temp_appointment.setQueuePosition(queue);
                        Util.getAppointmentDR().child(temp_appointment.getId()).setValue(temp_appointment);
                    }
                }
                Employee em = appointment.getEmployee();
                Map<Integer,Integer> map =  Util.convertScheduleToMap(em.getSchedule());
                int persons = map.get(appointment.getDate());
                persons--;
                map.put(appointment.getDate(),persons);
                em.setSchedule(Util.convertMapToSchedule(map));
                Util.getAccountDR().child(em.getId()).setValue(em);
                Util.getAppointmentDR().child(appointment.getId()).removeValue();
            }
        }
        // cancel employee selected service with servicename
        for(Employee employee : employees){
            String new_service="";
            String[] services = employee.getServices().split(", ");
            for(String s : services){
                if (s.split(":")[0].equals(serviceName)){;}
                else {
                    new_service+=s+", ";
                }
            }
            employee.setServices(new_service);
            Util.getAccountDR().child(employee.getId()).setValue(employee);
        }


    }

    public static void deleteEmployee(String id,List<Appointment> appointments){
        for(Appointment appointment : appointments){
            if(appointment.getEmployee().getId().equals(id)){
                Util.getAppointmentDR().child(appointment.getId()).removeValue();
            }
        }
        Util.getAccountDR().child(id).removeValue();

    }
    public static void deletePatient(String id,List<Appointment> appointments){
        for(Appointment appointment : appointments){
            if(appointment.getPatient().getId().equals(id)){
                Util.getAppointmentDR().child(appointment.getId()).removeValue();
            }
        }
        Util.getAccountDR().child(id).removeValue();

    }






}
