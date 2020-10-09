package com.example.walkinclinics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.lang.*;
import java.util.regex.Pattern;


public class ActivitySignUp extends AppCompatActivity {

    EditText editTextName;
    EditText editTextUserId;
    EditText editTextPwd;
    EditText editTextConfirmPwd;
    EditText editTextEmail;
    EditText editTextPhone;

    RadioGroup radioGroup;

    Button buttonConfirm;
    Button buttonCancel;

    Boolean flag = true;
    String error;
    String phone;
    Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editTextName = findViewById(R.id.new_name);
        editTextUserId  = findViewById(R.id.new_user_id);
        editTextPwd  = findViewById(R.id.new_pwd_id);
        editTextConfirmPwd  = findViewById(R.id.confirm_pwd_id);
        editTextEmail = findViewById(R.id.new_email_id);
        editTextPhone = findViewById(R.id.editText);

        buttonConfirm = findViewById(R.id.confirm);
        buttonCancel = findViewById(R.id.buttonSignUpCancel);

        radioGroup = findViewById(R.id.radioRole);

        util = new Util();

        buttonConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*
                Get the string in the editText from program layout.
                And we need to test each field to make sure they are all valid.
                 */
                String name = editTextName.getText().toString().trim();
                if (!isNameValid(name)) { flag = false; }

                phone = editTextPhone.getText().toString().trim();
                if (!isPhoneValid(phone)) { flag = false; }

                String user_id = editTextUserId.getText().toString().trim();
                if (!isIDValid(user_id)) { flag = false; }

                String pwd = editTextPwd.getText().toString().trim();
                String confirm_pwd = editTextConfirmPwd.getText().toString().trim();
                if (pwd.compareTo(confirm_pwd) != 0) {
                    flag = false;
                    error = "Confirm password does not match the original password, please try again.";
                }
                if (pwd.length() < 8 || pwd.length() > 16) {
                    flag = false;
                    error = "The password length should be 8 to 16 characters. Please try again.";
                }
                if (!isPwdValid(pwd)) {
                    flag = false;
                }
                String email = editTextEmail.getText().toString().trim();
                if (!isEmailValid(email)) {
                    flag = false;
                }


                int selectedId = radioGroup.getCheckedRadioButtonId();

                /*
                We need to test to make sure all the blank been filled in order to process the user sign up application.
                */
                if ((TextUtils.isEmpty(name))
                        && (TextUtils.isEmpty(user_id))
                        && (TextUtils.isEmpty(pwd))
                        && (TextUtils.isEmpty(email))
                        && (TextUtils.isEmpty(phone))) {
                    flag = false;
                    error = "All the blank should be filled. Please make sure you fill in all the blank and try again";
                }


                // If there are no error detected, then we can process the user sign up application.
                if (flag) {
                    try{ pwd = SHA256Util.encrypt(pwd); } catch (Throwable t){}

                    if (selectedId == R.id.patient) {
                        Patient patient = new Patient("", user_id, pwd, name, email,phone);
                        util.addPatient(patient);
                        launchInsPay();
                    }
                    if (selectedId == R.id.employee) {
                        Employee employee = new Employee("", user_id, pwd, email, name, phone);
                        util.addEmployee(employee);
                        launchActivitySelectAddress();
                    }
                } else {
                    // If there are any error from user input, program will toast the error message.
                    flag = true;
                    Toast.makeText(ActivitySignUp.this, error, Toast.LENGTH_LONG).show();
                }
            }

        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean isNameValid (String name){
        /*
        Test character at name string place at i, make sure this character is a letter or space.
        All the character should be a letter or a space, symbol or digit should not be accepted.
         */
        Character ch;
        for (int i = 0; i < name.length(); i++){
            ch = name.charAt(i);
            if (!Character.isLetter(ch))
                if (!ch.equals(' ')){
                    error = "Name is not valid. Please make sure that customer name can only contains letter or space! Please try again.";
                    return false;
                }
        }
        return true;
    }
    private boolean isIDValid (String user_id){
        /*
        Test character at user_id string place at i, make sure this character is a letter or a number.
        All the character should be a letter or a number, symbol or space should not be accepted.
         */
        Character ch;
        for (int i = 0; i < user_id.length(); i++){
            ch = user_id.charAt(i);
            if (!Character.isLetter(ch))
                if (!Character.isDigit(ch)) {
                    error = "User ID is not valid. Please make sure that user ID can only contains letters or numbers! Please try again.";
                    return false;
                }
        }
        return true;
    }
    private boolean isPwdValid (String pwd){
        /*
            Create a pwd rules pattern and make sure all the rules can be found in the password.
            If yes, the password is valid.
            If not, the password is not valid. User need to try again.
         */
        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        if (!specailCharPatten.matcher(pwd).find()) {
            error = "Password must have at least one special character! Please try again.";
            return false;
        }

        if (!UpperCasePatten.matcher(pwd).find()) {
            error = "Password must have at least one uppercase character! Please try again.";
            return false;
        }

        if (!lowerCasePatten.matcher(pwd).find()) {
            error = "Password must have at least one lowercase character! Please try again.";
            return false;
        }

        if (!digitCasePatten.matcher(pwd).find()){
            error = "Password must have at least one digit number! Please try again.";
            return false;
        }
        return true; //Yeah! Pwd qualify the pwd rules, we can use it!
    }
    private boolean isEmailValid (String email){
        /*
            In this function, we will test to make sure the email address only have one @ sign in the whole string.
            Also, we will test to make sure there are at least one '.' in the server address (After @ symbol)
         */
        if(TextUtils.isEmpty(email)){
            return false;
        }

        String[] emailByPart = email.split("@");
        if (emailByPart.length>2){
            error = "Wrong email format, please try again. Need contains only one '@' ";
            return false;
        }
        // Test the email server part is valid.
        emailByPart = emailByPart[1].split("\\.");
        if (emailByPart.length<2){
            error = "Wrong email format, please try again. Need contains at least one '.' ";
            return false;
        }
        return true;
    }
    private boolean isPhoneValid (String phone){
        Character ch;
        for (int i = 0; i < phone.length(); i++) {
            ch = phone.charAt(i);
            if (!Character.isDigit(ch)) {
                error = "Phone Number. Please make sure phone number only contains numbers! Please try again.";
                return false;
            }

        }
        if (phone.length()!=10){
            error = "Phone number consists only 10 digits. Please try again.";
            return false;
        }
        return true;
    }

    private void launchInsPay(){
        Intent intent = new Intent(this, ActivityInsurancePayment.class);
        startActivity(intent);
    }
    private void launchActivitySelectAddress() {
        Intent intent = new Intent(this, ActivitySelectAddress.class);
        startActivity(intent);
    }
}



