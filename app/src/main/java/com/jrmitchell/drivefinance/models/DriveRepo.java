package com.jrmitchell.drivefinance.models;

import android.util.Log;

public class DriveRepo implements Repo {

    private String folderName;
    private final DriveRepoInnerClass innerData;

    public DriveRepo() {
        String TAG = DriveRepo.class.getSimpleName();
        Log.i(TAG,"Created new DriveRepo");
        innerData = new DriveRepoInnerClass();
    }

    @Override
    public Repo.InnerData getInnerData() {
        return innerData;
    }

    @Override
    public String getFolderName() {
        return folderName;
    }

    @Override
    public void setFolderName(String folderName) {
        this.folderName = folderName;
        innerData.findFolder(folderName);
    }

}
