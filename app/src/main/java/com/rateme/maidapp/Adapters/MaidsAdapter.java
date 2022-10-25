package com.rateme.maidapp.Adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.rateme.maidapp.Classes.ClientSelect;
import com.rateme.maidapp.Classes.ClientSelect2;
import com.rateme.maidapp.Classes.UserPosts;
import com.rateme.maidapp.R;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.w3c.dom.Text;

public class MaidsAdapter extends FirebaseRecyclerAdapter<UserPosts, MaidsAdapter.maidsViewholder> {
    public MaidsAdapter(@NonNull FirebaseRecyclerOptions<UserPosts> options) {
        super(options);
    }

    FirebaseAuth mAuth;
    Uri uri;
    Double distanceme;
    LatLng sydney = new LatLng(-34, 151);
    LatLng Brisbane = new LatLng(-27.470125, 153.021072);

    @Override
    protected void onBindViewHolder(@NonNull MaidsAdapter.maidsViewholder holder, int position, @NonNull UserPosts model) {
        holder.mainage.setText(model.getAge() + " " + "Years");
        mAuth = FirebaseAuth.getInstance();
        String keybro = getRef(position).getKey();
        holder.mainexpe.setText(model.getExperience() + " " + "Years");
        holder.mainid.setText(model.getId());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Details").child(keybro);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String stateme = snapshot.child("state").getValue(String.class);
                if (stateme.equals("Selected")) {
                    holder.btnselection.setText("Worker Unavailable");
                    holder.btnselection.setEnabled(false);
                    holder.btnselection.setBackgroundTintList(ColorStateList.valueOf((Color.parseColor("#013220"))));
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.mainname.setText(model.getName());
        holder.kincontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(holder.itemView.getContext());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
                TextView namekin = bottomSheetDialog.findViewById(R.id.namekin);
                TextView contactkin = bottomSheetDialog.findViewById(R.id.contactkin);
                ImageView whatsappkin = bottomSheetDialog.findViewById(R.id.contactwhatsapp);
                ImageView mesagekin = bottomSheetDialog.findViewById(R.id.messagecontact);
                namekin.setText(model.getWitnname());
                contactkin.setText(model.getWitcontact());
                whatsappkin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://api.whatsapp.com/send?phone=" + model.getWitcontact();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(uri.parse(url));
                        holder.itemView.getContext().startActivity(i);
                    }
                });
                mesagekin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(holder.itemView.getContext(), "Not yet available", Toast.LENGTH_SHORT).show();
                    }
                });
                bottomSheetDialog.show();
            }
        });
        DatabaseReference ratingRef = FirebaseDatabase.getInstance().getReference("Details").child(keybro);
        ratingRef.child("Rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
                    holder.ratingBar.setRating(rating);
                    holder.rating.setText(dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cordinato = snapshot.child("coordinates").getValue(String.class);
                if (!snapshot.child("coordinates").exists()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("Alert");
                    builder.setCancelable(false);
                    builder.setMessage("To see distance,configure your location in profile");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    String[] latlong = cordinato.split(",");
                    String[] latlongpers = model.getCoordninates().split(",");
                    double latitude1 = Double.parseDouble(latlong[0]);
                    double longitude1 = Double.parseDouble(latlong[1]);
                    double latitudep = Double.parseDouble(latlongpers[0]);
                    double longitudep = Double.parseDouble(latlongpers[1]);
                    LatLng me = new LatLng(latitude1, longitude1);
                    LatLng person = new LatLng(latitudep, longitudep);
                    distanceme = SphericalUtil.computeDistanceBetween(me, person);
                    String stringdouble = Double.toString(distanceme);
                    holder.distance.setText(String.format("%.2f", distanceme / 1000) + "km" + " " + "away");
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Details").child(keybro);
                    reference1.child("distance").setValue(String.format("%.2f", distanceme / 1000));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
                /*String[] latlong = cordinato.split(",");
                String[] latlongpers = model.getCoordninates().split(",");
                double latitude1 = Double.parseDouble(latlong[0]);
                double longitude1 = Double.parseDouble(latlong[1]);
                double latitudep = Double.parseDouble(latlongpers[0]);
                double longitudep = Double.parseDouble(latlongpers[1]);
                LatLng me = new LatLng(latitude1, longitude1);
                LatLng person = new LatLng(latitudep, longitudep);
                distanceme = SphericalUtil.computeDistanceBetween(me, person);
                String stringdouble = Double.toString(distanceme);*/
        //holder.distance.setText(stringdouble);
        final String userUid = model.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String pictureUrl = snapshot.child("url").getValue(String.class);
                    final String namemain = snapshot.child("username").getValue(String.class);
                    String imgurlbro = snapshot.child("urlimg").getValue(String.class);
                    if (pictureUrl.isEmpty()) {
                        holder.mainimage.setImageResource(R.drawable.user);
                    } else {
                        Picasso.get().load(pictureUrl).into(holder.mainimage);

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=" + model.getPhone();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(uri.parse(url));
                holder.itemView.getContext().startActivity(i);
            }
        });
        holder.btnselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = mAuth.getCurrentUser().getUid();
                String email = mAuth.getCurrentUser().getEmail();
                DatabaseReference mebro = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                mebro.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String namememain = model.getName();
                            String uidme = model.getUid();
                            String phoneme = model.getPhone();
                            String expe = model.getExperience();
                            String Idme = model.getId();
                            String age = model.getAge();
                            String nameme = snapshot.child("username").getValue(String.class);
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Details").child(keybro).child("Selection");
                            ClientSelect clientSelect = new ClientSelect(uid, email, "selected", nameme);
                            reference.child(uid).setValue(clientSelect);
                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Details").child(keybro);
                            databaseReference1.child("state").setValue("Selected");
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Selections");
                            ClientSelect2 clientSelect2 = new ClientSelect2(namememain, uid, phoneme, expe, Idme, age);
                            reference2.child(keybro).setValue(clientSelect2);
                            Toast.makeText(holder.itemView.getContext(), model.getName() + " " + "Selected ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @NonNull
    @Override
    public MaidsAdapter.maidsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maids, parent, false);
        return new MaidsAdapter.maidsViewholder(view);
    }

    public class maidsViewholder extends RecyclerView.ViewHolder {
        TextView mainname, mainid, mainexpe, mainage, distance, rating;
        Button btnselection;
        ImageView whatsapp, mainimage, kincontact;
        RatingBar ratingBar;

        public maidsViewholder(@NonNull View itemView) {
            super(itemView);
            mainage = itemView.findViewById(R.id.mainage);
            kincontact = itemView.findViewById(R.id.kincontact);
            distance = itemView.findViewById(R.id.distance);
            ratingBar = itemView.findViewById(R.id.simpleRatingBar);
            mainimage = itemView.findViewById(R.id.mainimage);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            mainname = itemView.findViewById(R.id.mainname);
            rating = itemView.findViewById(R.id.ratingme);
            btnselection = itemView.findViewById(R.id.btnselection);
            mainid = itemView.findViewById(R.id.mainid);
            mainexpe = itemView.findViewById(R.id.mainexp);
        }
    }
}
