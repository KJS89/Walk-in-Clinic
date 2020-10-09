package com.example.walkinclinics;

import android.text.Editable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Service {

    private String _name;
    private String _id;
    private String _role;
    private boolean sIsValid = false;
    private String _hourlyRate;
    private String _expRate;






    public Service(){}
    public Service(String id, String name, String role){
        _hourlyRate = "";
        _expRate = "";
        _name = name;
        _id = id;
        _role = role;
    }

    public static final Pattern SERVICE_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}");

    public boolean isValid() {
        return sIsValid;
    }

    public static boolean isValidService(CharSequence service) {
        return service != null && SERVICE_PATTERN.matcher(service).matches();
    }

    final public void afterTextChanged(Editable editableText) {
        sIsValid = isValidService(editableText);
    }
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {/*No-op*/}
    final public void onTextChanged(CharSequence s, int start, int before, int count) {/*No-op*/}


    public void setName(String name){
        _name = name;
    }
    public String getName(){
        return _name;
    }


    public void setId(String id){
        _id = id;
    }

    public String getId(){
        return _id;
    }

    public void setRole(String role){
        _role = role;
    }
    public String getRole(){
        return _role;
    }


    public void setHourlyRate(String hourlyRate){_hourlyRate = hourlyRate;}
    public String getHourlyRate(){return _hourlyRate;}

    public void setExpRate(String expRate){_expRate = expRate;}
    public String getExpRate(){return _expRate;}


    public void setSIsValid(boolean v){
        sIsValid = v;
    }



    public String toString(){
        return "Service: "+_name+_id+_role;
    }

}
