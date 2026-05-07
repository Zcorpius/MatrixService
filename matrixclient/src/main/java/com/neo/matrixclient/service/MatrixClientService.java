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

import com.neo.matrixserver.IMatrixCallback;
import com.neo.matrixserver.IMatrixProxy;
import com.neo.matrixserver.state.StateInfo;

public class MatrixClientService extends Service {

    public static String TAG = MatrixClientService.class.getName();

    @Nullable
    public static MatrixClientService INSTANCE;
    IMatrixCallback mMatrixCallback;
    private IMatrixProxy remoteProxy;
    private boolean isRemoteBound = false;

    private final ServiceConnection remoteConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected name: " + name);
            remoteProxy = IMatrixProxy.Stub.asInterface(service);
            isRemoteBound = true;
            registerMatrixServerCallBack();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected name: " + name);
            remoteProxy = null;
            isRemoteBound = false;
            unregisterMatrixServerCallBack();
        }
    };

    /**
     * 回调实现类
     * 对于oneway接口，使用匿名类实现会导致回调对象为null。需要使用具名类继承自IMatrixCallback.Stub
     */
    private static class MatrixCallbackImpl extends IMatrixCallback.Stub {
        @Override
        public void onStateChanged(int state) throws RemoteException {
            Log.d(TAG, "onStateChanged state: " + state);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        // 使用具名类而不是匿名类，避免oneway接口的问题
        mMatrixCallback = new MatrixCallbackImpl();
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

    public boolean requestSetStateSync(int state) {
        if (remoteProxy == null) return false;
        try {
            remoteProxy.setState_aysc(state);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public void requestStateInfoIn() {
        if (remoteProxy == null) return;
        StateInfo info = new StateInfo(100,200);
        try {
            remoteProxy.getStateInfo_in(info);
            Log.d(TAG, "requestStateInfoInOut: " + info);
        } catch (RemoteException e) {
            Log.e(TAG, "requestStateInfoInOut Exception: " + e);
        }
    }

    public void requestStateInfoOut() {
        if (remoteProxy == null) return;
        StateInfo info = new StateInfo(200,300);
        try {
            remoteProxy.getStateInfo_out(info);
            Log.d(TAG, "requestStateInfoOut: " + info);
        } catch (RemoteException e) {
            Log.e(TAG, "requestStateInfoOut Exception: " + e);
        }
    }

    public void requestStateInfoInOut() {
        if (remoteProxy == null) return;
        StateInfo info = new StateInfo(300,400);
        try {
            remoteProxy.getStateInfo_inout(info);
            Log.d(TAG, "requestStateInfoInOut: " + info);
        } catch (RemoteException e) {
            Log.e(TAG, "requestStateInfoInOut Exception: " + e);
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


    private void registerMatrixServerCallBack() {
        Log.d(TAG, "registerMatrixServerCallBack() called, mIMatrixProxy=" + remoteProxy);
        try {
            remoteProxy.registerMatrixCallBack(mMatrixCallback);
            Log.d(TAG, "registerMatrixServerCallBack() success");
        } catch (RemoteException e) {
            Log.e(TAG, "registerMatrixServerCallBack Exception: " + e.getMessage());
        }
    }

    private void unregisterMatrixServerCallBack() {
        Log.d(TAG, "unregisterMatrixServerCallBack() called, mIMatrixProxy=" + remoteProxy);
        try {
            remoteProxy.unregisterMatrixCallBack(mMatrixCallback);
            Log.d(TAG, "unregisterMatrixCallBack() success");
        } catch (RemoteException e) {
            Log.e(TAG, "unregisterMatrixCallBack Exception: " + e.getMessage());
        }
    }
}
