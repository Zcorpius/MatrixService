package com.neo.matrixserver;

import com.neo.matrixserver.state.StateInfo;
import com.neo.matrixserver.IMatrixCallback;

// IMatrixProxy.aidl
interface IMatrixProxy {
    int getState();
    void setState(int state);

    oneway void setState_aysc(int state);

    int getStateInfo_in(in StateInfo info);
    int getStateInfo_out(out StateInfo info);
    int getStateInfo_inout(inout StateInfo info);

    void registerMatrixCallBack(in IMatrixCallback callback);
    void unregisterMatrixCallBack(in IMatrixCallback callback);
}