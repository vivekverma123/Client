package com.example.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.FlatInfo;
import com.example.model.Helper;
import com.example.model.Maintenance;
import com.example.model.Month;
import com.example.model.Request;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        context = MainActivity2.this;

        DatabaseReference d1 = FirebaseDatabase.getInstance().getReference();
        d1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    TextView t1 = findViewById(R.id.textView9);
                    TextView t2 = findViewById(R.id.textView10);
                    TextView t3 = findViewById((R.id.textView11));
                    TextView t4 = findViewById((R.id.textView12));
                    TextView t5 = findViewById((R.id.textView13));
                    TextView t6 = findViewById((R.id.textView14));

                    String date = Helper.getLastDate();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, YYYY");
                    Date date1 = new Date();
                    String curr_date = simpleDateFormat.format(date1);

                    if (dataSnapshot.child("Name").exists() == false) {
                        t1.setText("Name not set");
                    } else {
                        t1.setText(dataSnapshot.child("Name").getValue(String.class));
                    }

                    if (dataSnapshot.child("Address").exists() == false) {
                        t2.setText("Address not set");
                    } else {
                        t2.setText(dataSnapshot.child("Address").getValue(String.class));
                    }

                    String s1 = dataSnapshot.child("FlatOwners").child(FlatInfo.flatNo).child("name").getValue(String.class);
                    TextView t7 = findViewById(R.id.textView15);
                    t7.setText("Welcome " + s1);

                    int amt1 = 0;
                    int amt2 = 0;

                    amt1 = dataSnapshot.child("AdvancedAmount").child(FlatInfo.flatNo).getValue(Integer.class);
                    amt2 = dataSnapshot.child("TotalDue").child(FlatInfo.flatNo).getValue(Integer.class);

                    String id = dataSnapshot.child("CurrentMonth").getValue(String.class);
                    Maintenance m1 = dataSnapshot.child("MaintenanceRecord").child(id).child(FlatInfo.flatNo).getValue(Maintenance.class);

                    amt2 += m1.getContr() - m1.getAmt_paid();

                    t3.setText("Your Total due as on " + curr_date + ": ");
                    t5.setText("Your Advanced Amount held with Society as on  " + curr_date + ": ");

                    t6.setText("₹" + amt1);
                    t4.setText("₹" + amt2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        d1 = FirebaseDatabase.getInstance().getReference();
        d1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Button b1 = findViewById(R.id.reqmain);
        FrameLayout b1 = findViewById(R.id.frame1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialogBox1(view);
                Intent i1 = new Intent(MainActivity2.this,UploadInvoiceActivity.class);
                i1.putExtra("Type","0");
                startActivity(i1);
            }
        });

        //Button b2 = findViewById(R.id.view_soc);
        FrameLayout b2 = findViewById(R.id.frame5);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this,ViewMonths.class));
            }
        });

        //Button b3 = findViewById(R.id.pay_advan);
        FrameLayout b3 = findViewById(R.id.frame2);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(MainActivity2.this,UploadInvoiceActivity.class);
                i1.putExtra("Type","1");
                startActivity(i1);
            }
        });

        //Button b4 = findViewById(R.id.view_main);
        FrameLayout b4 = findViewById(R.id.frame4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this,ViewMain.class));
            }
        });

        //Button b5 = findViewById(R.id.pay_due);
        FrameLayout b5 = findViewById(R.id.frame3);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity2.this,DueActivity.class));
            }
        });

        FrameLayout b6 = findViewById(R.id.frame6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(MainActivity2.this, ViewTransactionsActivity.class));
                }
                catch(Exception e1)
                {
                    Toast.makeText(MainActivity2.this,e1.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void showDialogBox1(View view)
    {

        final View alert_layout = getLayoutInflater().inflate(R.layout.req_dialog,null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("File Request");
        alert.setView(alert_layout);

        alert.setPositiveButton("Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        DatabaseReference d1 = FirebaseDatabase.getInstance().getReference();

                        d1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                try {

                                    EditText e1 = alert_layout.findViewById(R.id.amt_1);
                                    EditText e2 = alert_layout.findViewById(R.id.remark_client);

                                    int x = Integer.parseInt(e1.getText().toString());
                                    String remark = e2.getText().toString();


                                    {
                                        String id = snapshot.child("CurrentMonth").getValue(String.class);
                                        Month m1 = snapshot.child("Months").child(id).getValue(Month.class);
                                        int amt1 = m1.getContr();


                                        Maintenance m2 = snapshot.child("MaintenanceRecord").child(id).child(FlatInfo.flatNo).getValue(Maintenance.class);
                                        int amt2 = m2.getAmt_paid();


                                        if (amt2 + x > amt1) {
                                            Toast.makeText(MainActivity2.this, "You cannot pay more than the limit set by the admin, if already paid enter the amount of maintenance remaining only and file advanced/due request for the leftover amount.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            DatabaseReference d1 = FirebaseDatabase.getInstance().getReference();
                                            String s1 = d1.child("Requests").push().getKey();
                                            Request r1 = new Request(FlatInfo.flatNo, x, remark, "", false, s1, id);
                                            d1.child("Requests").child(s1).setValue(r1);
                                            Toast.makeText(MainActivity2.this, "Request filed successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                catch(Exception e1)
                                {
                                    Toast.makeText(context,e1.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
        );

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();

    }

    public void showDialogBox2(View view)
    {
        final View alert_layout = getLayoutInflater().inflate(R.layout.req_dialog,null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("File Request");
        alert.setView(alert_layout);

        alert.setPositiveButton("Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        DatabaseReference d1 = FirebaseDatabase.getInstance().getReference();

                        d1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                EditText e1 = alert_layout.findViewById(R.id.amt_1);
                                EditText e2 = alert_layout.findViewById(R.id.remark_client);

                                int x = Integer.parseInt(e1.getText().toString());
                                String remark = e2.getText().toString();
                                String id = snapshot.child("CurrentMonth").getValue(String.class);

                                DatabaseReference d1 = FirebaseDatabase.getInstance().getReference();
                                String s1 = d1.child("Requests").push().getKey();
                                Request r1 = new Request(FlatInfo.flatNo,x,remark,"",false,s1,id);
                                d1.child("AdvancePayRequests").child(s1).setValue(r1);
                                Toast.makeText(MainActivity2.this,"Request filed successfully",Toast.LENGTH_SHORT).show();
                            }



                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
        );

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }
}