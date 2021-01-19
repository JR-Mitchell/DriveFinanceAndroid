package com.jrmitchell.drivefinance.viewmodels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.jrmitchell.drivefinance.models.Repo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TestRepo implements Repo {
    private String folderName = "REPO_FOLDER_NAME";
    private List<Pair<String, String>> files = new ArrayList<Pair<String, String>>() {{
            add(new Pair<>("TestFile", "id1"));
            add(new Pair<>("TestPdf.pdf", "id2"));
        }};

    @Override
    public Repo.InnerData getInnerData() {
        return () -> new MutableLiveData<>("REPO_STATUS_CODE");
    }

    @Override
    public String getFolderName() {
        return folderName;
    }

    @Override
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public List<Pair<String, String>> getFileMatches(Pattern regexPattern) {
        return files;
    }
}