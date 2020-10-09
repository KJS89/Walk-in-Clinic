package com.example.walkinclinics;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.util.List;

        public class ListEmployee extends ArrayAdapter<Employee> {


            private Activity context;
            private List<Employee> employees;

            public ListEmployee(Activity context, List<Employee> employees) {
                super(context, R.layout.account_list, employees);
                this.context = context;
                this.employees = employees;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = context.getLayoutInflater();
                View listViewItem = inflater.inflate(R.layout.employee_list, null, true);

                TextView name= listViewItem.findViewById(R.id.textNameEmployee);
                TextView email = listViewItem.findViewById(R.id.textEmailEmployee);
                TextView address = listViewItem.findViewById(R.id.textAddressEmployee);
                TextView role = listViewItem.findViewById(R.id.textRole);
                TextView clinicname = listViewItem.findViewById(R.id.textView14);
                TextView wh = listViewItem.findViewById(R.id.textView15);

                Employee p = employees.get(position);
                name.setText(p.getName());
                email.setText(p.getEmail());
                role.setText(p.getRole());
                address.setText(p.getAddress().split("@")[0]);
                clinicname.setText("Clinic name: "+p.getClinicName());
                String builder="";
                builder+=p.getWh().split("@")[0];
                String range = p.getWh().split("@")[1];
                builder += " from "+range.substring(0,2)+":"+range.substring(2,4);
                builder+= " to "+range.substring(4,6)+":"+range.substring(6,8);
                wh.setText(builder);

                return listViewItem;
            }
        }






