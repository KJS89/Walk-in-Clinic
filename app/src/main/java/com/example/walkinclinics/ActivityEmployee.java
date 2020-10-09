package com.example.walkinclinics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ActivityEmployee extends AppCompatActivity {

    TextView textName;
    TextView textRole;
    TextView textPhone;
    TextView textAddress;
    TextView textClinicName;
    TextView textGenWh;
    TextView textMonTW;
    TextView textThursF;
    TextView textIns;
    TextView textPay;

    Button buttonLogout;
    Button editWh;
    Button editServices;


    Employee p;
    String id;
    String wh;

    List<Service> services;
    ListView listServices;

    DatabaseReference databaseAccounts;
    DatabaseReference databaseServices;
    DatabaseReference dR;

    Util util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_view);
        util = new Util();
        util.retrieveEmployee();
        services = new ArrayList<>();
        Bundle b = getIntent().getExtras();
        id = b.getString("id");

        textName = findViewById(R.id.textViewEmployeeName);
        textRole = findViewById(R.id.textViewRoleName);
        textPhone = findViewById(R.id.textViewEmployeePh);
        textAddress = findViewById(R.id.textEmployeeAdd);
        textClinicName = findViewById(R.id.employee_Clinic_name);
        textGenWh = findViewById(R.id.textViewGeneralWH);
        textMonTW = findViewById(R.id.textViewMonTueWed);
        textThursF = findViewById(R.id.textViewThursFri);
        textIns = findViewById(R.id.textViewEmIns);
        textPay = findViewById(R.id.textViewEmPay);

        editWh = findViewById(R.id.buttonEditWH);
        editServices = findViewById(R.id.buttonEmEditService);
        buttonLogout = findViewById(R.id.Goback_button);
        dR = FirebaseDatabase.getInstance().getReference(Util.DATABASE_ACCOUNT+"/"+id);
        databaseAccounts = FirebaseDatabase.getInstance().getReference(Util.DATABASE_ACCOUNT);
        databaseServices = FirebaseDatabase.getInstance().getReference(Util.DATABASE_SERVICE);


        listServices = findViewById(R.id.employee_service_list);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Util.goToMain(ActivityEmployee.this);
            }
        });

        editWh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWh();
            }
        });

        listServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Service s = services.get(position);
                showDeleteDialog(s.getName());

            }
        });

        editServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               util.setId(id);
                launchSelectService();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                p = dataSnapshot.getValue(Employee.class);
                textName.setText(p.getName());
                textRole.setText(p.getRole());
                textPhone.setText(p.getPhoneNumber());
                textAddress.setText(p.getAddress().split("@")[0]);
                textClinicName.setText(p.getClinicName());
                wh = p.getWh();
                String[] description = getDescription(wh);
                textGenWh.setText(description[0]);
                textMonTW .setText(description[1]);
                textThursF.setText(description[2]);
                textIns.setText("Insurance accepted:  "+p.getInsurance());
                String temp = "";
                temp = p.getPayment();
                if(p.getPayment().endsWith(", ")){
                    temp = temp.substring(0,temp.length()-2);
                }
                textPay.setText("Payment accepted:  "+temp);

                services.clear();
                addList(p.getServices());
                ListService servicesAdapter = new ListService(ActivityEmployee.this, services);
                listServices.setAdapter(servicesAdapter);
                Util.setDynamicHeight(listServices);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }



    private void addList(String s){
        services.clear();
        if (s.length()==0){
            return;
        }
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
        List<String> weekdays = new ArrayList<>();
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

    private void showDeleteDialog(final String selected) {
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

                String builder = "";
                Employee e = util.getEmployee();
                String[] service = p.getServices().split(", ");
                for (String s : service){
                    if( !s.contains(selected)){
                        builder += s + ", ";
                    }
                }
                e.setServices(builder);
                util.updateEmployee(e);
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
    private void launchSelectService(){
        Intent intent = new Intent(this, ActivitySelectService.class);
        intent.putExtra("op", "1");
        intent.putExtra("service",p.getServices());
        intent.putExtra("role",p.getRole());
        startActivity(intent);
    }
    private void launchWh(){
        Intent intent = new Intent(this, ActivitySetWh.class);
        intent.putExtra("op", "0");
        intent.putExtra("wh",wh);
        startActivity(intent);
    }
}
