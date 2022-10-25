package com.rateme.maidapp.Fragments;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.service.controls.actions.FloatAction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rateme.maidapp.Adapters.SelectionsAdapter;
import com.rateme.maidapp.Adapters.ShortlistsAdapter;
import com.rateme.maidapp.Classes.ClientSelect2;
import com.rateme.maidapp.Classes.Shortlist;
import com.rateme.maidapp.MainActivity;
import com.rateme.maidapp.R;
import com.rateme.maidapp.Services.MyFirebaseMessagingService;
import com.rateme.maidapp.editActivity;
import com.rateme.maidapp.homeActivity;
import com.rateme.maidapp.postActivity;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

public class StatusFragment extends Fragment {
    FirebaseDatabase rootNode;
    FirebaseAuth mAuth;
    DatabaseReference mbase;
    ImageView imageView;
    LinearLayout linearLayout;
    TextView mainname, mainexp, mainage, mainid, txtrequest;
    ProgressBar progressBar;
    RecyclerView txtshortlist;
    FloatingActionButton floatingActionButton;
    ShortlistsAdapter adapter;
    RatingBar simpleRatingBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
        floatingActionButton = view.findViewById(R.id.main_post);
        mAuth = FirebaseAuth.getInstance();
        simpleRatingBar = view.findViewById(R.id.simpleRatingBar);
        checkstatus();
        checkRequests();
        RecyclerView recyclerView = view.findViewById(R.id.shortlists);
        rootNode = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton floatingActionButton = view.findViewById(R.id.main_post);
        mbase = FirebaseDatabase.getInstance().getReference("Details").child(mAuth.getCurrentUser().getUid()).child("Selection");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions<Shortlist> options = new FirebaseRecyclerOptions.Builder<Shortlist>().setQuery(mbase, Shortlist.class).build();
        adapter = new ShortlistsAdapter(options);
        recyclerView.setAdapter(adapter);
        // DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Details").child(mAuth.getCurrentUser().getUid()).child("Selection");
        mbase.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Vibrator v = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();
                Intent intent = new Intent(view.getContext(), homeActivity.class);
                String CHANNEL_ID = "MYCHANNEL";
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_LOW);
                PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), 1, intent, 0);
                Notification notification = new Notification.Builder(view.getContext(), CHANNEL_ID)
                        .setContentText("New shortlisting!")
                        .setContentTitle("You have new shortlistings!")
                        .setContentIntent(pendingIntent)
                        .addAction(R.drawable.notify, "OPEN", pendingIntent)
                        .setChannelId(CHANNEL_ID)
                        .setSmallIcon(R.drawable.mesaages)
                        .build();
                NotificationManager notificationManager = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);
                notificationManager.notify(1, notification);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                }
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
        adapter.notifyDataSetChanged();
        adapter.startListening();
        mainage = view.findViewById(R.id.mainage);
        linearLayout = view.findViewById(R.id.cardview);
        imageView = view.findViewById(R.id.mainimage);
        txtshortlist = view.findViewById(R.id.shortlists);
        mainname = view.findViewById(R.id.mainname);
        mainexp = view.findViewById(R.id.mainexp);
        mainid = view.findViewById(R.id.mainid);
        txtrequest = view.findViewById(R.id.txtbro);
        progressBar = view.findViewById(R.id.progressmain);
        mainname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtshortlist.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                txtrequest.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), postActivity.class));
            }
        });
        return view;
    }

    private void checkRequests() {
        DatabaseReference referencemain = FirebaseDatabase.getInstance().getReference("Details").child(mAuth.getCurrentUser().getUid()).child("Selection");
        referencemain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //txtrequest.setText("Available shortlistings");
                } else {
                    txtrequest.setText("No shortlistings yet!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkstatus() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Details");
        reference.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    retriieve();
                    retrieveurl();
                } else {
                    txtshortlist.setVisibility(View.VISIBLE);
                    txtrequest.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("You haven't posted any details yet. Click OK");
                    builder.setTitle("Empty");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getContext(), postActivity.class));
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrieveurl() {
        DatabaseReference referenceme = FirebaseDatabase.getInstance().getReference("Users");
        referenceme.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    String urlme = (String) childDataSnapshot.child("url").getValue();
                    if (urlme.isEmpty()) {
                        imageView.setImageResource(R.drawable.ic_baseline_person_24);
                    } else {
                        Picasso.get().load(urlme).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retriieve() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Details");
        reference.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    String ageme = (String) childDataSnapshot.child("age").getValue();
                    String nameme = (String) childDataSnapshot.child("name").getValue();
                    String idme = (String) childDataSnapshot.child("id").getValue();
                    String expe = (String) childDataSnapshot.child("experience").getValue();
                    String rateme = (String) childDataSnapshot.child("Rating").getValue();
                    if (childDataSnapshot.child("Rating").exists()) {
                        float rating = Float.parseFloat(rateme);
                        simpleRatingBar.setRating(rating);
                    } else {

                    }
                    mainage.setText(ageme + " " + "Years");
                    mainexp.setText(expe + " " + "Years");
                    mainid.setText(idme);
                    mainname.setText(nameme);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
