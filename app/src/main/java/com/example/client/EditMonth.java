package com.example.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Month;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditMonth extends AppCompatActivity {

    private DataSnapshot snapshot;
    private Month m1;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_month);

        refresh();



        if(snapshot==null)
        {
            Toast.makeText(EditMonth.this,"It's NULL",Toast.LENGTH_SHORT).show();
        }



        Intent i1 = getIntent();
        m1 = (Month)i1.getSerializableExtra("Month");
        init();


    }

    public void init()
    {
        try {

            //Month m1 = snapshot.child("Months").child(s1).getValue(Month.class);

            //TimeUnit.SECONDS.sleep(5);

            //Month m1 = new Month();

            //Toast.makeText(EditMonth.this,s1,Toast.LENGTH_SHORT).show();

            TextView title = findViewById(R.id.textView4);
            title.setText(m1.getTitle());

            TextView amt1 = findViewById(R.id.amt1);
            amt1.setText(m1.getContr() + "");

            TextView amt2 = findViewById(R.id.amt2);
            amt2.setText(m1.getAmtOb() + "");

            TextView amt3 = findViewById(R.id.amt3);
            amt3.setText(m1.getElectricity() + "");

            TextView amt4 = findViewById(R.id.amt4);
            amt4.setText(m1.getGarbageCollection() + "");

            TextView amt5 = findViewById(R.id.amt5);
            amt5.setText(m1.getGardener() + "");

            TextView amt6 = findViewById(R.id.amt6);
            amt6.setText(m1.getSecurity() + "");

            TextView amt7 = findViewById(R.id.amt7);
            amt7.setText(m1.getSweeper() + "");

            TextView amt8 = findViewById(R.id.amt8);
            amt8.setText(m1.getOther() + "");

            TextView desc = findViewById(R.id.description1);
            desc.setText(m1.getDescription());

            TextView amt9 = findViewById(R.id.amt9);
            amt9.setText(m1.getTotExp() + "");
        }
        catch(Exception e1)
        {
            Toast.makeText(EditMonth.this,e1.toString(),Toast.LENGTH_SHORT).show();
        }



    }

    public void refresh()
    {
        DatabaseReference d1 = FirebaseDatabase.getInstance().getReference();
        d1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                setSnapshot(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setSnapshot(DataSnapshot snapshot)
    {
        this.snapshot = snapshot;
    }
}