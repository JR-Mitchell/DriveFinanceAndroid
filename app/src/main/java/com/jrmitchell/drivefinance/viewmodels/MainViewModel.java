package com.jrmitchell.drivefinance.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jrmitchell.drivefinance.models.Repo;
import com.jrmitchell.drivefinance.models.RepoFactory;

public class MainViewModel extends ViewModel {
    private final Repo repo;
    private final MutableLiveData<String> folderName;

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
        if (repoFolderName != null) folderName.setValue(repoFolderName);
        return folderName;
    }

}
