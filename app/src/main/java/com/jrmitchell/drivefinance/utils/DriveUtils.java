package com.jrmitchell.drivefinance.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.Collections;

/**
 * Singleton class interfacing directly with the Google Drive packages
 * Provides abstracted methods for login and file access
 */
public final class DriveUtils {
    private static DriveUtils SINGLETON_INSTANCE;
    private  DriveUtils() {
    }

    private Drive driveService;
    private boolean isDriveServiceInitialised = false;

    public Drive getDriveService() {
        return driveService;
    }

    public boolean isDriveServiceInitialised() {
        return isDriveServiceInitialised;
    }

    public static DriveUtils getSingletonInstance() {
        if (SINGLETON_INSTANCE == null) SINGLETON_INSTANCE = new DriveUtils();
        return  SINGLETON_INSTANCE;
    }

    public Intent googleSigninFlow(Context context) {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                        .build();
        GoogleSignInClient client = GoogleSignIn.getClient(context, signInOptions);
        return client.getSignInIntent();
    }

    public void initialiseDriveService(Context context, Intent intent, SigninCallback callback) {
        GoogleSignIn.getSignedInAccountFromIntent(intent)
                .addOnSuccessListener(googleAccount -> {
                    Log.d("DriveFinanceDebug","Signed in as " + googleAccount.getEmail());
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    context, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    driveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("Testing Things")
                                    .build();
                    isDriveServiceInitialised = true;
                    callback.success();
                }).addOnFailureListener(error -> {
                    Log.e("DriveFinanceDebug","Unable to sign in!");
                    callback.failure();
        });
    }
}
