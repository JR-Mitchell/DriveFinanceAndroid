package com.jrmitchell.drivefinance.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.utils.DriveUtils;
import com.jrmitchell.drivefinance.utils.SigninCallback;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    private void driveServiceInitialised() {
        //Ensure that querying can be performed
        DriveUtils.getSingletonInstance().queryFiles(intent -> {
            ActivityResultContract<Intent, ActivityResult> innerContract = new ActivityResultContracts.StartActivityForResult();
            ActivityResultCallback<ActivityResult> innerCallback = innerResult ->
                    NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_loggedInFragment);
            prepareCall(innerContract,innerCallback);
        },"name = 'a' and name = 'b'")
                .addOnSuccessListener(fileList -> NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_loggedInFragment))
                .addOnFailureListener(e -> NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_failedLoginFragment));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Log in to the google account
        if (DriveUtils.getSingletonInstance().driveServiceNeedsInitialising()) {
            Intent signInIntent = DriveUtils.getSingletonInstance().googleSigninFlow(view.getContext());
            ActivityResultContract<Intent, ActivityResult> contract = new ActivityResultContracts.StartActivityForResult();
            ActivityResultCallback<ActivityResult> callback = result -> DriveUtils.getSingletonInstance().initialiseDriveService(view.getContext(), result.getData(), new SigninCallback() {
                @Override
                public void success() {
                    driveServiceInitialised();
                }

                @Override
                public void failure() {
                    NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_failedLoginFragment);
                }
            });
            prepareCall(contract,callback).launch(signInIntent);
        } else driveServiceInitialised();
        super.onViewCreated(view, savedInstanceState);
    }
}