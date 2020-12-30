package com.jrmitchell.drivefinance.activities;

//Basic android imports
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.navigation.NavigationView;
import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.fragments.LoggedInFragment;
import com.jrmitchell.drivefinance.utils.FolderUtils;

//Google API imports


/**
 * Main activity class.
 * Inherits from android AppCompatActivity, and uses android's Fragments system to navigate different tools.
 */
public class FolderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set up the top bar
        ((TextView) findViewById(R.id.topBarText)).setText(FolderUtils.getSingletonInstance().getDriveFolderName());

        //Set up the menu button
        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> {
            DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START,true);
        });

        //Set menu onclicks
        NavigationView navView = findViewById(R.id.navView);
        FragmentContainerView fragView = findViewById(R.id.fragmentContainerView);
        navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_payments:
                    Navigation.findNavController(fragView).navigate(R.id.paymentsFragment);
                    break;
                case R.id.menu_item_reports:
                    Navigation.findNavController(fragView).navigate(R.id.reportFragment);
                    break;
            };
            return true;
        });
    }
}