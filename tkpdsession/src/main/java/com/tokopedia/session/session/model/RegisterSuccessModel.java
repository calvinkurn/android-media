package com.tokopedia.session.session.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 16/11/2015.
 */
@Parcel
public class RegisterSuccessModel {
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
}
