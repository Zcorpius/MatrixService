package com.neo.matrixserver.viewmodel;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.neo.matrixserver.service.MatrixServerService;

public class MainViewModel extends AndroidViewModel {

    public final MutableLiveData<String> stateText = new MutableLiveData<>("State: -");
    public final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void startService() {
        Context context = getApplication();
        Intent intent = new Intent(context, MatrixServerService.class);
        context.startService(intent);
        toastMessage.setValue("Service Started");
    }

    public void stopService() {
        Context context = getApplication();
        Intent intent = new Intent(context, MatrixServerService.class);
        context.stopService(intent);
        stateText.setValue("State: -");
        toastMessage.setValue("Service Stopped");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
