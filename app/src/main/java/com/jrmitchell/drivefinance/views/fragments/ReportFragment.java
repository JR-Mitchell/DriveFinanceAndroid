package com.jrmitchell.drivefinance.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.models.FolderRepo;
import com.jrmitchell.drivefinance.viewmodels.MainViewModel;
import com.jrmitchell.drivefinance.viewmodels.MainViewModelFactory;
import com.jrmitchell.drivefinance.views.adapters.ReportAdapter;

import java.util.List;
import java.util.regex.Pattern;

public class ReportFragment extends Fragment {

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
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            //Get the main ViewModel
            MainViewModel mainViewModel = new ViewModelProvider(
                    getActivity(),
                    new MainViewModelFactory(FolderRepo::new)
            ).get(MainViewModel.class);

            //Get the list of reports
            List<Pair<String,String>> reportList = mainViewModel.getFileMatches(Pattern.compile(".*\\.pdf"));

            //Set up the recyclerView
            RecyclerView myView = view.findViewById(R.id.report_fragment_recycler_view);
            if (myView != null) {
                myView.setLayoutManager(new LinearLayoutManager(getContext()));
                myView.setAdapter(new ReportAdapter(getContext(),reportList));
            }
        }
    }
}