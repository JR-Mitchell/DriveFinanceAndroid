package com.jrmitchell.drivefinance.viewmodels;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.jrmitchell.drivefinance.models.Repo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(BlockJUnit4ClassRunner.class)
public class MainViewModelTest {

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

    @Test
    public void testSetFolderNameCallsRepoNonNull() {
        Repo mockRepo = spy(Repo.class);
        MainViewModel viewModel = new MainViewModel(() -> mockRepo);
        viewModel.setFolderName("NEW_FOLDER_NAME");
        verify(mockRepo).setFolderName("NEW_FOLDER_NAME");
    }

    @Test
    public void testSetFolderNameNullDoesNothing() {
        Repo mockRepo = spy(Repo.class);
        MainViewModel viewModel = new MainViewModel(() -> mockRepo);
        viewModel.setFolderName(null);
        verify(mockRepo,never()).setFolderName(anyString());
        verify(mockRepo,never()).setFolderName(null);
    }

}