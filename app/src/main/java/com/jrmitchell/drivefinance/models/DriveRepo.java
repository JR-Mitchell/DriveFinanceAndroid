package com.jrmitchell.drivefinance.models;

import android.content.Intent;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.model.FileList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class DriveRepo implements Repo {

    protected final DriveRepoInnerData innerData = new DriveRepoInnerData();

    //Overrides

    @Override
    public Repo.InnerData getInnerData() {
        return innerData;
    }

    protected Intent RecoverableAuthIntent;

    //Objects necessary for Google drive queries
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Queries the drive for all files matching a certain query
     *
     * @param queryString the query to match
     * @return a Task for the FileList of matching files
     */
    protected Task<FileList> queryFiles(String queryString) {
        return Tasks.call(executor,()-> {
            try {
                return innerData.driveService.files()
                        .list()
                        .setQ(queryString)
                        .setFields("files(name,id,capabilities)")
                        .setSpaces("drive")
                        .execute();
            } catch (UserRecoverableAuthIOException e) {
                RecoverableAuthIntent = e.getIntent();
                innerData.status.setValue("DriveRepoRunRecoverableIntent");
            }
            return null;
        });
    }
}
