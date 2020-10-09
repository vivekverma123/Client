package com.example.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.model.FlatInfo;
import com.example.model.Maintenance;
import com.example.model.Month;
import com.example.model.UserAdapter3;
import com.example.model.UserAdapter4;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewMain extends AppCompatActivity {

    ListView l1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_main);



            l1 = findViewById(R.id.list_main);



            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference ownerRef = myRef.child("MaintenanceRecord");

            ownerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ArrayList<Maintenance> f1 = new ArrayList<>();
                    for (DataSnapshot d1 : dataSnapshot.getChildren())
                    {
                        //Toast.makeText(ViewMain.this,FlatInfo.flatNo,Toast.LENGTH_SHORT).show();
                        for(DataSnapshot d2 : d1.getChildren())
                        {
                            Maintenance maintenance = d2.getValue(Maintenance.class);
                            //Toast.makeText(ViewMain.this,FlatInfo.flatNo,Toast.LENGTH_SHORT).show();
                            if(maintenance.getFlatNumber().equals(FlatInfo.flatNo)) {
                                f1.add(maintenance);
                            }
                        }
                    }
                    UserAdapter4 u1 = new UserAdapter4(ViewMain.this, f1);
                    l1.setAdapter(u1);
                }


                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }
