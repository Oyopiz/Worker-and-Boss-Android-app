package com.rateme.maidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class checkrole extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_checkrole);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    // Log.d("TAG", "PARENT: "+ childDataSnapshot.getKey());
                    String role = (String) childDataSnapshot.child("role").getValue();
                    if (role.equals("Worker")){
                        startActivity(new Intent(checkrole.this, homeActivity.class));
                    } else if (role.equals("Client")) {
                        startActivity(new Intent(checkrole.this, Boss.class));
                    }else if (role.equals("Admin")) {
                        startActivity(new Intent(checkrole.this, adminActivity1.class));
                    } else {
                        Toast.makeText(checkrole.this, "An error occured", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}