package com.rateme.maidapp.Adapters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rateme.maidapp.Classes.ClientSelect2;
import com.rateme.maidapp.Classes.UserPosts;
import com.rateme.maidapp.Payment;
import com.rateme.maidapp.R;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.net.HttpCookie;

public class SelectionsAdapter extends FirebaseRecyclerAdapter<ClientSelect2, SelectionsAdapter.selectionsViewholder> {
    public SelectionsAdapter(@NonNull FirebaseRecyclerOptions<ClientSelect2> options) {
        super(options);
    }

    FirebaseAuth mAuth;

    @Override
    protected void onBindViewHolder(@NonNull selectionsViewholder holder, int position, @NonNull ClientSelect2 model) {
        holder.mainage.setText(model.getAge() + " " + "Years");
        mAuth = FirebaseAuth.getInstance();
        String keybro = getRef(position).getKey();
        holder.mainexpe.setText(model.getExperience() + " " + "Years");
        holder.mainid.setText(model.getId());
        holder.mainname.setText(model.getName());
        final String userUid = model.getUid();
        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = model.getPhone();
                Intent i = new Intent(holder.itemView.getContext(), Payment.class);
                i.putExtra("key", value);
                holder.itemView.getContext().startActivity(i);
                //holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), Payment.class));
            }
        });
        FirebaseDatabase.getInstance().getReference("Users").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final String pictureUrl = snapshot.child("url").getValue(String.class);
                    final String namemain = snapshot.child("username").getValue(String.class);
                    String imgurlbro = snapshot.child("urlimg").getValue(String.class);
                    if (pictureUrl.isEmpty()) {
                        holder.mainimage1.setImageResource(R.drawable.user);
                    } else {
                        Picasso.get().load(pictureUrl).into(holder.mainimage1);

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
                i.setData(Uri.parse(url));
                holder.itemView.getContext().startActivity(i);
            }
        });
        holder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(holder.itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog);
                RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                Button button = dialog.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Details").child(keybro);
                        String rateme = String.valueOf(ratingBar.getRating());
                        databaseReference.child("Rating").setValue(rateme);
                        dialog.dismiss();
                        // String rating=String.valueOf(ratingBar.getRating());
                        Toast.makeText(holder.itemView.getContext(), model.getName() + " " + "Rated", Toast.LENGTH_LONG).show();
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Selections");
                reference.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                            String uid = FirebaseAuth.getInstance().getUid();
                            String name1 = (String) childDataSnapshot.child("uid").getValue();
                            if (keybro == childDataSnapshot.getKey()) {
                                AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(holder.itemView.getContext());
                                builder.setMessage("Cancel" + " " + model.getName() + "'s" + " " + "shortlisting?");
                                builder.setTitle("Confirm");
                                builder.setCancelable(false);
                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        childDataSnapshot.getRef().removeValue();
                                        DatabaseReference referencebro = FirebaseDatabase.getInstance().getReference("Details").child(keybro).child("Selection").child(mAuth.getCurrentUser().getUid());
                                        referencebro.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                snapshot.getRef().removeValue();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        DatabaseReference referencebrome = FirebaseDatabase.getInstance().getReference("Details").child(keybro);
                                        referencebrome.child("state").setValue("null");
                                        Toast.makeText(holder.itemView.getContext(), "Listing modified", Toast.LENGTH_SHORT).show();

                                    }

                                });

                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                //Toast.makeText(v.getContext(), "Error Deleting post", Toast.LENGTH_SHORT).show();
                            }

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
    public selectionsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maidselections, parent, false);
        return new SelectionsAdapter.selectionsViewholder(view);
    }

    public class selectionsViewholder extends RecyclerView.ViewHolder {
        TextView mainname, mainid, mainexpe, mainage;
        ImageView delete, mainimage1, whatsapp, rate;
        Button pay;
        ScaleRatingBar scaleRatingBar;

        public selectionsViewholder(@NonNull View itemView) {
            super(itemView);
            mainimage1 = itemView.findViewById(R.id.mainimage1);
            mainage = itemView.findViewById(R.id.mainage);
            delete = itemView.findViewById(R.id.delete);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            pay = itemView.findViewById(R.id.pay);
            rate = itemView.findViewById(R.id.rate);
            mainname = itemView.findViewById(R.id.mainname);
            mainid = itemView.findViewById(R.id.mainid);
            mainexpe = itemView.findViewById(R.id.mainexp);
        }
    }
}