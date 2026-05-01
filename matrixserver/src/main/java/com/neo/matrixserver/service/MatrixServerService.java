package com.neo.matrixserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.neo.matrixserver.IMatrixProxy;

public class MatrixServerService extends Service {

    public static String TAG = MatrixServerService.class.getName();

    private int currentState = 0;

    private final IMatrixProxy.Stub binder = new IMatrixProxy.Stub() {
        @Override
        public int getState() throws RemoteException {
            Log.d(TAG, "getState currentState: " + currentState);
            return currentState;
        }

        @Override
        public void setState(int state) throws RemoteException {
            Log.d(TAG, "setState state: " + state);
            currentState = state;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate.");
    }
}
