package com.neo.matrixserver;

// IMatrixProxy.aidl
interface IMatrixProxy {
    int getState();                           // 获取当前状态
    void setState(int state);                 // 设置状态
}