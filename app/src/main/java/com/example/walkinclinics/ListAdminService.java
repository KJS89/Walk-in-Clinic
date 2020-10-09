package com.example.walkinclinics;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdminService extends ArrayAdapter<Service> {
    private Activity context;
    private List<Service> services;

    public ListAdminService(Activity context, List<Service> services) {
        super(context, R.layout.service_list, services);
        this.context = context;
        this.services = services;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_admin_service, null, true);
        TextView textName = listViewItem.findViewById(R.id.list_admin_servicename);
        TextView textRole = listViewItem.findViewById(R.id.list_service_role);
        Service service = services.get(position);
        textName.setText(service.getName());
        textRole.setText(service.getRole());


        return listViewItem;
    }
}

