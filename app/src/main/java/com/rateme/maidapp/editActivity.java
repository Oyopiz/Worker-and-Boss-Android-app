package com.rateme.maidapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import java.io.IOException;
import java.util.UUID;

public class editActivity extends AppCompatActivity {
    EditText name, location;
    Button save;
    ImageView imgme;
    StorageReference storageReference;
    FirebaseDatabase rootNode, rootNode1;
    DatabaseReference reference, reference1;
    FirebaseAuth mAuth;
    ProgressBar progress;
    TextView txturl, txtremove;
    LinearLayout minl2;
    FirebaseStorage storage;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_edit);
        name = findViewById(R.id.name_set);
        txtremove = findViewById(R.id.txtremove);
        progress = findViewById(R.id.progress);
        location = findViewById(R.id.location_set);
        imgme = findViewById(R.id.img_set);
        txturl = findViewById(R.id.urlstore);
        imgme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chooseImage();
            }
        });
        minl2 = findViewById(R.id.mainl2);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                progress.setVisibility(View.GONE);
                minl2.setVisibility(View.VISIBLE);

            }
        });
        rootNode = FirebaseDatabase.getInstance();
        rootNode1 = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");
        reference1 = rootNode.getReference("Users").child(mAuth.getUid());
        reference.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    // Log.d("TAG", "PARENT: "+ childDataSnapshot.getKey());
                    String name1 = (String) childDataSnapshot.child("url").getValue();
                    String namefirst = (String) childDataSnapshot.child("username").getValue();
                    String locationbro = (String) childDataSnapshot.child("location").getValue();
                    //String idme = (String) childDataSnapshot.child("id").getValue();
                    //getActivity().setTitle(name);
                    txtremove.setText(name1);
                    name.setText(namefirst);
                    location.setText(locationbro);
                    if (name1.isEmpty()) {
                        //Drawable res = getResources().getDrawable(R.drawable.person);
                        imgme.setImageResource(R.drawable.ic_baseline_person_24);
                    } else {
                        Picasso.get().load(name1).into(imgme);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        save = findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = name.getText().toString();
                String locationme = location.getText().toString();
                reference1.child("username").setValue(fname);
                reference1.child("location").setValue(locationme);
                Toast.makeText(editActivity.this, "Done", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(editActivity.this, homeActivity.class));
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void profile(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Change profile photo");
        builder.setTitle("Profile Photo");
        builder.setCancelable(false);
        builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                reference1.child("url").setValue("");
                FirebaseStorage mFirebaseStorage;
                mFirebaseStorage = FirebaseStorage.getInstance();
                StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(txtremove.getText().toString());
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        Log.d(TAG, "onSuccess: deleted file");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                        Log.d(TAG, "onFailure: did not delete file");
                    }
                });
                Toast.makeText(editActivity.this, "Profile Image deleted", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(editActivity.this, homeActivity.class));
            }
        });
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                chooseImage();
            }
        });
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK &&
                data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgme.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save Image?");
        builder.setTitle("Save");
        builder.setCancelable(false);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (filePath != null) {

                    // Code for showing progressDialog while uploading
                    ProgressDialog progressDialog = new ProgressDialog(editActivity.this);
                    progressDialog.setTitle("Updating...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    // Defining the child of storageReference
                    storage = FirebaseStorage.getInstance();
                    storageReference = storage.getReference();
                    StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FirebaseStorage mFirebaseStorage;
                                    mFirebaseStorage = FirebaseStorage.getInstance();
                                    if (txtremove.getText().toString().isEmpty()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(editActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(editActivity.this, homeActivity.class));
                                        txturl.setText(uri.toString());
                                        String uid = FirebaseAuth.getInstance().getUid();
                                        String urlme = txturl.getText().toString();
                                        reference1.child("url").setValue(urlme);
                                        setProfileImage();
                                        finish();
                                        startActivity(new Intent(editActivity.this, homeActivity.class));
                                    } else {
                                        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(txtremove.getText().toString());
                                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // File deleted successfully
                                                Log.d(TAG, "onSuccess: deleted file");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Uh-oh, an error occurred!
                                                Log.d(TAG, "onFailure: did not delete file");
                                            }
                                        });
                                        progressDialog.dismiss();
                                        Toast.makeText(editActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(editActivity.this, homeActivity.class));
                                        txturl.setText(uri.toString());
                                        String uid = FirebaseAuth.getInstance().getUid();
                                        String urlme = txturl.getText().toString();
                                        reference1.child("url").setValue(urlme);
                                        setProfileImage();
                                        finish();
                                        startActivity(new Intent(editActivity.this, homeActivity.class));
                                    }

                                }
                                //mDownloadUrl = uri.toString();

                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(editActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("progress " + (int) progress + "%");
                        }
                    });
                }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setProfileImage() {
        Picasso.get().load(txturl.getText().toString()).into(imgme);
    }

}
