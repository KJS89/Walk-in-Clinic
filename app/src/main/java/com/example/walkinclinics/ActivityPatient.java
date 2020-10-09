package com.example.walkinclinics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ActivityPatient extends AppCompatActivity {
    String id;
    Patient p;
    String insurance;
    String payment;
    List<Appointment> appointments;
    List<Appointment> all_appointments;
    TextView textName;
    TextView textPhone;
    TextView textIns;
    TextView textPayment;

    Button buttonLogout;
    ListView listAppointments;
    Button editInsPay;
    Button buttonBook;
    Button button_checkin;

    DatabaseReference dR;
    String appoint;
    List<Employee> employees;
    List<Patient> patients;
    DatabaseReference database_accounts;
    DatabaseReference database_appointment;
    Util util;
    int rating;
    Employee temp_employee;

    Appointment to_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view);
        util = new Util();
        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        appointments = new ArrayList<>();
        all_appointments = new ArrayList<>();
        database_appointment = Util.getAppointmentDR();
        listAppointments = findViewById(R.id.list_of_appoiuntments);
        dR = FirebaseDatabase.getInstance().getReference(Util.DATABASE_ACCOUNT + "/" + id);
        database_accounts = Util.getAccountDR();
        textName = findViewById(R.id.textNamePatient);
        textPhone = findViewById(R.id.textPhoneNumberPatient);
        textIns = findViewById(R.id.textViewInsurance);
        textPayment = findViewById(R.id.textViewPayment);
        employees = new ArrayList<>();
        patients = new ArrayList<>();
        editInsPay = findViewById(R.id.button3);
        editInsPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchEditInsPay();
            }
        });
        buttonBook = findViewById(R.id.button8);
        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.hold_patient(p);
                launchSearchClinic();
            }
        });



        buttonLogout = findViewById(R.id.button21);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.goToMain(ActivityPatient.this);
            }
        });

        listAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Appointment appointment = appointments.get(position);

                showCheckInDialog(appointment);


            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                p = dataSnapshot.getValue(Patient.class);
                textName.setText("Name: "+p.getName());
                textPhone.setText("Phone Number: "+p.getPhoneNumber());
                textIns.setText("Insurance: "+p.getInsurance());
                textPayment.setText("Payment: "+p.getPayment());
                insurance = p.getInsurance();
                payment = p.getPayment();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

            database_accounts.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    employees.clear();
                    patients.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Person p = postSnapshot.getValue(Person.class);
                        if(p.getClassName().equals("Patient")) {
                            patients.add(postSnapshot.getValue(Patient.class));
                        }
                        else {
                            employees.add(postSnapshot.getValue(Employee.class));
                        }

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            database_appointment.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    appointments.clear();
                    all_appointments.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Appointment appointment = postSnapshot.getValue(Appointment.class);
                        all_appointments.add(appointment);
                        if(appointment.getPatient().getId().equals(id)){

                            appointments.add(appointment);
                        }

                    }

                    ListAppointment adapter = new ListAppointment(ActivityPatient.this, appointments);
                    listAppointments.setAdapter(adapter);
                    Util.setDynamicHeight(listAppointments);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }

    private void showRateExperienceDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_rateexperience, null);
        dialogBuilder.setView(dialogView);
        final Button confirm = dialogView.findViewById(R.id.button11);
        final RatingBar rate = dialogView.findViewById(R.id.ratingBar);
        final AlertDialog b = dialogBuilder.create();
        b.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = (int)rate.getRating();
                if(rating == 0){
                    Toast.makeText(ActivityPatient.this, "cannot rate 0 star",Toast.LENGTH_LONG).show();
                }
                else{
                    b.dismiss();
                    delete_appointment();
                }

            }
        });

    }
    private void showCheckInDialog(final Appointment appointment) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_checkin, null);
        dialogBuilder.setView(dialogView);
        final Button checkin = dialogView.findViewById(R.id.button9);
        final Button cancel = dialogView.findViewById(R.id.button10);
        final AlertDialog b = dialogBuilder.create();
        b.show();
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(appointment.getDate()!=Util.getCurrentDate()){
                    Toast.makeText(ActivityPatient.this,"Cannot checkin as today is "+
                            Util.intDateToString(Util.getCurrentDate()),Toast.LENGTH_LONG).show();
                    b.dismiss();
                }
                else if(appointment.getQueuePosition()!=1){
                    Toast.makeText(ActivityPatient.this,"Cannot checkin as your wait time is not 0 mins",Toast.LENGTH_LONG).show();
                    b.dismiss();
                }
                else {
                    util.setId(appointment.getEmployee().getId());
                    util.retrieveEmployee();
                    to_delete = appointment;
                    b.dismiss();
                    showRateExperienceDialog();


                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();

            }
        });

    }
    private void launchEditInsPay(){
        Intent intent = new Intent(this, ActivityEditInsPay.class);
        util.setId(id);
        startActivity(intent);
    }
    private void launchSearchClinic(){
        Intent intent = new Intent(this, ActivitySearchClinic.class);
        intent.putExtra("payment",payment);
        intent.putExtra("p_id",id);
        startActivity(intent);
    }
    private int getWaitTime(String s){
        String time = s.split(", ")[2].split(" ")[1];
        return Integer.parseInt(time)/15 ;
    }
    private void delete_appointment(){
        temp_employee = util.getEmployee();
        String new_services = Util.updateServiceRate(temp_employee.getServices(), to_delete.getService().getName(),rating);
        temp_employee.setServices(new_services);
        Util.getAccountDR().child(temp_employee.getId()).setValue(temp_employee);
        for(Appointment temp_appointment : all_appointments){
            if (temp_appointment.getEmployee().getId().equals(temp_employee.getId()) &&
                    temp_appointment.getDate()==to_delete.getDate() ){
                int queue = temp_appointment.getQueuePosition();
                queue--;
                temp_appointment.setQueuePosition(queue);
                Util.getAppointmentDR().child(temp_appointment.getId()).setValue(temp_appointment);
            }
        }

        Map<Integer,Integer> map =  Util.convertScheduleToMap(temp_employee.getSchedule());
        int persons = map.get(to_delete.getDate());
        persons--;
        map.put(to_delete.getDate(),persons);
        temp_employee.setSchedule(Util.convertMapToSchedule(map));
        Util.getAccountDR().child(temp_employee.getId()).setValue(temp_employee);

        Util.getAppointmentDR().child(to_delete.getId()).removeValue();





    }
}
