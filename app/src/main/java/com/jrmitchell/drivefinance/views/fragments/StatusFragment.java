package com.jrmitchell.drivefinance.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.models.DriveRepo;
import com.jrmitchell.drivefinance.viewmodels.MainViewModel;
import com.jrmitchell.drivefinance.viewmodels.MainViewModelFactory;

/**
 * Fragment for relaying the status of the app.
 * Uses the 'status_fragment' layout
 * Contains a TextView (status_fragment_folder_name) which states the current folder name.
 * Contains a TextView (status_fragment_repo_status) which states the repo's current status code.
 * Contains an EditText (status_fragment_input_fname) allowing the user to change the folder name.
 * Contains a Button (status_fragment_set_fname_button) confirming the changed folder name.
 *
 * Uses the MainViewModel viewmodel
 */
public class StatusFragment extends Fragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.status_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            MainViewModel viewModel = new ViewModelProvider(
                    getActivity(),
                    new MainViewModelFactory(DriveRepo::new)
            ).get(MainViewModel.class);

            //Set up the folder name handler
            setUpFolderName(view,viewModel);

            //Set up the repo status handler
            setUpStatusHandler(view,viewModel);

            //Set up the button onclick handler
            setUpButtonOnClickHandler(view,viewModel);
        }
    }

    private void setUpButtonOnClickHandler(@NonNull View view, MainViewModel viewModel) {
        Button setButton = view.findViewById(R.id.status_fragment_set_fname_button);
        setButton.setOnClickListener(v -> {
            EditText editText = view.findViewById(R.id.status_fragment_input_fname);
            String folderName = editText.getText().toString();
            viewModel.setFolderName(folderName);
        });
    }

    private void setUpStatusHandler(@NonNull View view, MainViewModel viewModel) {
        viewModel.getStatusCode().observe(getViewLifecycleOwner(),string->{
            TextView statusHolder = view.findViewById(R.id.status_fragment_repo_status);
            statusHolder.setText(String.format(getString(R.string.status_curr_status_code), string));
        });
    }

    private void setUpFolderName(@NonNull View view, MainViewModel viewModel) {
        viewModel.getFolderName().observe(getViewLifecycleOwner(),string->{
            TextView folderName = view.findViewById(R.id.status_fragment_folder_name);
            folderName.setText(String.format(getString(R.string.status_curr_folder_name), string));
        });
    }
}