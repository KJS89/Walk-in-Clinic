package com.example.walkinclinics;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class ActivityEmployeeSetRate extends AppCompatActivity {
    int text_id = 100;
    int edit_id = 200;
    Button confirm;
    String[] services;
    List<String> rates;
    LinearLayout layout_parent;
    String error;
    Boolean flag = true;
    Util util;
    String op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_rate);
        util = new Util();
        util.retrieveEmployee();
        Bundle b = getIntent().getExtras();
        services = b.getString("services").split(", ");
        op = b.getString("op");
        rates = new ArrayList<>();

        confirm = findViewById(R.id.button7);


        layout_parent = findViewById(R.id.linear_layout_rate);

        LinearLayout.LayoutParams p;
        LinearLayout child_layout;
        TextView textView;
        EditText editText;

        for (String ser:services){
            String service = ser.split(":")[0];
            p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            child_layout = new LinearLayout(this);
            child_layout.setOrientation(LinearLayout.HORIZONTAL);
            child_layout.setLayoutParams(p);

            p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView = new TextView(this);
            p.weight=(float)0.3;
            textView.setLayoutParams(p);
            textView.setId(text_id);
            text_id++;
            textView.setText("Hourly rate for "+service+":  $");
            textView.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
            child_layout.addView(textView);

            editText = new EditText(this);
            p.weight=(float)1.0;
            editText.setLayoutParams(p);
            editText.setId(edit_id);
            editText.setHint("1-100");
            if(op!=null){
                editText.setText(ser.split(":")[1]);
            }
            edit_id++;
            child_layout.addView(editText);

            layout_parent.addView(child_layout);

        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rates.clear();
                String rate;
                EditText et;
                for(int i = 200;i<edit_id;i++){
                    et = findViewById(i);
                    rate = et.getText().toString().trim();
                    if (!(isRateValid(rate))){flag = false;}
                    if (TextUtils.isEmpty(rate)){
                        flag = false;
                        error = "Some rate is missing";
                    }
                    rates.add(rate);
                }
                if(flag){
                    String temp="";
                    for (int i = 0; i<services.length;i++){
                        temp+= services[i].split(":")[0]+":"+rates.get(i)+":"+services[i].split(":")[2]+", ";
                    }
                    Employee employee = util.getEmployee();
                    employee.setServices(temp);
                    util.updateEmployee(employee);

                    if(op!=null){
                        Util.goToEmployee(ActivityEmployeeSetRate.this,util.getId());
                    }
                    else{
                        Util.goToMain(ActivityEmployeeSetRate.this);
                    }

                }
                else{
                    flag = true;
                    Toast.makeText(ActivityEmployeeSetRate.this,error,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean isRateValid(String s){
        Integer integer=null;
        try{
            integer = Integer.parseInt(s);
        }
        catch (Exception e){}
        if(integer == null){
            error = "one of the rate entered is not an integer";
            return false;
        }
        else if(integer<1 || integer>100){
            error = "one of the hour entered is not in the range [1,100]";
            return false;
        }
        else {
            return true;
        }
    }
}
