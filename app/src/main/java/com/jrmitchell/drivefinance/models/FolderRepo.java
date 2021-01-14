package com.jrmitchell.drivefinance.models;

import android.util.Log;

import com.google.api.services.drive.model.File;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FolderRepo extends DriveRepo {

    private final String TAG = FolderRepo.class.getSimpleName();

    @Override
    public String getFolderName() {
        return folderName;
    }

    @Override
    public void setFolderName(String folderName) {
        this.folderName = folderName;
        findFolder(folderName);
    }

    //Objects for Drive folder access

    public final Map<String,String> fileDict = new HashMap<>();
    private String driveFolderId;
    private String folderName;

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
                    innerData.status.setValue("DriveRepoWriteFolderNameToSharedPrefs");
                    driveFolderId = files.get(0).getId();
                    refreshFolderFiles();
                } else innerData.status.setValue("DriveRepoMultipleFoldersFound");
            } else innerData.status.setValue("DriveRepoFolderNotFound");
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
                        innerData.status.setValue("DriveRepoFullyLoaded");
                    });
        }
    }

}
