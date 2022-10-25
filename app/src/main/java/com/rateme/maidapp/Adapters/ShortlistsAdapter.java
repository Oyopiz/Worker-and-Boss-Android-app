package com.rateme.maidapp.Adapters;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rateme.maidapp.Classes.ClientSelect2;
import com.rateme.maidapp.Classes.Shortlist;
import com.rateme.maidapp.R;
import com.rateme.maidapp.postActivity;
import com.squareup.picasso.Picasso;

public class ShortlistsAdapter extends FirebaseRecyclerAdapter<Shortlist, ShortlistsAdapter.shortlistViewholder> {
    public ShortlistsAdapter(@NonNull FirebaseRecyclerOptions<Shortlist> options) {
        super(options);
    }

    FirebaseAuth mAuth;
    @Override
    protected void onBindViewHolder(@NonNull ShortlistsAdapter.shortlistViewholder holder, int position, @NonNull Shortlist model) {
        mAuth=FirebaseAuth.getInstance();
        holder.name.setText(model.getName());
        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + model.getEmail()));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Maid Services");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Maid services");
                holder.itemView.getContext().startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
            }
        });
        String keybro = getRef(position).getKey();


    }
        @NonNull
    @Override
    public ShortlistsAdapter.shortlistViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shortlist, parent, false);
        return new ShortlistsAdapter.shortlistViewholder(view);
    }

    public class shortlistViewholder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView contact, imageclient;

        public shortlistViewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameclient);
            imageclient = itemView.findViewById(R.id.imgclient);
            contact = itemView.findViewById(R.id.contact_client);
        }
    }
}
