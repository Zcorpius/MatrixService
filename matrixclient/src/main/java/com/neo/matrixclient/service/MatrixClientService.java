package com.neo.matrixclient.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.neo.matrixserver.IMatrixProxy;

public class MatrixClientService extends Service {

    public static String TAG = MatrixClientService.class.getName();

    @Nullable
    public static MatrixClientService INSTANCE;

    private IMatrixProxy remoteProxy;
    private boolean isRemoteBound = false;

    private final ServiceConnection remoteConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected name: " + name);
            remoteProxy = IMatrixProxy.Stub.asInterface(service);
            isRemoteBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected name: " + name);
            remoteProxy = null;
            isRemoteBound = false;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        bindRemoteService();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRemoteBound) {
            unbindService(remoteConnection);
            isRemoteBound = false;
        }
        INSTANCE = null;
    }

    public int requestGetState() {
        if (remoteProxy == null) return -1;
        try {
            return remoteProxy.getState();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean requestSetState(int state) {
        if (remoteProxy == null) return false;
        try {
            remoteProxy.setState(state);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    private void bindRemoteService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(
                "com.neo.matrixserver",
                "com.neo.matrixserver.service.MatrixServerService"));
        boolean result = bindService(intent, remoteConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bind MatrixServerService result: " + result);
    }
}
