package com.neo.matrixclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MatrixClientService extends Service {

    public static String TAG = MatrixClientService.class.getName();

    @Nullable
    public static MatrixClientService INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        INSTANCE = null;
    }
}
