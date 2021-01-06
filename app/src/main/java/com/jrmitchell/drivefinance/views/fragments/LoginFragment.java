package com.jrmitchell.drivefinance.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.utils.DriveUtils;
import com.jrmitchell.drivefinance.utils.SuccessFailureCallback;
import com.jrmitchell.drivefinance.viewmodels.Login;

public class LoginFragment extends Fragment {

    private final Login login = new Login();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        login.onViewCreated(view,this);
        super.onViewCreated(view, savedInstanceState);
    }
}