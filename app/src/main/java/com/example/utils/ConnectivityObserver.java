package com.example.utils;

import androidx.lifecycle.LiveData;

import io.reactivex.rxjava3.core.Observable;

public interface ConnectivityObserver {

    Observable<Object> Observe();

    Boolean networkStatus();


}





