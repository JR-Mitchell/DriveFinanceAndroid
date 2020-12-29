package com.jrmitchell.drivefinance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.utils.FolderUtils;
import com.jrmitchell.drivefinance.utils.SigninCallback;

public class LoggedInFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.logged_in_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Hide all but progress bar
        view.findViewById(R.id.logged_in_textview).setVisibility(View.GONE);
        view.findViewById(R.id.edittext_foldername).setVisibility(View.GONE);
        view.findViewById(R.id.foldername_button).setVisibility(View.GONE);
        if (FolderUtils.getSingletonInstance().driveServiceNeedsInitialising()) {
            //Redirect to login
            NavHostFragment.findNavController(LoggedInFragment.this).navigate(R.id.action_loggedInFragment_to_loginFragment);
        } else FolderUtils.getSingletonInstance().checkDriveFolderNeedsSelecting(this, new SigninCallback() {
            @Override
            public void success() {
                //Redirect to folder
                NavHostFragment.findNavController(LoggedInFragment.this).navigate(R.id.action_loggedInFragment_to_folderLoadedFragment);
            }

            @Override
            public void failure() {
                //Hide progress bar, show other elements
                view.findViewById(R.id.folder_progress_bar).setVisibility(View.GONE);
                view.findViewById(R.id.logged_in_textview).setVisibility(View.VISIBLE);
                view.findViewById(R.id.edittext_foldername).setVisibility(View.VISIBLE);
                view.findViewById(R.id.foldername_button).setVisibility(View.VISIBLE);
                //Set up button
                view.findViewById(R.id.foldername_button).setOnClickListener(v -> {
                    view.findViewById(R.id.folder_progress_bar).setVisibility(View.VISIBLE);
                    String inputText = ((EditText) view.findViewById(R.id.edittext_foldername)).getText().toString();
                    FolderUtils.getSingletonInstance().selectDriveFolder(inputText, LoggedInFragment.this, new SigninCallback() {
                        @Override
                        public void success() {
                            //Redirect to folder
                            NavHostFragment.findNavController(LoggedInFragment.this).navigate(R.id.action_loggedInFragment_to_folderLoadedFragment);
                        }

                        @Override
                        public void failure() {
                            //Redirect to login
                            NavHostFragment.findNavController(LoggedInFragment.this).navigate(R.id.action_loggedInFragment_to_loginFragment);
                        }
                    });
                });
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}