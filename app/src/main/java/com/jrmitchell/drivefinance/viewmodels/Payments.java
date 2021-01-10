package com.jrmitchell.drivefinance.viewmodels;

import android.app.Activity;
import android.content.Intent;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.utils.CallbackInterface;
import com.jrmitchell.drivefinance.utils.FolderUtils;
import com.jrmitchell.drivefinance.utils.FragmentWrapper;
import com.jrmitchell.drivefinance.utils.SuccessFailureCallback;
import com.jrmitchell.drivefinance.utils.UpdateableFile;
import com.jrmitchell.drivefinance.views.activities.LoginActivity;

/**
 * Non-inheriting class holding behaviour for the
 * Payments list view.
 * Should ultimately be testable, with minimal dependencies
 */
public class Payments {
    private final FolderUtils folderUtils;
    private UpdateableFile paymentsFile;

    public Payments(FolderUtils folderUtils) {
        this.folderUtils = folderUtils;
    }

    public void onViewCreated(FragmentWrapper fragmentWrapper, Activity activity, CallbackInterface<Intent> startActivity) {
        if (folderUtils.driveServiceNeedsInitialising() || folderUtils.driveFolderNeedsSelecting()) {
            //Redirect to login
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity.run(intent);
        } else {
            fragmentWrapper.setTextViewText("textView",R.string.reading_payments_file);
            //Set up text
            folderUtils.getPaymentsFile(new SuccessFailureCallback<UpdateableFile>() {
                @Override
                public void success(UpdateableFile content) {
                    paymentsFile = content;
                    fragmentWrapper.setTextViewText("textView",paymentsFile.getContent());
                }

                @Override
                public void failure() {
                    fragmentWrapper.setTextViewText("textView",R.string.no_payments_file_found);
                }
            });
        }
    }
}
