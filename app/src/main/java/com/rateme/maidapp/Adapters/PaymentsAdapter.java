package com.rateme.maidapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.rateme.maidapp.Classes.Payments;
import com.rateme.maidapp.Classes.UserPosts;
import com.rateme.maidapp.R;

public class PaymentsAdapter extends FirebaseRecyclerAdapter<Payments, PaymentsAdapter.paymentsViewholder> {
    public PaymentsAdapter(@NonNull FirebaseRecyclerOptions<Payments> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PaymentsAdapter.paymentsViewholder holder, int position, @NonNull Payments model) {
        holder.sender.setText(model.getSender());
        holder.receiver.setText(model.getSender());
        holder.receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @NonNull
    @Override
    public PaymentsAdapter.paymentsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payments, parent, false);
        return new PaymentsAdapter.paymentsViewholder(view);
    }

    public class paymentsViewholder extends RecyclerView.ViewHolder {
        TextView sender, receiver;
        ImageView receipt;
        public paymentsViewholder(@NonNull View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.senderphone);
            receiver = itemView.findViewById(R.id.receiverphone);
            receipt = itemView.findViewById(R.id.receipt);
        }
    }
}