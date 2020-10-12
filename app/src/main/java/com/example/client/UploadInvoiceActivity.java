package com.example.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.model.FlatInfo;
import com.example.model.Month;
import com.example.model.Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class UploadInvoiceActivity extends AppCompatActivity {

    private Button btnChoose, btnUpload;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_invoice);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnChoose = (Button) findViewById(R.id.choose);
        btnUpload = (Button) findViewById(R.id.upload);
        imageView = (ImageView) findViewById(R.id.imageView);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Picasso.with(this).load(filePath).resize(300, 300).into(imageView);
        }
    }

    private void uploadImage() {

        try {
            if (filePath != null) {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        Intent i1 = getIntent();
                        String type = i1.getStringExtra("Type");

                        String child_id;

                        DatabaseReference d1 = FirebaseDatabase.getInstance().getReference();

                        if (type.equals("0")) {
                            child_id = "Requests";
                        } else if (type.equals("1")) {
                           child_id = "AdvancePayRequests";
                        } else {
                           child_id = "DueRequests";
                        }



                        String id = snapshot.child("CurrentMonth").getValue(String.class);

                        EditText e1 = findViewById(R.id.amt_1);
                        EditText e2 = findViewById(R.id.remark_client);

                        int x = Integer.parseInt(e1.getText().toString());

                        String req_id = d1.child(child_id).push().getKey();

                        Request r1 = new Request(FlatInfo.flatNo, x, e2.getText().toString(), "", false, req_id, id);

                        int flag = 0;
                        for(DataSnapshot dataSnapshot : snapshot.child(child_id).getChildren())
                        {
                            Request request = dataSnapshot.getValue(Request.class);
                            if(request.getFlatNo().equals(FlatInfo.flatNo) && request.getStatus()==false)
                            {
                                ++flag;
                                break;
                            }
                        }

                        if(flag==1)
                        {
                            Toast.makeText(UploadInvoiceActivity.this,"You already have a pending request in this section",Toast.LENGTH_LONG).show();
                        }
                        else {

                            final ProgressDialog progressDialog = new ProgressDialog(UploadInvoiceActivity.this);
                            progressDialog.setTitle("Uploading...");
                            progressDialog.show();

                            try {
                                d1.child(child_id).child(req_id).setValue(r1);
                            } catch (Exception e) {
                                Toast.makeText(UploadInvoiceActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }

                            StorageReference ref = storageReference.child("images/" + r1.getInvoice_id());
                            ref.putFile(filePath)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            progressDialog.dismiss();
                                            Toast.makeText(UploadInvoiceActivity.this, "Request filed successfully.", Toast.LENGTH_SHORT).show();
                                            back();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(UploadInvoiceActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            back();
                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                                    .getTotalByteCount());
                                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
        catch(Exception e1)
        {
            Toast.makeText(UploadInvoiceActivity.this,e1.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void back()
    {
        super.onBackPressed();
    }
}