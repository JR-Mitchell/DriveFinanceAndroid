package com.jrmitchell.drivefinance.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.material.navigation.NavigationView;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.DriveScopes;
import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.models.DriveRepo;
import com.jrmitchell.drivefinance.models.Repo;
import com.jrmitchell.drivefinance.models.RepoFactory;
import com.jrmitchell.drivefinance.viewmodels.MainViewModel;
import com.jrmitchell.drivefinance.viewmodels.MainViewModelFactory;

import java.util.Collections;

/**
 * Main activity of the app.
 * Uses the 'activity_main' layout
 * Contains a drawer layout (main_activity_drawer_layout) with a navigation menu.
 * Contains a toolbar (main_activity_toolbar) with
 *  - a menu button (main_activity_menu_button) which opens and closes the drawer
 *  - a textview (main_activity_toolbar_text) which shows the currently open folder
 * Contains a fragment container (main_activity_fragment_container) for inner content
 * Contains a NavigationView (main_activity_navigation_view) which shows the menu
 *
 * Uses the MainViewModel viewmodel
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Basic setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get an instance of the ViewModel and set up the top text
        MainViewModel viewModel = new ViewModelProvider(
                this,
                new MainViewModelFactory(DriveRepo::new)
        ).get(MainViewModel.class);
        setUpTopText(viewModel);

        //Set up the menu button
        setUpMenuButtonOnClick();

        //Set up the menu actions
        setUpMenuSelectionListener();
    }

    /**
     * The default sign in options for Google signin
     *
     * @return the SignInOptions for Google signin
     */
    private GoogleSignInOptions defaultSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();
    }

    /**
     * Sets the OnClickListener for the menu button
     */
    private void setUpMenuButtonOnClick() {
        ImageButton menuButton = findViewById(R.id.main_activity_menu_button);
        menuButton.setOnClickListener(v -> {
            DrawerLayout drawerLayout = findViewById(R.id.main_activity_drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START,true);
        });
    }

    /**
     * Sets the NavigationItemSelectedListener for the menu
     */
    private void setUpMenuSelectionListener() {
        NavigationView navView = findViewById(R.id.main_activity_navigation_view);
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_item_payments) {
                //TODO
            } else if (id == R.id.menu_item_reports) {
                //TODO
            } else {
                //TODO
            }
            return true;
        });
    }

    /**
     * Sets up the toolbar text
     *
     * @param viewModel ViewModel for the main activity
     */
    private void setUpTopText(MainViewModel viewModel) {
        viewModel.getFolderName().observe(this, string -> {
            TextView textView = findViewById(R.id.main_activity_toolbar_text);
            textView.setText(string);
        });
    }
}
