package com.example.walkinclinics;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAppointment extends ArrayAdapter<Appointment> {
    private Activity context;
    private List<Appointment> appointments;

    public ListAppointment(Activity context, List<Appointment> appointments) {
        super(context, R.layout.appointment_list, appointments);
        this.context = context;
        this.appointments = appointments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.appointment_list, null, true);
        TextView textName = listViewItem.findViewById(R.id.list_appoint_clinicname);
        TextView textService = listViewItem.findViewById(R.id.list_appoint_service_name);
        TextView textDate = listViewItem.findViewById(R.id.list_appoint_date);
        TextView textWait = listViewItem.findViewById(R.id.list_appoint_wait);
        Appointment appointment = appointments.get(position);
        textName.setText(appointment.getEmployee().getClinicName());
        textService.setText(appointment.getService().getName());
        textDate.setText(Util.intDateToString(appointment.getDate()));
        textWait.setText(((appointment.getQueuePosition()-1)*15)+" mins");
        return listViewItem;
    }
}

