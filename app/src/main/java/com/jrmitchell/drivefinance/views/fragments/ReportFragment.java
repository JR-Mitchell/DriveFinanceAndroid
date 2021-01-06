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
import com.jrmitchell.drivefinance.viewmodels.Reports;
import com.jrmitchell.drivefinance.utils.FolderUtils;

public class ReportFragment extends Fragment {

    private final Reports reports = new Reports(FolderUtils.getSingletonInstance(),ReportFragment.class);

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.report_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        reports.onViewCreated(new FragmentWrapper(this,view)
                .addViewId("recyclerView",R.id.reportRecyclerView),
                getActivity(),
                getContext(),
                this::startActivity);
        super.onViewCreated(view, savedInstanceState);
    }
}