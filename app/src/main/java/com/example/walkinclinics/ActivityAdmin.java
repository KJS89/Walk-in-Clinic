package com.example.walkinclinics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ActivityAdmin extends AppCompatActivity {

    List<Service> services;
    List<Employee> employees;
    List<Person> patients;
    List<Appointment> appointments;

    ListView listServices;
    ListView listEmployees;
    ListView listPatients;

    Button logout;
    Button addService;
    String error;

    DatabaseReference databaseServices;
    DatabaseReference databaseAccounts;
    DatabaseReference database_appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        appointments = new ArrayList<>();
        databaseServices = FirebaseDatabase.getInstance().getReference(Util.DATABASE_SERVICE);
        databaseAccounts = FirebaseDatabase.getInstance().getReference(Util.DATABASE_ACCOUNT);
        database_appointments = Util.getAppointmentDR();

        services = new ArrayList<>();
        employees = new ArrayList<>();
        patients = new ArrayList<>();

        listServices = findViewById(R.id.listViewServices);
        listEmployees = findViewById(R.id.listViewEmployee);
        listPatients = findViewById(R.id.listViewPatients);

        logout = findViewById(R.id.buttonLogout);

        addService = findViewById(R.id.buttonAddService);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityAdmin.this, ActivityMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);;
            }
        });

        listPatients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person p = patients.get(position);
                showDeleteDialog(p.getId(),"Patient");

            }
        });

        listEmployees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person p = employees.get(position);
                showDeleteDialog(p.getId(),"Employee");
            }
        });
        listServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Service s = services.get(position);
                if(s.getId().startsWith("default")){
                    Toast.makeText(ActivityAdmin.this,"Cannot edit/delete default services",Toast.LENGTH_LONG).show();
                }else{
                    showDeleteEditDialog(s.getId(), s.getName(),s.getRole());
                }
            }
        });

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddServiceDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                services.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Service s = postSnapshot.getValue(Service.class);
                    services.add(s);
                }
                ListAdminService servicesAdapter = new ListAdminService(ActivityAdmin.this, services);
                listServices.setAdapter(servicesAdapter);
                Util.setDynamicHeight(listServices);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        databaseAccounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patients.clear();
                employees.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Person p = postSnapshot.getValue(Person.class);
                    if (p.getClassName().equals("Employee")) {
                        Employee e = postSnapshot.getValue(Employee.class);
                        employees.add(e);
                    }
                    if (p.getClassName().equals("Patient")) { patients.add(p); }

                }
                ListEmployee employeesAdapter = new ListEmployee(ActivityAdmin.this, employees);
                listEmployees.setAdapter(employeesAdapter);
                Util.setDynamicHeight(listEmployees);

                ListAccount patientsAdapter = new ListAccount(ActivityAdmin.this, patients);
                listPatients.setAdapter(patientsAdapter);
                Util.setDynamicHeight(listPatients);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) { }

        });
        database_appointments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointments.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    appointments.add(postSnapshot.getValue(Appointment.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    private void showDeleteDialog(final String id, final String className) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_delete, null);
        dialogBuilder.setView(dialogView);

        final Button delete = (Button) dialogView.findViewById(R.id.buttonDelete);
        final Button cancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(className.equals("Patient")){
                    Util.deletePatient(id,appointments);
                }
                if(className.equals("Employee")){
                    Util.deleteEmployee(id,appointments);
                }
                b.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
    }

    private void showAddServiceDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_service, null);
        dialogBuilder.setView(dialogView);

        final EditText textService = dialogView.findViewById(R.id.editTextService);
        final RadioGroup employeetype= dialogView.findViewById(R.id.employee_type);
        final Button confirmButton = dialogView.findViewById(R.id.buttonAdd);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serviceName = textService.getText().toString().trim();
                if (isServiceNameValid(serviceName)){
                    int selectedId = employeetype.getCheckedRadioButtonId();
                    if (!TextUtils.isEmpty(serviceName)) {
                        String id = databaseServices.push().getKey();
                        Service s = null;
                        if (selectedId == R.id.doctor){
                            s = new Service(id,serviceName,"doctor");
                        }
                        if (selectedId == R.id.staff){
                            s = new Service(id,serviceName,"staff");
                        }
                        if (selectedId == R.id.nurse){
                            s = new Service(id,serviceName,"nurse");
                        }

                        if (selectedId==-1){
                            Toast.makeText(ActivityAdmin.this, "Missing selection", Toast.LENGTH_LONG).show();
                        }
                        else{
                            databaseServices.child(id).setValue(s);
                            b.dismiss();
                        }

                    }
                }
                else{
                    Toast.makeText(ActivityAdmin.this, error, Toast.LENGTH_LONG).show();
                }
                b.dismiss();


            }
        });
    }

    private void showDeleteEditDialog(final String id, final String serviceName, final String role ) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_edit_delete, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextEditService);
        editTextName.setText(serviceName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteService);


        final RadioButton radio1 = dialogView.findViewById(R.id.radioButton12);
        final RadioButton radio2 = dialogView.findViewById(R.id.radioButton13);
        final RadioButton radio3 = dialogView.findViewById(R.id.radioButton14);

        final RadioGroup radioGroup = dialogView.findViewById(R.id.r3);

        if (role.equals("doctor")){ radio1.setChecked(true); }
        if (role.equals("nurse")){ radio2.setChecked(true); }
        if (role.equals("staff")){ radio3.setChecked(true); }

        dialogBuilder.setTitle(serviceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    String new_role = "";
                    if (selectedId == R.id.radioButton12){ new_role = "doctor"; }
                    if (selectedId == R.id.radioButton13){ new_role = "nurse"; }
                    if (selectedId == R.id.radioButton14){ new_role = "staff"; }
                    Service new_s = new Service(id,name,new_role);
                    Util.updateService(appointments,employees,new_s,serviceName);
                    b.dismiss();

                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Util.deleteService(appointments,employees,serviceName);
                databaseServices.child(id).removeValue();


                    b.dismiss();
            }
        });




    }

    private boolean isServiceNameValid(String name){
        /*
        Test character at name string place at i, make sure this character is a letter or space.
        All the character should be a letter or a space, symbol or digit should not be accepted.
         */
        Character ch;
        for (int i = 0; i < name.length(); i++){
            ch = name.charAt(i);
            if (!Character.isLetter(ch) && !Character.isSpaceChar(ch)){
                error = "Name does not valid. Please make sure that customer name can only contains letter or spaces! Please try again.";
                return false;
            }

        }
        return true;
    }










}