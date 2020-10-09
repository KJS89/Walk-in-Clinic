package com.example.walkinclinics;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAccount extends ArrayAdapter<Person> {


    private Activity context;
    private List<Person> persons;

    public ListAccount(Activity context, List<Person> persons) {
        super(context, R.layout.account_list, persons);
        this.context = context;
        this.persons = persons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.account_list, null, true);

        TextView name= listViewItem.findViewById(R.id.textName);
        TextView email = listViewItem.findViewById(R.id.textEmail);
        Person p = persons.get(position);
        name.setText(p.getName());
        email.setText(p.getEmail());
        return listViewItem;
    }
}

