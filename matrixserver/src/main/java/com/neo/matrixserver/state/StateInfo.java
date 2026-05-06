package com.neo.matrixserver.state;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StateInfo implements Parcelable {

    private int userId;
    private int value;

    public StateInfo() {
    }

    public StateInfo(int userId, int value) {
        this.userId = userId;
        this.value = value;
    }

    protected StateInfo(Parcel in) {
        userId = in.readInt();
        value = in.readInt();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getValue() {
        return this.value;
    }

    public static final Creator<StateInfo> CREATOR = new Creator<StateInfo>() {
        @Override
        public StateInfo createFromParcel(Parcel in) {
            return new StateInfo(in);
        }

        @Override
        public StateInfo[] newArray(int size) {
            return new StateInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(value);
    }

    /**
     * 参数是一个Parcel,用它来存储与传输数据
     * 如果有 out 与 inout 参数，必须添加这个方法，不然生成aidl后编译不过
     */
    public void readFromParcel(Parcel in) {
        userId = in.readInt();
        value = in.readInt();
    }

    @NonNull
    @Override
    public String toString() {
        return "StateInfo {" +
                "userId:" + userId +
                ", value:" + value +
                '}';
    }
}
