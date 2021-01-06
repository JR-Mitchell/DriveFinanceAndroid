package com.jrmitchell.drivefinance.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.utils.CallbackInterface;
import com.jrmitchell.drivefinance.utils.DriveUtils;
import com.jrmitchell.drivefinance.utils.SuccessFailureCallback;
import com.jrmitchell.drivefinance.views.fragments.LoginFragment;

/**
 * Non-inheriting class holding behaviour for the
 * Login flow.
 * Should ultimately be testable, with minimal dependencies
 */
public class Login {

    private void driveServiceInitialised( Fragment fragment) {
        //Ensure that querying can be performed
        DriveUtils.getSingletonInstance().queryFiles(intent -> {
            ActivityResultContract<Intent, ActivityResult> innerContract = new ActivityResultContracts.StartActivityForResult();
            ActivityResultCallback<ActivityResult> innerCallback = innerResult ->
                    NavHostFragment.findNavController(fragment).navigate(R.id.action_loginFragment_to_loggedInFragment);
            fragment.prepareCall(innerContract,innerCallback).launch(intent);
        },"name = 'a' and name = 'b'")
                .addOnSuccessListener(fileList -> NavHostFragment.findNavController(fragment).navigate(R.id.action_loginFragment_to_loggedInFragment))
                .addOnFailureListener(e -> {
                    if (!(e instanceof UserRecoverableAuthIOException)) {
                        NavHostFragment.findNavController(fragment).navigate(R.id.action_loginFragment_to_failedLoginFragment);
                    }
                });
    }

    public void onViewCreated(@NonNull View view, Fragment fragment) {
        //Log in to the google account
        if (DriveUtils.getSingletonInstance().driveServiceNeedsInitialising()) {
            Intent signInIntent = DriveUtils.getSingletonInstance().googleSigninFlow(view.getContext());
            ActivityResultContract<Intent, ActivityResult> contract = new ActivityResultContracts.StartActivityForResult();
            ActivityResultCallback<ActivityResult> callback = result -> DriveUtils.getSingletonInstance().initialiseDriveService(view.getContext(), result.getData(), new SuccessFailureCallback<Void>() {
                @Override
                public void success(Void avoid) {
                    driveServiceInitialised(fragment);
                }

                @Override
                public void failure() {
                    NavHostFragment.findNavController(fragment).navigate(R.id.action_loginFragment_to_failedLoginFragment);
                }
            });
            fragment.prepareCall(contract,callback).launch(signInIntent);
        } else driveServiceInitialised(fragment);
    }
}
