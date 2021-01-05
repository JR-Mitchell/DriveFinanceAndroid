package com.jrmitchell.drivefinance.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.viewmodels.Payments;
import com.jrmitchell.drivefinance.utils.FolderUtils;

public class PaymentsFragment extends Fragment {

    private final Payments payments = new Payments(FolderUtils.getSingletonInstance());

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
        payments.onViewCreated(view,getActivity(), this::startActivity);
        super.onViewCreated(view, savedInstanceState);
    }
}