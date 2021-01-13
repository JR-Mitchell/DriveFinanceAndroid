package com.jrmitchell.drivefinance.models;

import android.util.Log;

public class DriveRepo implements Repo {

    private String TAG = DriveRepo.class.getSimpleName();

    public DriveRepo() {
        Log.i(TAG,"Created new DriveRepo");
    }

    @Override
    public String getFolderName() {
        return null;
    }

}
