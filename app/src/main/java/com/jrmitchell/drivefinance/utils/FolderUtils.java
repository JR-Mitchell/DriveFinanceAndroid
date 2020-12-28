package com.jrmitchell.drivefinance.utils;

import android.util.Log;

import com.google.api.services.drive.model.File;

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

    public final Map<String,String> fileDict = new HashMap<>();

    public boolean driveServiceNeedsInitialising() {
        return driveUtils.driveServiceNeedsInitialising();
    }

    public static FolderUtils getSingletonInstance() {
        if (SINGLETON_INSTANCE == null) SINGLETON_INSTANCE = new FolderUtils();
        return  SINGLETON_INSTANCE;
    }


    public boolean driveFolderNeedsSelecting() {
        return !isDriveFolderSelected;
    }

    public void selectDriveFolder(String folderName, SigninCallback callback) {
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
                        isDriveFolderSelected = true;
                        driveFolderId = files.get(0).getId();
                        refreshFolderFiles(callback);
                    } else callback.failure();
                } else callback.failure();
            })
        .addOnFailureListener(e -> callback.failure());
    }

    public void refreshFolderFiles(SigninCallback callback) {
        driveUtils.queryFiles(intent -> {}, "'" + driveFolderId + "' in parents")
                .addOnSuccessListener(fileList -> {
                    List<File> files = fileList.getFiles();
                    if (!(files == null || files.isEmpty())) {
                        for (File file : files) {
                            String name = file.getName();
                            if (name != null) Log.d("DriveFinanceName",name);
                            fileDict.put(file.getName(),file.getId());
                        }
                    }
                    callback.success();
                })
                .addOnFailureListener(e->callback.failure());
    }
}
