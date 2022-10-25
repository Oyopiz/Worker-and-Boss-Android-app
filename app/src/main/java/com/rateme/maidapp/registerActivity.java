package com.rateme.maidapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rateme.maidapp.Classes.UserHelper;
import com.shivtechs.maplocationpicker.MapUtility;

import java.util.ArrayList;
import java.util.List;

public class registerActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Spinner mainspinner;
    Uri selectedImage;
    ProgressBar dialog;
    ImageView imageView;
    Button signup;
    EditText email, password, username, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        location = findViewById(R.id.location);
        imageView = findViewById(R.id.imageView);
        signup = findViewById(R.id.continueBtn);
        mainspinner = findViewById(R.id.mainspinner);
        List<String> categories = new ArrayList<String>();
        categories.add("Client");
        categories.add("Worker");
        categories.add("Admin");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mainspinner.setAdapter(dataAdapter);
        dialog = findViewById(R.id.mainprogress);
        email = findViewById(R.id.emailsin);
        password = findViewById(R.id.passsin);
        username = findViewById(R.id.usernamesin);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setVisibility(View.VISIBLE);
                String emailme = email.getText().toString();
                String passwordme = password.getText().toString();
                String userme = username.getText().toString();
                if (selectedImage != null) {
                    if (emailme.isEmpty()) {
                        email.setError("Email");
                        dialog.setVisibility(View.GONE);
                    } else if (passwordme.isEmpty()) {
                        password.setError("Password");
                        dialog.setVisibility(View.GONE);
                    } else if (userme.isEmpty()) {
                        username.setError("Username");
                        dialog.setVisibility(View.GONE);
                    } else if (location.getText().toString().isEmpty()) {
                        location.setError("Location");
                        dialog.setVisibility(View.GONE);
                    } else {
                        String emailmka = email.getText().toString();
                        String usermka = username.getText().toString();
                        DatabaseReference referenceme = FirebaseDatabase.getInstance().getReference("Users");
                        referenceme.orderByChild("username").equalTo(usermka).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    username.setError("Username already taken");
                                    dialog.setVisibility(View.GONE);
                                } else {
                                    registeruser(emailme, passwordme);
                                    dialog.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                } else {
                    if (emailme.isEmpty()) {
                        email.setError("Email");
                        dialog.setVisibility(View.GONE);
                    } else if (passwordme.isEmpty()) {
                        password.setError("Password");
                        dialog.setVisibility(View.GONE);
                    } else if (userme.isEmpty()) {
                        username.setError("Username");
                        dialog.setVisibility(View.GONE);
                    } else if (location.getText().toString().isEmpty()) {
                        location.setError("Location");
                        dialog.setVisibility(View.GONE);
                    } else {
                        String usermka = username.getText().toString();
                        DatabaseReference referenceme = FirebaseDatabase.getInstance().getReference("Users");
                        referenceme.orderByChild("username").equalTo(usermka).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    username.setError("Username already taken");
                                    dialog.setVisibility(View.GONE);
                                } else {
                                    regwithoutImage(emailme, passwordme);
                                    dialog.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });
    }

    private void regwithoutImage(String emailme, String passwordme) {
        auth.createUserWithEmailAndPassword(emailme, passwordme).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String emailme = email.getText().toString();
                String passwordme = password.getText().toString();
                String choice = mainspinner.getSelectedItem().toString();
                String locationme = location.getText().toString();
                String userme = username.getText().toString();
                if (task.isSuccessful()) {
                    dialog.setVisibility(View.GONE);
                    if (choice == "Client") {
                        UserHelper userHelper = new UserHelper("", userme, emailme, locationme, auth.getCurrentUser().getUid(), choice);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(auth.getCurrentUser().getUid()).setValue(userHelper);
                        Toast.makeText(registerActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(registerActivity.this, Boss.class));
                    } else if (choice == "Worker") {
                        UserHelper userHelper = new UserHelper("", userme, emailme, locationme, auth.getCurrentUser().getUid(), choice);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(auth.getCurrentUser().getUid()).setValue(userHelper);
                        Toast.makeText(registerActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(registerActivity.this, homeActivity.class));
                    } else if (choice == "Admin") {
                        UserHelper userHelper = new UserHelper("", userme, emailme, locationme, auth.getCurrentUser().getUid(), choice);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(auth.getCurrentUser().getUid()).setValue(userHelper);
                        Toast.makeText(registerActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(registerActivity.this, adminActivity1.class));
                    }else {
                        Toast.makeText(registerActivity.this, "Cannot get", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialog.setVisibility(View.GONE);
                    Toast.makeText(registerActivity.this, "Registration failed, try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registeruser(String emailme, String passwordme) {
        auth.createUserWithEmailAndPassword(emailme, passwordme).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.setVisibility(View.GONE);
                    uploadImage();
                } else {
                    Toast.makeText(registerActivity.this, "Registration failed, try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImage() {
        storage = FirebaseStorage.getInstance();
        StorageReference reference1 = storage.getReference().child("images").child(auth.getCurrentUser().getUid());
        reference1.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dialog.setVisibility(View.GONE);
                            String emailme = email.getText().toString();
                            String locationme = location.getText().toString();
                            String userme = username.getText().toString();
                            String choice = mainspinner.getSelectedItem().toString();
                            String imageUrl = uri.toString();

                            if (choice == "Client") {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                UserHelper userHelper = new UserHelper(imageUrl, userme, emailme, locationme, auth.getCurrentUser().getUid(), choice);
                                reference.child(auth.getCurrentUser().getUid()).setValue(userHelper);
                                startActivity(new Intent(registerActivity.this, Boss.class));
                            } else if (choice == "Worker") {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                UserHelper userHelper = new UserHelper(imageUrl, userme, emailme, locationme, auth.getCurrentUser().getUid(), choice);
                                reference.child(auth.getCurrentUser().getUid()).setValue(userHelper);
                                startActivity(new Intent(registerActivity.this, homeActivity.class));
                            }
                            else if (choice == "Admin") {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                UserHelper userHelper = new UserHelper(imageUrl, userme, emailme, locationme, auth.getCurrentUser().getUid(), choice);
                                reference.child(auth.getCurrentUser().getUid()).setValue(userHelper);
                                startActivity(new Intent(registerActivity.this, adminActivity1.class));
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.getData() != null) {
                imageView.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }
    }
}