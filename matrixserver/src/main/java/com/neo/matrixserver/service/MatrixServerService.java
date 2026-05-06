package com.neo.matrixserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.neo.matrixserver.IMatrixProxy;
import com.neo.matrixserver.state.StateInfo;

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
            Log.d(TAG, "setState: " + state);
            currentState = state;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Log.i(TAG, "setState Done.");
        }

        @Override
        public void setState_aysc(int state) throws RemoteException {
            Log.d(TAG, "setState_aysc: " + state);
            currentState = state;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Log.i(TAG, "setState_aysc Done.");
        }

        @Override
        public int getStateInfo_in(StateInfo info) throws RemoteException {
            Log.d(TAG, "getStateInfo_in: " + info);
            info.setUserId(101);
            info.setValue(201);
            return 0;
        }

        @Override
        public int getStateInfo_out(StateInfo info) throws RemoteException {
            Log.d(TAG, "getStateInfo_out: " + info);
            info.setUserId(201);
            info.setValue(301);
            return 0;
        }

        @Override
        public int getStateInfo_inout(StateInfo info) throws RemoteException {
            Log.d(TAG, "getStateInfo_inout: " + info);
            info.setUserId(301);
            info.setValue(401);
            return 0;
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
