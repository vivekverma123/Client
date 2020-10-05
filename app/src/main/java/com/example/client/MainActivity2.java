package com.example.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.FlatInfo;
import com.example.model.Maintenance;
import com.example.model.Month;
import com.example.model.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {



    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        context = MainActivity2.this;

        Toast.makeText(MainActivity2.this, "" + FlatInfo.flatNo,Toast.LENGTH_SHORT).show();

        Button b1 = findViewById(R.id.reqmain);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox1(view);
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




                                    if(amt2 + x > amt1)
                                    {
                                        Toast.makeText(MainActivity2.this,"You cannot pay more than the limit set by the admin, if already paid enter the amount of maintenance remaining only and file advanced/due request for the leftover amount.",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Request r1 = new Request(FlatInfo.flatNo,x,remark,"",false);
                                        DatabaseReference d1 = FirebaseDatabase.getInstance().getReference();
                                        String s1 = d1.child("Requests").push().getKey();
                                        d1.child("Requests").child(s1).setValue(r1);
                                        Toast.makeText(MainActivity2.this,"Request filed successfully",Toast.LENGTH_SHORT).show();
                                    }
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
}