package com.example.model;

import com.example.client.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class UserAdapter3 extends ArrayAdapter<Month>
{

    Context context;
    DataSnapshot snapshot;

    public UserAdapter3(@NonNull Context context, ArrayList<Month> month)
    {
        super(context, 0,month);
        this.context = context;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Month month = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.month_list_item, parent, false);
        }

        TextView t1 = convertView.findViewById(R.id.month_title);
        t1.setText(month.getTitle());

        return convertView;
    }
}