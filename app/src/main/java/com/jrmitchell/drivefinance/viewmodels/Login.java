package com.jrmitchell.drivefinance.viewmodels;

import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.jrmitchell.drivefinance.utils.DriveUtils;
import com.jrmitchell.drivefinance.utils.FragmentWrapper;
import com.jrmitchell.drivefinance.utils.SuccessFailureCallback;

/**
 * Non-inheriting class holding behaviour for the
 * Login flow.
 * Should ultimately be testable, with minimal dependencies
 */
public class Login {

    private void driveServiceInitialised( FragmentWrapper fragmentWrapper) {
        //Ensure that querying can be performed
        DriveUtils.getSingletonInstance().queryFiles(intent -> {
            ActivityResultContract<Intent, ActivityResult> innerContract = new ActivityResultContracts.StartActivityForResult();
            ActivityResultCallback<ActivityResult> innerCallback = innerResult ->
                    fragmentWrapper.navigate("success");
            fragmentWrapper.launchCall(innerContract,innerCallback,intent);
        },"name = 'a' and name = 'b'")
                .addOnSuccessListener(fileList -> fragmentWrapper.navigate("success"))
                .addOnFailureListener(e -> {
                    if (!(e instanceof UserRecoverableAuthIOException)) {
                        fragmentWrapper.navigate("failure");
                    }
                });
    }

    public void onViewCreated(@NonNull View view, FragmentWrapper fragmentWrapper) {
        //Log in to the google account
        if (DriveUtils.getSingletonInstance().driveServiceNeedsInitialising()) {
            Intent signInIntent = DriveUtils.getSingletonInstance().googleSigninFlow(view.getContext());
            ActivityResultContract<Intent, ActivityResult> contract = new ActivityResultContracts.StartActivityForResult();
            ActivityResultCallback<ActivityResult> callback = result -> DriveUtils.getSingletonInstance().initialiseDriveService(view.getContext(), result.getData(), new SuccessFailureCallback<Void>() {
                @Override
                public void success(Void avoid) {
                    driveServiceInitialised(fragmentWrapper);
                }

                @Override
                public void failure() {
                    fragmentWrapper.navigate("failure");
                }
            });
            fragmentWrapper.launchCall(contract,callback,signInIntent);
        } else driveServiceInitialised(fragmentWrapper);
    }
}
