package com.jrmitchell.drivefinance.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jrmitchell.drivefinance.R;
import com.jrmitchell.drivefinance.utils.CallbackInterface;
import com.jrmitchell.drivefinance.utils.FolderUtils;
import com.jrmitchell.drivefinance.views.adapters.ReportAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Non-inheriting class holding behaviour for the
 * Reports list view.
 * Should ultimately be testable, with minimal dependencies
 */
public class Reports {

    private final FolderUtils folderUtils;
    private final Class<? extends Fragment> cls;

    public Reports(FolderUtils folderUtils, Class<? extends Fragment> cls) {
        this.folderUtils = folderUtils;
        this.cls = cls;
    }

    public void onViewCreated(@NonNull View view, Activity activity, Context context, CallbackInterface<Intent> startActivity) {
        if (folderUtils.driveServiceNeedsInitialising() || folderUtils.driveFolderNeedsSelecting()) {
            //Redirect to login
            Intent intent = new Intent(activity, cls);
            startActivity.run(intent);
        } else {
            //Set up text
            List<Pair<String,String>> fileDictEntries = new ArrayList<>();
            Iterator<Map.Entry<String,String>> it = folderUtils.fileDict.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String,String> item = it.next();
                if (item.getKey().contains(".pdf"))
                    fileDictEntries.add(new Pair<>(item.getKey(), item.getValue()));
                it.remove();
            }
            //Set up RecyclerView
            RecyclerView recyclerView = view.findViewById(R.id.reportRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ReportAdapter(context,fileDictEntries));
        }
    }
}
