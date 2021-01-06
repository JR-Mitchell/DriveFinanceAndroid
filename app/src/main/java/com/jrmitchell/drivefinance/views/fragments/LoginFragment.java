package com.jrmitchell.drivefinance.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.utils.FragmentWrapper;
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
        login.onViewCreated(view,new FragmentWrapper(this,view)
                .addNavId("success",R.id.action_loginFragment_to_loggedInFragment)
                .addNavId("failure",R.id.action_loginFragment_to_failedLoginFragment));
        super.onViewCreated(view, savedInstanceState);
    }
}