package com.jrmitchell.drivefinance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.jrmitchell.drivefinance.R;

public class FailedLoginFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.failed_login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((Button) view.findViewById(R.id.retry_button)).setOnClickListener((View.OnClickListener) v -> NavHostFragment.findNavController(FailedLoginFragment.this).navigate(R.id.action_failedLoginFragment_to_loginFragment));
        super.onViewCreated(view, savedInstanceState);
    }
}