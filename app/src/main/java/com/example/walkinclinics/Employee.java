package com.example.walkinclinics;

public class Employee extends Person {

    private String _address;
    private String _role;
    private String _wh;
    private String _services;
    private String _clinicName;
    private String _schedule;
    public Employee(){}
    public Employee(String id, String username, String pwd, String email, String name,String phoneNumber){
        super(id, username, pwd, email, name, "Employee",phoneNumber);
        _role = "";
        _wh = "";
        _services = "";
        _address = "";
        _clinicName ="";
        _schedule = "";
    }


    public void setAddress(String address){
        _address = address;
    }
    public String getAddress(){
        return _address;
    }

    public void setServices(String services){
        _services = services;
    }
    public String getServices(){
        return _services;
    }

    public void setRole(String role){
        _role = role;
    }
    public String getRole(){
        return _role;
    }

    public void setWh(String wh){
        _wh = wh;
    }
    public String getWh(){
        return _wh;
    }

    public void setClinicName(String clinicName){
        _clinicName = clinicName;
    }
    public String getClinicName(){
        return _clinicName;
    }

    public void setSchedule(String schedule){_schedule = schedule;}
    public String getSchedule(){return _schedule;}

}
