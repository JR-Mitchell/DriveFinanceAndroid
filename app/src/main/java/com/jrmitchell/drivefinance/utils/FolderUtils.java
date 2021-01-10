package com.jrmitchell.drivefinance.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import com.google.api.services.drive.model.File;
import com.jrmitchell.drivefinance.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class for accessing files in a particular folder on the drive
 */
public class FolderUtils {
    private static FolderUtils SINGLETON_INSTANCE;
    private  FolderUtils() {
    }
    private static final DriveUtils driveUtils = DriveUtils.getSingletonInstance();

    private boolean isDriveFolderSelected = false;
    private String driveFolderId;
    private String driveFolderName;

    public String getDriveFolderName() {
        return driveFolderName;
    }

    public final Map<String,String> fileDict = new HashMap<>();

    public boolean driveServiceNeedsInitialising() {
        return driveUtils.driveServiceNeedsInitialising();
    }

    public static FolderUtils getSingletonInstance() {
        if (SINGLETON_INSTANCE == null) SINGLETON_INSTANCE = new FolderUtils();
        return  SINGLETON_INSTANCE;
    }

    public boolean driveFolderNeedsSelecting() {
        return  !isDriveFolderSelected;
    }

    public void checkDriveFolderNeedsSelecting(Fragment fragment, SuccessFailureCallback<Void> callback) {
        if (isDriveFolderSelected) callback.success(null);
        else {
            //If there is a shared preference folder name, select it
            SharedPreferences sharedPref = fragment.getActivity().getPreferences(Context.MODE_PRIVATE);
            String folderName = sharedPref.getString(fragment.getString(R.string.folder_name_lookup_key), null);
            if (folderName != null) selectDriveFolder(folderName, fragment, callback);
            else callback.failure();
        }
    }

    public void selectDriveFolder(String folderName, Fragment fragment, SuccessFailureCallback<Void> callback) {
        driveUtils.queryFiles(intent -> { }, "name = '" + folderName + "'")
            .addOnSuccessListener(fileList -> {
                List<File> files = fileList.getFiles();
                if (!(files == null || files.isEmpty())) {
                    for (File file : files) {
                        if (!file.getCapabilities().getCanListChildren()) {
                             files.remove(file);
                        }
                    }
                    if (files.size() == 1) {
                        //Write to shared preferences
                        SharedPreferences sharedPref = fragment.getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(fragment.getString(R.string.folder_name_lookup_key),folderName);
                        editor.apply();
                        isDriveFolderSelected = true;
                        driveFolderId = files.get(0).getId();
                        driveFolderName = folderName;
                        refreshFolderFiles(callback);
                    } else callback.failure();
                } else callback.failure();
            })
        .addOnFailureListener(e -> callback.failure());
    }

    public void refreshFolderFiles(SuccessFailureCallback<Void> callback) {
        driveUtils.queryFiles(intent -> {}, "'" + driveFolderId + "' in parents")
                .addOnSuccessListener(fileList -> {
                    List<File> files = fileList.getFiles();
                    if (!(files == null || files.isEmpty())) {
                        for (File file : files) {
                            String name = file.getName();
                            fileDict.put(file.getName(),file.getId());
                        }
                    }
                    callback.success(null);
                })
                .addOnFailureListener(e->callback.failure());
    }

    public void getPaymentsFile(SuccessFailureCallback<UpdateableFile> callback) {
        String paymentsId = fileDict.get("Payments");
        if (paymentsId != null) {
            driveUtils.getFileTextData(paymentsId)
                    .addOnSuccessListener(s -> callback.success(new UpdateableFile(paymentsId,s)))
                    .addOnFailureListener(e -> callback.failure());

        } else {
            callback.failure();
        }
    }
}
