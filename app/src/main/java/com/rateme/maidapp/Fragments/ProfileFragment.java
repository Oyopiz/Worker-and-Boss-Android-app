package com.rateme.maidapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rateme.maidapp.R;
import com.rateme.maidapp.editActivity;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    FirebaseDatabase rootNode;
    FirebaseAuth mAuth;
    DatabaseReference mbase;
    TextView txtshotlists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView email = view.findViewById(R.id.email_get);
        mAuth = FirebaseAuth.getInstance();
        TextView nameme = view.findViewById(R.id.name_get);
        LinearLayout mainl = view.findViewById(R.id.main_layout);
        TextView urlmbro = view.findViewById(R.id.urlme);
        String voke = urlmbro.getText().toString();
        ProgressBar progressBar = view.findViewById(R.id.load_progress);
        ImageView img = view.findViewById(R.id.img_get);
        Button profile = view.findViewById(R.id.edit_profile);
        TextView location = view.findViewById(R.id.location_get);
        rootNode = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        nameme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                progressBar.setVisibility(View.GONE);
                mainl.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), editActivity.class));
            }
        });
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");
        email.setText(mAuth.getCurrentUser().getEmail());
        reference.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    // Log.d("TAG", "PARENT: "+ childDataSnapshot.getKey());
                    String name1 = (String) childDataSnapshot.child("url").getValue();
                    String name = (String) childDataSnapshot.child("username").getValue();
                    String locationme = (String) childDataSnapshot.child("location").getValue();
                    //String idme = (String) childDataSnapshot.child("id").getValue();
                    nameme.setText(name);
                    //getActivity().setTitle(name);
                    urlmbro.setText(name1);
                    if (locationme.isEmpty()) {
                        location.setText("Location not set");
                    } else {
                        location.setText(locationme);
                    }
                    if (name.isEmpty()) {
                        nameme.setText("Name not set");
                    } else {
                        nameme.setText(name);
                    }
                    if (name1.isEmpty()) {
                        img.setImageResource(R.drawable.ic_baseline_person_24);
                    } else {
                        Picasso.get().load(name1).into(img);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void getShortlists() {
        DatabaseReference likereference;
        likereference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Selections");
        likereference.addChildEventListener(new ChildEventListener() {
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
    }
    }
