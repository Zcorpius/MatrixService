package com.neo.matrixserver;

import com.neo.matrixserver.state.StateInfo;

// IMatrixProxy.aidl
interface IMatrixProxy {
    int getState();
    void setState(int state);

    oneway void setState_aysc(int state);

    int getStateInfo_in(in StateInfo info);
    int getStateInfo_out(out StateInfo info);
    int getStateInfo_inout(inout StateInfo info);
}