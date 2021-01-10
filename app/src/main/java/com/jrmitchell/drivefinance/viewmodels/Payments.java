package com.jrmitchell.drivefinance.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.utils.CallbackInterface;
import com.jrmitchell.drivefinance.utils.FolderUtils;
import com.jrmitchell.drivefinance.utils.FragmentWrapper;
import com.jrmitchell.drivefinance.utils.SuccessFailureCallback;
import com.jrmitchell.drivefinance.utils.UpdateableFile;
import com.jrmitchell.drivefinance.views.activities.LoginActivity;
import com.jrmitchell.drivefinance.views.adapters.PaymentLineAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    private void setupRecyclerView(FragmentWrapper fragmentWrapper, Context context) {
        List<String> lines;
        if (paymentsFile != null) {
            lines = Arrays.asList(paymentsFile.getContent().split("\n"));
        } else {
            lines = Collections.singletonList("Failed to load payments file!");
        }
        //Set up RecyclerView
        fragmentWrapper.setupRecyclerView("recyclerView",
                new PaymentLineAdapter(context,lines));
    }

    public void onViewCreated(FragmentWrapper fragmentWrapper, Activity activity, Context context, CallbackInterface<Intent> startActivity) {
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
                    setupRecyclerView(fragmentWrapper,context);
                }

                @Override
                public void failure() {
                    setupRecyclerView(fragmentWrapper,context);
                }
            });
        }
    }
}
