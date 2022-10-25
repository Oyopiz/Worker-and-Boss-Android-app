package com.rateme.maidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.rateme.maidapp.Fragments.BossHome;
import com.rateme.maidapp.Fragments.BossProfile;
import com.rateme.maidapp.Fragments.BossSelections;
import com.rateme.maidapp.Fragments.ProfileFragment;
import com.rateme.maidapp.Fragments.StatusFragment;

public class Boss extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    private long pressedTime;
    int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_boss);
        loadFragment(new BossHome());
        askPermissions();

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigationboss);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    private void askPermissions() {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_posts:
                fragment = new BossHome();
                break;
            case R.id.selected:
                fragment = new BossSelections();
                break;
            case R.id.navigation_profile:
                fragment = new BossProfile();
                break;

        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}

