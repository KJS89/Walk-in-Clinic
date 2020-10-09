package com.example.walkinclinics;

public class Appointment {
    private Employee employee;
    private int date;
    private Patient patient;
    private Service service;
    private int queuePosition;
    private String id;

    public Appointment(){}
    public Appointment(Employee employee, int date,Patient patient, int queuePosition,Service service){
        this.employee = employee;
        this.date = date;
        this.patient = patient;
        this.queuePosition = queuePosition;
        id="";
        this.service =service;
    }


    public void setEmployee(Employee employee){this.employee = employee;}
    public Employee getEmployee(){return employee;}

    public void setDate(int date){this.date = date;}
    public int getDate(){return date;}
    public void setPatient(Patient patient){this.patient = patient;}
    public Patient getPatient(){return patient;}
    public void setQueuePosition(int queuePosition){this.queuePosition = queuePosition;}
    public int getQueuePosition(){return queuePosition;}

    public void setId(String id){this.id = id;}
    public String getId(){return id;}

    public void setService(Service service){this.service = service;}
    public Service getService(){ return service;}

}

