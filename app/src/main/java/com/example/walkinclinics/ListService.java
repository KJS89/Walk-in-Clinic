package com.example.walkinclinics;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListService extends ArrayAdapter<Service> {
    private Activity context;
    private List<Service> services;

    public ListService(Activity context, List<Service> services) {
        super(context, R.layout.service_list, services);
        this.context = context;
        this.services = services;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.service_list, null, true);
        TextView textName = listViewItem.findViewById(R.id.list_service_name);
        TextView textHour = listViewItem.findViewById(R.id.list_service_hourrate);
        TextView textExp = listViewItem.findViewById(R.id.list_service_exprate);

        Service service = services.get(position);
        textName.setText(service.getName());
        textHour.setText(service.getHourlyRate());
        if(service.getExpRate().equals("0")){
            textExp.setText("unrated");
        }
        else{
            textExp.setText(service.getExpRate());
        }

        return listViewItem;
    }
}

