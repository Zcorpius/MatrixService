package com.neo.matrixclient.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.neo.matrixclient.service.MatrixClientService;

public class MainViewModel extends AndroidViewModel {

    public static String TAG = MatrixClientService.class.getName();


    public final MutableLiveData<String> stateText = new MutableLiveData<>("State: -");
    public final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void startService() {
        Context context = getApplication();
        Intent intent = new Intent(context, MatrixClientService.class);
        context.startService(intent);
        toastMessage.setValue("Service Started");
    }

    public void getState() {
        MatrixClientService service = MatrixClientService.INSTANCE;
        if (service == null) {
            toastMessage.setValue("Service not running");
            return;
        }
        int state = service.requestGetState();
        stateText.setValue("State: " + state);
    }

    public void setState() {
        MatrixClientService service = MatrixClientService.INSTANCE;
        if (service == null) {
            toastMessage.setValue("Service not running");
            return;
        }
        int newState = (int) (Math.random() * 100);
        Log.d(TAG, "start setState.");
        if (service.requestSetState(newState)) {
            Log.d(TAG, "finish setState.");
            stateText.setValue("State: " + newState);
            toastMessage.setValue("State set to " + newState);
        } else {
            toastMessage.setValue("Set state failed");
        }
    }

    public void setStateSync() {
        MatrixClientService service = MatrixClientService.INSTANCE;
        if (service == null) {
            toastMessage.setValue("Service not running");
            return;
        }
        int newState = (int) (Math.random() * 100);
        Log.d(TAG, "start setStateSync.");
        if (service.requestSetStateSync(newState)) {
            Log.d(TAG, "finish setStateSync.");
            stateText.setValue("State: " + newState);
            toastMessage.setValue("State set sync to " + newState);
        } else {
            toastMessage.setValue("Set state sync failed");
        }
    }

    public void StateInfoIn() {
        MatrixClientService service = MatrixClientService.INSTANCE;
        if (service == null) {
            toastMessage.setValue("Service not running");
            return;
        }
        service.requestStateInfoIn();
    }

    public void StateInfoOut() {
        MatrixClientService service = MatrixClientService.INSTANCE;
        if (service == null) {
            toastMessage.setValue("Service not running");
            return;
        }
        service.requestStateInfoOut();
    }

    public void StateInfoInOut() {
        MatrixClientService service = MatrixClientService.INSTANCE;
        if (service == null) {
            toastMessage.setValue("Service not running");
            return;
        }
        service.requestStateInfoInOut();
    }

    public void stopService() {
        Context context = getApplication();
        Intent intent = new Intent(context, MatrixClientService.class);
        context.stopService(intent);
        stateText.setValue("State: -");
        toastMessage.setValue("Service Stopped");
    }
}
