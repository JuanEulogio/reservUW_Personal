package com.cs407.reservuw;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.cs407.reservuw.databinding.ActivityMainBinding;
import com.cs407.reservuw.databinding.ActivityMainMenuBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

public class MainMenu extends AppCompatActivity {

    //TODO: why is this one right and not "MainActivityBinding"
    ActivityMainMenuBinding binding;

    //TODO: look into why binding is prefered method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId== R.id.favorites){
                //go to favorites pg
                return true;
            } else if (itemId== R.id.reserves) {
                //go to reserve list
                return true;
            }else if(itemId== R.id.logout){
                //go to logout and sign out user
                return true;
            }
            return true;
        });


    }


}
