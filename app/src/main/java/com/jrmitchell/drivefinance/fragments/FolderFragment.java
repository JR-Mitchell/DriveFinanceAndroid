package com.jrmitchell.drivefinance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.utils.FolderUtils;

import java.util.Iterator;
import java.util.Map;

public class FolderFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.folder_loaded_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (FolderUtils.getSingletonInstance().driveServiceNeedsInitialising()) {
            //Redirect to login
            NavHostFragment.findNavController(FolderFragment.this).navigate(R.id.action_folderLoadedFragment_to_loginFragment);
        } else if (FolderUtils.getSingletonInstance().driveFolderNeedsSelecting()) {
            //Redirect to folder
            NavHostFragment.findNavController(FolderFragment.this).navigate(R.id.action_folderLoadedFragment_to_loggedInFragment);
        } else {
            //Set up text
            StringBuilder string = new StringBuilder();
            Iterator<Map.Entry<String, String>> it = FolderUtils.getSingletonInstance().fileDict.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String,String> pair = it.next();
                string.append(pair.getKey()).append(": ").append(pair.getValue()).append("\n");
                it.remove();
            }
            ((TextView) view.findViewById(R.id.folder_textview)).setText(string.toString());
        }
        super.onViewCreated(view, savedInstanceState);
    }
}