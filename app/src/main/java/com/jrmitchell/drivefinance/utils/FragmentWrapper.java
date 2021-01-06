package com.jrmitchell.drivefinance.utils;

import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.HashMap;
import java.util.Map;

public class FragmentWrapper {
    private final Fragment fragment;
    private final View view;
    private final Map<String, Integer> viewMap = new HashMap<>();
    private final Map<String, Integer> navMap = new HashMap<>();

    public FragmentWrapper(Fragment fragment, @NonNull View view) {
        this.fragment = fragment;
        this.view = view;
    }

    public FragmentWrapper addViewId(String viewName, int viewId) {
        viewMap.put(viewName,viewId);
        return this;
    }

    public FragmentWrapper addNavId(String navName, int navId) {
        navMap.put(navName,navId);
        return this;
    }

    public boolean navigate(String navName) {
        try {
            Integer id = navMap.get(navName);
            if (id != null) {
                NavHostFragment.findNavController(fragment).navigate(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public View getViewByName(String viewName) {
        Integer id = viewMap.get(viewName);
        if (id != null) {
            return view.findViewById(id);
        }
        return null;
    }

    public boolean setTextViewText(String viewName, String viewText) {
        try {
            TextView myView = (TextView) getViewByName(viewName);
            if (myView != null) {
                myView.setText(viewText);
                return true;
            }
            return false;
        } catch(Exception e) {
            return false;
        }
    }

    public boolean setTextViewText(String viewName, int viewText) {
        try {
            TextView myView = (TextView) getViewByName(viewName);
            if (myView != null) {
                myView.setText(viewText);
                return true;
            }
            return false;
        } catch(Exception e) {
            return false;
        }
    }

    public <T extends RecyclerView.ViewHolder> boolean setupRecyclerView(String viewName, RecyclerView.Adapter<T> adapter) {
        try {
            RecyclerView myView = (RecyclerView) getViewByName(viewName);
            if (myView != null) {
                myView.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
                myView.setAdapter(adapter);
                return true;
            }
            return false;
        } catch(Exception e) {
            return false;
        }
    }

    public <I,O> void launchCall(@NonNull ActivityResultContract<I,O> contract, @NonNull ActivityResultCallback<O> callback, I i) {
        fragment.prepareCall(contract,callback).launch(i);
    }
}
