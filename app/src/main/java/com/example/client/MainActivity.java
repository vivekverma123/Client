package com.example.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.model.FlatInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button b1 = findViewById(R.id.login);
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner.setVisibility(View.VISIBLE);
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        EditText e1 = findViewById(R.id.flatno);
                        EditText e2 = findViewById(R.id.mobno);

                        String s1 = e1.getText().toString();
                        String s2 = e2.getText().toString();

                        if(snapshot.child("FlatOwners").child(s1).exists())
                        {
                            String s3 = snapshot.child("FlatOwners").child(s1).child("mobNo").getValue(String.class);
                            if(s3.equals(s2))
                            {
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                Intent i1 = new Intent(MainActivity.this,MainActivity2.class);
                                startActivity(i1);
                                FlatInfo.flatNo = s1;
                            }
                            else
                            {
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this,"Invalid Phone Number",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this,"Flat number not registered, contact the admin",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}