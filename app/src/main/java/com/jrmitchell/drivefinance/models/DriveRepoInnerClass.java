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
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveRepoInnerClass implements Repo.InnerData {
    private final GoogleSignInOptions signInOptions =
            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                    .build();
    private GoogleSignInAccount account;
    private Drive driveService;
    private GoogleSignInClient client;
    private final String TAG = DriveRepoInnerClass.class.getSimpleName();
    private final MutableLiveData<String> status = new MutableLiveData<>("DriveRepoSupplyClient");
    public final Map<String,String> fileDict = new HashMap<>();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private String driveFolderId;

    @Override
    public LiveData<String> getStatusCode() {
        return status;
    }

    public GoogleSignInOptions getSignInOptions() {
        Log.i(TAG,"Provided default GoogleSignInOptions");
        return signInOptions;
    }

    public void setCredential(GoogleAccountCredential credential) {
        Log.i(TAG,"Received GoogleAccountCredential from activity");
        credential.setSelectedAccount(account.getAccount());
        Log.i(TAG,"Constructing Drive Service");
        driveService = new Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential)
                .setApplicationName("DriveFinance")
                .build();
        Log.i(TAG,"Drive Service constructed");
        status.setValue("DriveRepoAwaitingFolderName");
    }

    public void setGSIA(@NonNull GoogleSignInAccount account) {
        this.account = account;
        Log.i(TAG,"Received GoogleSignInAccount");
        status.setValue("DriveRepoSupplyCreds");
    }

    public void setGSIA(Intent intent) {
        GoogleSignIn.getSignedInAccountFromIntent(intent)
                .addOnCompleteListener(task -> {
                    GoogleSignInAccount taskAccount = task.getResult();
                    if (taskAccount != null) {
                        setGSIA(taskAccount);
                    } else {
                        status.setValue("DriveRepoRunClientIntent");
                    }
                });
    }

    public void setClient(GoogleSignInClient client) {
        this.client = client;
        Log.i(TAG,"Received GoogleSignInClient from activity");
        attemptSilentSignIn();
    }

    public Intent getClientIntent() {
        Log.i(TAG,"Provided GoogleSignInClient sign-in intent");
        return client.getSignInIntent();
    }

    private Task<FileList> queryFiles(String queryString) {
        return Tasks.call(executor,()-> {
            try {
                return driveService.files()
                        .list()
                        .setQ(queryString)
                        .setFields("files(name,id,capabilities)")
                        .setSpaces("drive")
                        .execute();
            } catch (UserRecoverableAuthIOException e) {
                //Pass e.getIntent to activity to run TODO
                return null;
            }
        });
    }

    public void findFolder(String folderName) {
        Log.i(TAG,"Asked to locate Drive folder '"+folderName+"'");
        String queryString = "name = '" + folderName + "'";
        queryFiles(queryString).addOnSuccessListener(fileList -> {
            List<File> files = fileList.getFiles();
            if (!(files == null || files.isEmpty())) {
                for (File file : files) {
                    if (!file.getCapabilities().getCanListChildren()) {
                        files.remove(file);
                    }
                }
                if (files.size() == 1) {
                    //Write to shared preferences
                    status.setValue("DriveRepoWriteFolderNameToSharedPrefs");
                    driveFolderId = files.get(0).getId();
                    refreshFolderFiles();
                } else status.setValue("DriveRepoMultipleFoldersFound");
            } else status.setValue("DriveRepoFolderNotFound");
        });
    }

    public void refreshFolderFiles() {
        if (driveFolderId != null) {
            Log.i(TAG,"Populating dictionary with IDs of files in Drive folder");
            queryFiles("'" + driveFolderId + "' in parents")
                    .addOnSuccessListener(fileList -> {
                        List<File> files = fileList.getFiles();
                        fileDict.clear();
                        if (!(files == null || files.isEmpty())) {
                            for (File file : files) {
                                String name = file.getName();
                                fileDict.put(file.getName(),file.getId());
                            }
                        }
                        status.setValue("DriveRepoFullyLoaded");
                    });
        }
    }

    private void attemptSilentSignIn() {
        if (client != null) {
            status.setValue("DriveRepoAttemptSilentSignIn");
            Task<GoogleSignInAccount> task = client.silentSignIn();
            if (task.isSuccessful()) {
                GoogleSignInAccount taskAccount = task.getResult();
                if (taskAccount != null) {
                    setGSIA(taskAccount);
                } else {
                    status.setValue("DriveRepoRunClientIntent");
                }
            }
            else task.addOnCompleteListener(completeTask -> {
                try {
                    GoogleSignInAccount taskAccount = task.getResult(ApiException.class);
                    if (taskAccount != null) {
                        setGSIA(taskAccount);
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
}