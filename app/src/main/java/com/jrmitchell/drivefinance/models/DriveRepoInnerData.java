package com.jrmitchell.drivefinance.models;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class DriveRepoInnerData implements Repo.InnerData {


    protected final String TAG = DriveRepoInnerData.class.getSimpleName();

    protected Intent recoverableAuthIntent;

    protected final MutableLiveData<String> status = new MutableLiveData<>("DriveRepoSupplyClient");

    @Override
    public LiveData<String> getStatusCode() {
        return status;
    }

    //Variables needed for setup

    protected Drive driveService;
    private final GoogleSignInOptions signInOptions =
            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                    .build();
    private GoogleSignInAccount account;
    private GoogleSignInClient client;
    private GoogleAccountCredential credential;

    //Methods for Google drive setup

    /**
     * Get the GoogleSignInOptions for the app
     *
     * @return the sign in options
     */
    public GoogleSignInOptions getSignInOptions() {
        Log.i(TAG,"Provided default GoogleSignInOptions");
        return signInOptions;
    }

    /**
     * Given a GoogleSignInClient from the active context, attempts to sign in with Google account.
     *
     * @param client the GoogleSignInClient for active context
     */
    public void setClient(GoogleSignInClient client) {
        this.client = client;
        Log.i(TAG,"Received GoogleSignInClient from activity");
        attemptSilentSignIn();
    }

    /**
     * If this DriveRepoInnerClass has a valid GoogleSignInClient client, attempts to get a
     * GoogleSignInAccount using silent sign-in. If this fails, it will update the status string
     * such that the current activity runs the client's sign-in Intent.
     */
    private void attemptSilentSignIn() {
        if (client != null) {
            status.setValue("DriveRepoAttemptSilentSignIn");
            Task<GoogleSignInAccount> task = client.silentSignIn();
            if (task.isSuccessful()) {
                GoogleSignInAccount taskAccount = task.getResult();
                if (taskAccount != null) {
                    setGoogleSignInAccount(taskAccount);
                } else {
                    status.setValue("DriveRepoRunClientIntent");
                }
            }
            else task.addOnCompleteListener(completeTask -> {
                try {
                    GoogleSignInAccount taskAccount = task.getResult(ApiException.class);
                    if (taskAccount != null) {
                        setGoogleSignInAccount(taskAccount);
                    } else {
                        status.setValue("DriveRepoRunClientIntent");
                    }
                } catch (ApiException apiException) {
                    status.setValue("DriveRepoRunClientIntent");
                }
            });
        } else {
            status.setValue("DriveRepoSupplyClient");
        }
    }

    /**
     * If this DriveRepoInnerClass has a valid GoogleSignInClient client, gets its sign-in Intent
     *
     * @return the client's sign-in Intent if valid, otherwise null
     */
    public Intent getClientIntent() {
        if (client != null) {
            Log.i(TAG, "Provided GoogleSignInClient sign-in intent");
            return client.getSignInIntent();
        }
        return null;
    }

    /**
     * Given a GoogleSignInAccount, sets this object's account,
     * initialising the Drive service if a valid credential also exists
     * or changing state to request a credential if not
     *
     * @param account the GoogleSignInAccount to use
     */
    public void setGoogleSignInAccount(@NonNull GoogleSignInAccount account) {
        this.account = account;
        Log.i(TAG,"Received GoogleSignInAccount");
        if (credential != null) {
            constructDriveService();
        } else {
            status.setValue("DriveRepoSupplyCreds");
        }
    }

    /**
     * Given an Intent result from running the client's sign-in Intent,
     * attempts to get the signed in account, and, upon finding a valid account,
     * sets this object's account,
     * initialising the Drive service if a valid credential also exists
     * or changing state to request a credential if not.
     *
     * @param intent the Intent received from running the client's sign-in intent
     */
    public void setGoogleSignInAccount(Intent intent) {
        GoogleSignIn.getSignedInAccountFromIntent(intent)
                .addOnCompleteListener(task -> {
                    GoogleSignInAccount taskAccount = task.getResult();
                    if (taskAccount != null) {
                        setGoogleSignInAccount(taskAccount);
                    } else {
                        status.setValue("DriveRepoRunClientIntentFailed");
                    }
                });
    }

    /**
     * Responding to a state of "DriveRepoSupplyCred", sets the GoogleAccountCredential,
     * initialising the Drive service if a valid account also exists
     * or attempting to get an account if not
     *
     * @param credential the credential to set
     */
    public void setCredential(GoogleAccountCredential credential) {
        Log.i(TAG,"Received GoogleAccountCredential from activity");
        this.credential = credential;
        if (account != null) {
            constructDriveService();
        } else {
            attemptSilentSignIn();
        }
    }

    /**
     * Constructs the Drive service
     */
    public void constructDriveService() {
        Log.i(TAG,"Constructing Drive Service");
        credential.setSelectedAccount(account.getAccount());
        driveService = new Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential)
                .setApplicationName("DriveFinance")
                .build();
        Log.i(TAG,"Drive Service constructed");
        status.setValue("DriveRepoAwaitingFolderName");
    }

    /**
     * Gets the intent from a recoverableAuthIOException
     *
     * @return the recoverable intent
     */
    public Intent getRecoverableAuthIntent() {
        return recoverableAuthIntent;
    }

}