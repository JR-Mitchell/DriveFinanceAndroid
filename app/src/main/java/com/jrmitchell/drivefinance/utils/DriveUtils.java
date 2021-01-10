package com.jrmitchell.drivefinance.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    private final Executor executor = Executors.newSingleThreadExecutor();

    public boolean driveServiceNeedsInitialising() {
        return !isDriveServiceInitialised;
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

    public void initialiseDriveService(Context context, Intent intent, SuccessFailureCallback<Void> callback) {
        GoogleSignIn.getSignedInAccountFromIntent(intent)
                .addOnSuccessListener(googleAccount -> {
                    Log.d("DriveFinanceDebug","Signed in as " + googleAccount.getEmail());
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    context, Collections.singleton(DriveScopes.DRIVE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    driveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("DriveFinance")
                                    .build();
                    isDriveServiceInitialised = true;
                    callback.success(null);
                }).addOnFailureListener(error -> {
                    Log.e("DriveFinanceDebug","Unable to sign in!");
                    callback.failure();
        });
    }

    public Task<FileList> queryFiles(CallbackInterface<Intent> callback, String queryString) {
        return Tasks.call(executor,()-> {
                try {
                    return driveService.files()
                            .list()
                            .setQ(queryString)
                            .setFields("files(name,id,capabilities)")
                            .setSpaces("drive")
                            .execute();
                } catch (UserRecoverableAuthIOException e) {
                    callback.run(e.getIntent());
                }
                return driveService.files()
                    .list()
                    .setQ(queryString)
                    .setSpaces("drive")
                    .execute();
                }
        );
    }

    public Task<String> getFileTextData(String fileId) {
        return Tasks.call(executor,()->{
            OutputStream outputStream = new OutputStream() {
                private final StringBuilder stringBuilder = new StringBuilder();

                @Override
                public void write(int b) {
                    this.stringBuilder.append((char) b);
                }

                public String toString() {
                    return this.stringBuilder.toString();
                }
            };
            driveService.files().export(fileId,"text/plain").executeMediaAndDownloadTo(outputStream);
            return outputStream.toString();
        });
    }

    public Task<Void> setFileTextData(String fileId, String newData) {
        return Tasks.call(executor,()->{
            InputStream stream = new ByteArrayInputStream(newData.getBytes());
            driveService.files().update(fileId,new File(),new InputStreamContent("text/plain",stream)).execute();
            return null;
        });
    }

}
