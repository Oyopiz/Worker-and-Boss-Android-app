package com.rateme.maidapp.Adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rateme.maidapp.Classes.UserPosts;
import com.rateme.maidapp.Classes.Users;
import com.rateme.maidapp.R;

public class UsersAdapter extends FirebaseRecyclerAdapter<Users, UsersAdapter.maidsViewholder> {
    public UsersAdapter(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UsersAdapter.maidsViewholder holder, int position, @NonNull Users model) {
        String keybro = getRef(position).getKey();
        holder.name.setText(model.getName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Delete User?");
                builder.setIcon(R.drawable.launcher);
                builder.setTitle("ALERT");
                builder.setCancelable(true);
                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Details").child(keybro);
                        databaseReference.removeValue();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @NonNull
    @Override
    public UsersAdapter.maidsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users, parent, false);
        return new UsersAdapter.maidsViewholder(view);
    }

    public class maidsViewholder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView delete;

        public maidsViewholder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            name = itemView.findViewById(R.id.nameuser);
        }
    }
}
