package com.jrmitchell.drivefinance.models;

import androidx.lifecycle.LiveData;

public interface Repo {
    interface InnerData{
        LiveData<String> getStatusCode();
    }

    InnerData getInnerData();
    String getFolderName();
    void setFolderName(String folderName);
}
