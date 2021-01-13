package com.jrmitchell.drivefinance.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.jrmitchell.drivefinance.models.Repo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class MainViewModelTest {

    private class TestRepo implements Repo {
        @Override
        public String getFolderName() {
            return "REPO_FOLDER_NAME";
        }
    }

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Test
    public void testGetFolderNameGetsRepoFolderName()  {
        MainViewModel viewModel = new MainViewModel(TestRepo::new);
        assertEquals(viewModel.getFolderName().getValue(),"REPO_FOLDER_NAME");
    }

}