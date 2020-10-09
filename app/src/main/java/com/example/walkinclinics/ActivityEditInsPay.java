package com.example.walkinclinics;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityEditInsPay extends AppCompatActivity {

    RadioGroup radioInsurance;
    RadioGroup radioPayment;
    Button buttonConfirm;
    Util util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_pay);

        util = new Util();
        util.retrievePatient();

        radioInsurance = findViewById(R.id.rg_insurance);
        radioPayment = findViewById(R.id.rg_payment);
        buttonConfirm = findViewById(R.id.ins_pay_confirm);


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selected_ins_id = radioInsurance.getCheckedRadioButtonId();
                int selected_pay_id = radioPayment.getCheckedRadioButtonId();
                if (selected_ins_id == -1 || selected_pay_id == -1) {
                    Toast.makeText(ActivityEditInsPay.this, "missing selection", Toast.LENGTH_LONG).show();
                }
                else {
                    String insurance = "";
                    String payment = "";

                    if (selected_ins_id == R.id.rb_OHIP) { insurance = "OHIP"; }
                    if (selected_ins_id == R.id.rb_UHIP) { insurance = "UHIP"; }
                    if (selected_ins_id == R.id.rb_private_ins) { insurance = "Private Insurance"; }
                    if (selected_ins_id == R.id.rb_no_ins) { insurance = "No Insurance"; }

                    if (selected_pay_id == R.id.rb_credit) { payment = "credit"; }
                    if (selected_pay_id == R.id.rb_debit) { payment = "debit"; }
                    if (selected_pay_id == R.id.rb_cash) { payment = "cash"; }

                    Patient p = util.getPatient();
                    p.setInsurance(insurance);
                    p.setPayment(payment);
                    util.updatePatient(p);
                    finish();
                }
            }
        });
    }


}
