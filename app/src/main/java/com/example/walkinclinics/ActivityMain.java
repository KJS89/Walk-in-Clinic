package com.example.walkinclinics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    List<Person> persons;
    Button signup_button;
    Button login_button;
    EditText editTextUserId;
    EditText editTextPwd;
    DatabaseReference database_account;

    Appointment appointment;

    Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        util = new Util();
        signup_button = findViewById(R.id.signup);
        login_button = findViewById(R.id.login);
        editTextUserId  = findViewById(R.id.user_id);
        editTextPwd  = findViewById(R.id.pwd_id);
        database_account = Util.getAccountDR();

        Util.serviceInitialize();
        Util.adminInitialize();
        persons = new ArrayList<>();

        signup_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchSignUp();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Person p;
                boolean flag_show = true;
                String username = editTextUserId.getText().toString().trim();
                String pwd = editTextPwd.getText().toString().trim();
                try{pwd = SHA256Util.encrypt(pwd); } catch (Throwable t) {}
                if ( (! TextUtils.isEmpty(username)) && (! TextUtils.isEmpty(pwd))  )
                {
                    for(int i = 0; i<persons.size(); i++)
                    {
                        p = persons.get(i);
                        if ( username.equals(p.getUsername()) && pwd.equals(p.getPwd() ) )
                        {
                            flag_show = false;
                            if (p.getClassName().equals("Admin")){
                                launchAdmin();
                            }
                            if (p.getClassName().equals("Employee")){
                                launchEmployee(p.getId());
                            }
                            if (p.getClassName().equals("Patient")){
                                launchPatient(p.getId());
                            }
                        }
                    }
                    if (flag_show){
                        Toast.makeText(ActivityMain.this,"log in failed",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        database_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                persons.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Person p = postSnapshot.getValue(Person.class);
                    persons.add(p);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }


    private void launchAdmin() {
        Intent intent = new Intent(this, ActivityAdmin.class);
        startActivity(intent);
    }
    private void launchPatient(String id) {
        Intent intent = new Intent(this,ActivityPatient.class);
        util.setId(id);
        intent.putExtra("id",id);
        startActivity(intent);
    }
    private void launchEmployee(String id) {
        Intent intent = new Intent(this, ActivityEmployee.class);
        util.setId(id);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    private void launchSignUp() {
        Intent intent = new Intent(this, ActivitySignUp.class);
        startActivity(intent);
    }

}