package com.jrmitchell.drivefinance.models;

import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.api.services.drive.model.File;
import com.jrmitchell.drivefinance.utils.UpdateableFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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

    public List<Pair<String,String>> getFileMatches(Pattern regexPattern) {
        List<Pair<String,String>> fileDictEntries = new ArrayList<>();
        Set<Map.Entry<String,String>> setCopy = new HashSet<>(fileDict.entrySet());
        Iterator<Map.Entry<String,String>> it = setCopy.iterator();
        while (it.hasNext()) {
            Map.Entry<String,String> item = it.next();
            if (regexPattern.matcher(item.getKey()).matches())
                fileDictEntries.add(new Pair<>(item.getKey(), item.getValue()));
            it.remove();
        }
        Log.d(TAG,"Number of matches: "+fileDictEntries.size());
        return fileDictEntries;
    }

    private Task<UpdateableFile> getTextFile(String fileId) {
        Task<String> fileTextTask = getFileText(fileId);
        return fileTextTask.continueWith(task -> {
            String fileContent = task.getResult();
            return new UpdateableFile(fileId,fileContent) {
                @Override
                public void update(String newContent) {
                    getFileText(fileId)
                            .addOnSuccessListener(s -> {
                                if (s.equals(fileContent)) {
                                    setFileText(fileId,newContent)
                                            .addOnSuccessListener(b -> success = b)
                                            .addOnFailureListener(e -> success = false);
                                } else {
                                    success = false;
                                }
                            })
                            .addOnFailureListener(e -> success = false);
                }
            };
        });
    }



}
