package com.jrmitchell.drivefinance.viewmodels;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jrmitchell.drivefinance.models.Repo;
import com.jrmitchell.drivefinance.models.RepoFactory;

public class MainViewModel extends ViewModel {
    private final Repo repo;
    private final MutableLiveData<String> folderName;
    private final String TAG = MainViewModel.class.getSimpleName();

    public MainViewModel(RepoFactory factory) {
        super();
        this.repo = factory.getRepo();
        folderName = new MutableLiveData<>("No folder loaded...");
    }

    /**
     * Gets the current name of the open repo folder
     *
     * @return LiveData of the repo folder name
     */
    public LiveData<String> getFolderName() {
        String repoFolderName = repo.getFolderName();
        if (repoFolderName != null && !repoFolderName.equals(folderName.getValue()))
            folderName.setValue(repoFolderName);
        return folderName;
    }

    /**
     * Sets a new name for the repo folder
     *
     * @param folderName the name of the folder
     */
    public void setFolderName(String folderName) {
        if (folderName != null) {
            Log.i(TAG,"Foldername set to "+folderName);
            repo.setFolderName(folderName);
            getFolderName();
        }
    }

    /**
     * Gets the status code for the open repo folder
     *
     * @return LiveData for the repo folder status code
     */
    public LiveData<String> getStatusCode() {
        return repo.getInnerData().getStatusCode();
    }

    /**
     * Get the innerdata for the repo
     *
     * @return the repo's inner data
     */
    public Repo.InnerData getInnerData() {
        return repo.getInnerData();
    }

}
