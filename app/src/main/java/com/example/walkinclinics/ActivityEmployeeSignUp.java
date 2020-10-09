package com.example.walkinclinics;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityEmployeeSignUp extends AppCompatActivity {

    Button confirm;
    RadioGroup radioGroup_role;
    RadioGroup radioGroup_ins;
    EditText textClinicName;
    CheckBox checkBox_credit;
    CheckBox checkBox_debit;
    CheckBox checkBox_cash;

    String role;
    String clinicName;
    String insurance;
    String payments;
    String error;
    Util util;
    Boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeesign);
        util = new Util();
        util.retrieveEmployee();
        confirm = findViewById(R.id.ins_pay_confirm);
        radioGroup_role = findViewById(R.id.rg_employee_role);
        radioGroup_ins = findViewById(R.id.rg_employee_ins);
        textClinicName = findViewById(R.id.editTextClinicName);
        checkBox_credit = findViewById(R.id.cb_credit);
        checkBox_debit = findViewById(R.id.cb_debit);
        checkBox_cash = findViewById(R.id.cb_cash);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected_1 = radioGroup_role.getCheckedRadioButtonId();
                int selected_2 = radioGroup_ins.getCheckedRadioButtonId();

                clinicName = textClinicName.getText().toString().trim();
                if (!isClinicNameValid(clinicName)) { flag = false; }

                if (TextUtils.isEmpty(clinicName) || selected_1==-1 || selected_2==-1 || ( !(checkBox_cash.isChecked())
                && !(checkBox_credit.isChecked())  && !(checkBox_debit.isChecked()) )) {
                    // if text is empty or no selection for role or insurance or all three check box are empty
                    flag = false;
                    error = "Some fields are missing";
                }

                if (flag){
                    if (selected_1==R.id.doctor3){ role = "doctor"; }
                    if (selected_1==R.id.nurse3){ role = "nurse"; }
                    if (selected_1==R.id.staff3){ role = "staff"; }

                    if (selected_2==R.id.cb_employee_ohip){ insurance = "ohip"; }
                    if (selected_2==R.id.cb_employee_uhip){ insurance = "uhip"; }
                    if (selected_2==R.id.cb_employee_private){ insurance = "private insurance"; }
                    if (selected_2==R.id.cb_employee_no){ insurance = "no insurance"; }
                    payments="";
                    if(checkBox_credit.isChecked()){payments+="credit, ";}
                    if(checkBox_debit.isChecked()){payments+="debit, ";}
                    if(checkBox_cash.isChecked()){payments+="cash";}

                    launchSetWh();
                }
                else{
                    flag = true;
                    Toast.makeText(ActivityEmployeeSignUp.this,error,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isClinicNameValid (String name){
        Character ch;
        for (int i = 0; i < name.length(); i++){
            ch = name.charAt(i);
            if (!Character.isLetter(ch))
                if ((!ch.equals(' ')) && (!ch.equals('\''))){
                    error = "Clinic name is not valid. Please make sure that customer name can only contains letter or space or ' ! Please try again.";
                    return false;
                }
        }
        return true;
    }

    private void launchSetWh(){

        Employee employee = util.getEmployee();
        employee.setClinicName(clinicName);
        employee.setInsurance(insurance);
        employee.setPayment(payments);
        employee.setRole(role);
        util.updateEmployee(employee);

        Intent intent = new Intent(this, ActivitySetWh.class);
        intent.putExtra("role",role);
        startActivity(intent);

    }
}