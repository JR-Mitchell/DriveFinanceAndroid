package com.jrmitchell.drivefinance.models;

import android.util.Pair;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.regex.Pattern;

public interface Repo {
    interface InnerData{
        LiveData<String> getStatusCode();
    }

    InnerData getInnerData();
    String getFolderName();
    void setFolderName(String folderName);
    List<Pair<String,String>> getFileMatches(Pattern regexPattern);
}
