package com.jrmitchell.drivefinance.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.utils.CallbackInterface;
import com.jrmitchell.drivefinance.utils.FolderUtils;
import com.jrmitchell.drivefinance.utils.SuccessFailureCallback;
import com.jrmitchell.drivefinance.views.activities.LoginActivity;

/**
 * Non-inheriting class holding behaviour for the
 * Payments list view.
 * Should ultimately be testable, with minimal dependencies
 */
public class Payments {
    private final FolderUtils folderUtils;

    public Payments(FolderUtils folderUtils) {
        this.folderUtils = folderUtils;
    }

    public void onViewCreated(@NonNull View view, Activity activity, CallbackInterface<Intent> startActivity) {
        if (folderUtils.driveServiceNeedsInitialising() || folderUtils.driveFolderNeedsSelecting()) {
            //Redirect to login
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity.run(intent);
        } else {
            ((TextView) view.findViewById(R.id.folder_textview)).setText(R.string.reading_payments_file);
            //Set up text
            folderUtils.getPaymentsFile(new SuccessFailureCallback<String>() {
                @Override
                public void success(String content) {
                    ((TextView) view.findViewById(R.id.folder_textview)).setText(content);
                }

                @Override
                public void failure() {
                    ((TextView) view.findViewById(R.id.folder_textview)).setText(R.string.no_payments_file_found);
                }
            });
        }
    }
}
