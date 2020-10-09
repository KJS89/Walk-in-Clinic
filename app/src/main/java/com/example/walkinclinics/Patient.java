package com.example.walkinclinics;

public class Patient extends Person{


    public Patient(){}
    public Patient(String id, String username, String pwd, String name, String email, String phoneNumber){
        super(id, username, pwd, email, name, "Patient",phoneNumber);
    }



}
