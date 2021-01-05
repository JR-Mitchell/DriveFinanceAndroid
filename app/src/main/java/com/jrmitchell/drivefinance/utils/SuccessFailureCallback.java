package com.jrmitchell.drivefinance.utils;

public interface SuccessFailureCallback<T> {
    void success(T t);
    void failure();
}
