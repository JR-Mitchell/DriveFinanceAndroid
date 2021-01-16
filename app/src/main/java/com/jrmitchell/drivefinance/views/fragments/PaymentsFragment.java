package com.jrmitchell.drivefinance.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.models.FolderRepo;
import com.jrmitchell.drivefinance.viewmodels.MainViewModel;
import com.jrmitchell.drivefinance.viewmodels.MainViewModelFactory;

public class PaymentsFragment extends Fragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.payments_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            //Get the main ViewModel
            MainViewModel viewModel = new ViewModelProvider(
                    getActivity(),
                    new MainViewModelFactory(FolderRepo::new)
            ).get(MainViewModel.class);
        }
    }
}