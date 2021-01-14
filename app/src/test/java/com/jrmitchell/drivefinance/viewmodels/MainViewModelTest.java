package com.jrmitchell.drivefinance.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.jrmitchell.drivefinance.models.Repo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class MainViewModelTest {

    private static class TestRepo implements Repo {
        @Override
        public InnerData getInnerData() {
            return () -> new MutableLiveData<>("REPO_STATUS_CODE");
        }

        @Override
        public String getFolderName() {
            return "REPO_FOLDER_NAME";
        }

        @Override
        public void setFolderName(String folderName) {

        }
    }

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Test
    public void testGetFolderNameGetsRepoFolderName()  {
        MainViewModel viewModel = new MainViewModel(TestRepo::new);
        assertEquals(viewModel.getFolderName().getValue(),"REPO_FOLDER_NAME");
    }

    @Test
    public void testGetStatusCodeGetsRepoStatusCode()  {
        MainViewModel viewModel = new MainViewModel(TestRepo::new);
        assertEquals(viewModel.getStatusCode().getValue(),"REPO_STATUS_CODE");
    }

}