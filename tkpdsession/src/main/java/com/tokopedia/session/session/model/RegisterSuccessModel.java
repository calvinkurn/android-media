package com.tokopedia.session.session.model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 16/11/2015.
 */
@Parcel
public class RegisterSuccessModel implements Parcelable {
    @SerializedName("is_active")
    int isActive;
    @SerializedName("u_id")
    int userId;

    public static final int ACTIVATION_PROCESS = 0;
    public static final int CREATE_PASSWORD_PROCESS = 1;
    public static final int GO_TO_HOME_PROCESS = 2;

    public static final int USER_DELETED = -2;
    public static final int USER_PENDING = -1;
    public static final int USER_INACTIVE = 0;
    public static final int USER_ACTIVE = 1;
    public static final int USER_BANNED = 2;

    public int getIsActive() {
        return isActive;
    }

    public RegisterSuccessModel(){
        isActive = -1;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(this.isActive);
        dest.writeInt(this.userId);
    }

    protected RegisterSuccessModel(android.os.Parcel in) {
        this.isActive = in.readInt();
        this.userId = in.readInt();
    }

    public static final Parcelable.Creator<RegisterSuccessModel> CREATOR = new Parcelable.Creator<RegisterSuccessModel>() {
        @Override
        public RegisterSuccessModel createFromParcel(android.os.Parcel source) {
            return new RegisterSuccessModel(source);
        }

        @Override
        public RegisterSuccessModel[] newArray(int size) {
            return new RegisterSuccessModel[size];
        }
    };
}
