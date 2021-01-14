package com.jrmitchell.drivefinance.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.jrmitchell.drivefinance.models.RepoFactory;

import java.lang.reflect.InvocationTargetException;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RepoFactory factory;

    public MainViewModelFactory(RepoFactory factory) {
        super();
        this.factory = factory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(RepoFactory.class)
                    .newInstance(factory);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return super.create(modelClass);
    }
}
