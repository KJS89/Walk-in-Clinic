package com.example.walkinclinics;

import java.security.NoSuchAlgorithmException;

public class Admin extends Person{

    public Admin(String id)throws NoSuchAlgorithmException {
        super(id, "admin", SHA256Util.encrypt("5T5ptQ"),"abs@uottawa.ca", "admin", "Admin","");
    }

}
