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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rateme.maidapp.Adapters.MaidsAdapter;
import com.rateme.maidapp.Adapters.SelectionsAdapter;
import com.rateme.maidapp.Classes.ClientSelect2;
import com.rateme.maidapp.Classes.UserPosts;
import com.rateme.maidapp.R;
import com.rateme.maidapp.editActivity;
import com.squareup.picasso.Picasso;

public class BossSelections extends Fragment {

    FirebaseDatabase rootNode;
    FirebaseAuth mAuth;
    SelectionsAdapter adapter;
    DatabaseReference mbase, mbaseme;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentboss_selections, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.selections);
        rootNode = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton floatingActionButton = view.findViewById(R.id.main_post);
        mbase = FirebaseDatabase.getInstance().getReference("Selections");
        Query query = mbase.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions<ClientSelect2> options = new FirebaseRecyclerOptions.Builder<ClientSelect2>().setQuery(query, ClientSelect2.class).build();
        adapter = new SelectionsAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
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
