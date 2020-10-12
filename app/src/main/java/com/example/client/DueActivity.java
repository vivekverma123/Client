package com.example.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.GregorianCalendar;

public class DueActivity extends AppCompatActivity {

    private int arr[] = {0,31,28,31,30,31,30,31,31,30,31,30,31};
    private int arr2[] = {0,31,29,31,30,31,30,31,31,30,31,30,31};
    String[] monthName = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    DataSnapshot dataSnapshot;
    Context context;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due);
        context = DueActivity.this;
        init();


    }

    public void init()
    {
        DatabaseReference d1 = FirebaseDatabase.getInstance().getReference();
        d1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                GregorianCalendar Calendar = new GregorianCalendar();
                int curr_month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                int prev_month;
                int prev_year;

                if(curr_month==1)
                {
                    prev_month = 12;
                    prev_year = Calendar.getInstance().get(Calendar.YEAR) - 1;
                }
                else
                {
                    prev_month = curr_month - 1;
                    prev_year = Calendar.getInstance().get(Calendar.YEAR);
                }

                String date;

                if(Calendar.isLeapYear(prev_year))
                {
                    date = monthName[prev_month] + " " + arr2[prev_month] + ", " + prev_year;
                }
                else
                {
                    date = monthName[prev_month] + " " + arr[prev_month] + ", " + prev_year;
                }

                TextView t1 = findViewById(R.id.statement);
                int amt = snapshot.child("TotalDue").child(FlatInfo.flatNo).getValue(Integer.class);
                t1.setText("Total amount due in the society account for your Flat Number " + FlatInfo.flatNo + " till " + date + " is ₹ " + amt );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button b1 = findViewById(R.id.pay_due_1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(DueActivity.this,UploadInvoiceActivity.class);
                i1.putExtra("Type","2");
                startActivity(i1);
            }
        });
    }

    public void setDataSnapshot(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
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

                                    int amt1 = snapshot.child("TotalDue").child(FlatInfo.flatNo).getValue(Integer.class);
                                    String id = snapshot.child("CurrentMonth").getValue(String.class);

                                    if (x <= amt1) {
                                        DatabaseReference d1 = FirebaseDatabase.getInstance().getReference();
                                        String s1 = d1.child("DueRequests").push().getKey();
                                        Request r1 = new Request(FlatInfo.flatNo, x, remark, "", false, s1, id);
                                        d1.child("DueRequests").child(s1).setValue(r1);
                                        Toast.makeText(context, "Request filed successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "You cannot pay more than the due amount, file request under advanced pay", Toast.LENGTH_SHORT).show();
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

}