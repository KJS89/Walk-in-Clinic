package com.example.walkinclinics;

public class Person {

    private String _id;
    private String _className;

    private String _name;
    private String _username;
    private String _pwd;
    private String _email;
    private String _phoneNumber;
    private String _insurance;
    private String _payment;

    public Person(){} // do not delete

    public Person(String id, String username, String pwd, String email, String name, String className,String phoneNumber){
        _pwd = pwd;
        _id = id;
        _username = username;
        _email = email;
        _name = name;
        _className = className;
        _phoneNumber = phoneNumber;
        _insurance="";
        _payment="";
    }

    public void setId(String id) {
        _id = id;
    }
    public String getId() {
        return _id;
    }

    public void setUsername(String username) {
        _username = username;
    }
    public String getUsername() {
        return _username;
    }

    public void setPwd(String pwd) {
        _pwd = pwd;
    }
    public String getPwd() {
        return _pwd;
    }

    public void setEmail(String email) {
        _email = email;
    }
    public String getEmail() {
        return _email;
    }

    public String getClassName(){
        return _className;
    }
    public void setClassName(String className){
        _className = className;
    }

    public void setName(String name) {
        _name = name;
    }
    public String getName() {
        return _name;
    }

    public void setPhoneNumber(String phoneNumber){
        _phoneNumber = phoneNumber;
    }
    public String getPhoneNumber(){
        return _phoneNumber;
    }

    public void setInsurance(String insurance){
        _insurance = insurance;
    }
    public String getInsurance(){
        return _insurance;
    }

    public void setPayment(String payment){
        _payment = payment;
    }
    public String getPayment(){
        return _payment;
    }
    public String toString(){
        return _className+" "+_id;
    }
}
