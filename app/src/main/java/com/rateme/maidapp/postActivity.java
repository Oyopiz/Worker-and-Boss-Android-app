package com.rateme.maidapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.rateme.maidapp.Classes.UserPosts;
import com.rateme.maidapp.Classes.UserPosts2;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class postActivity extends AppCompatActivity {

    EditText name, id, age, experience, phoneno, witname, witcontact;
    Button post;
    FirebaseStorage storage;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    int ADDRESS_PICKER_REQUEST = 10;
    TextView txtAddress, txtlatlong;
    Button pick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_post);
        witcontact = findViewById(R.id.witness);
        witname = findViewById(R.id.witnessname);
        MapUtility.apiKey = getResources().getString(R.string.your_api_key);
        experience = findViewById(R.id.experience);
        id = findViewById(R.id.id);
        pick = findViewById(R.id.pick);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(postActivity.this, LocationPickerActivity.class);
                startActivityForResult(i, ADDRESS_PICKER_REQUEST);
            }
        });
        txtAddress = findViewById(R.id.txtAddress);
        txtlatlong = findViewById(R.id.txtLatLong);
        name = findViewById(R.id.name);
        phoneno = findViewById(R.id.phoneno);
        progressBar = findViewById(R.id.progressbro);
        age = findViewById(R.id.age);
        String uid = mAuth.getCurrentUser().getUid();
        post = findViewById(R.id.post_details);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (experience.getText().toString().isEmpty()) {
                    experience.setError("Experience");
                    progressBar.setVisibility(View.GONE);
                } else if (id.getText().toString().isEmpty()) {
                    id.setError("ID");
                    progressBar.setVisibility(View.GONE);
                } else if (age.getText().toString().isEmpty()) {
                    age.setError("Age");
                    progressBar.setVisibility(View.GONE);
                } else if (name.getText().toString().isEmpty()) {
                    name.setError("Name");
                    progressBar.setVisibility(View.GONE);
                } else if (phoneno.getText().toString().isEmpty()) {
                    phoneno.setError("Phone");
                    progressBar.setVisibility(View.GONE);
                } else if (witcontact.getText().toString().isEmpty()) {
                    witcontact.setError("Contact");
                    progressBar.setVisibility(View.GONE);
                } else if (witname.getText().toString().isEmpty()) {
                    witname.setError("Name");
                    progressBar.setVisibility(View.GONE);
                } else if (txtlatlong.getText().toString().isEmpty()) {
                    Toast.makeText(postActivity.this, "Please pick Location", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    upload();
                }
            }
        });
    }

    private void upload() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Details");
        String phone = phoneno.getText().toString();
        String coordninates = txtlatlong.getText().toString();
        UserPosts2 userPosts = new UserPosts2(name.getText().toString(), id.getText().toString(), age.getText().toString(), experience.getText().toString(), mAuth.getCurrentUser().getUid(), phone, coordninates, witname.getText().toString(), witcontact.getText().toString(), "null");
        reference.child(mAuth.getCurrentUser().getUid()).setValue(userPosts);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    Bundle completeAddress = data.getBundleExtra("fullAddress");
                    txtAddress.setText(new StringBuilder().append("addressline2: ").append
                            (completeAddress.getString("addressline2")).append("\ncity: ").append
                            (completeAddress.getString("city")).append("\npostalcode: ").append
                            (completeAddress.getString("postalcode")).append("\nstate: ").append
                            (completeAddress.getString("state")).toString());

                    txtlatlong.setText(new StringBuilder().append("").append(currentLatitude).append
                            (",").append(currentLongitude).toString());

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
