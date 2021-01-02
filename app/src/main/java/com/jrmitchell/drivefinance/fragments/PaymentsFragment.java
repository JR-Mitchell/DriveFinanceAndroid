package com.jrmitchell.drivefinance.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.activities.LoginActivity;
import com.jrmitchell.drivefinance.utils.DriveUtils;
import com.jrmitchell.drivefinance.utils.FolderUtils;

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
        if (FolderUtils.getSingletonInstance().driveServiceNeedsInitialising() || FolderUtils.getSingletonInstance().driveFolderNeedsSelecting()) {
            //Redirect to login
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            //Set up text
            String string = FolderUtils.getSingletonInstance().fileDict.get("Payments");
            if (string == null) {
                ((TextView) view.findViewById(R.id.folder_textview)).setText(R.string.no_payments_file_found);
            } else {
                ((TextView) view.findViewById(R.id.folder_textview)).setText(R.string.reading_payments_file);
                DriveUtils.getSingletonInstance().getFileTextData(string)
                        .addOnSuccessListener(content -> ((TextView) view.findViewById(R.id.folder_textview)).setText(content))
                        .addOnFailureListener(e -> ((TextView) view.findViewById(R.id.folder_textview)).setText(R.string.failed_payments_read));
            }
        }
        super.onViewCreated(view, savedInstanceState);
    }
}