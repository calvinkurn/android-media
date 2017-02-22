package com.tokopedia.session.register.model.gson;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by nisie on 1/30/17.
 */

@Parcel
public class RegisterResult {
    public static final int USER_DELETED = -2;
    public static final int USER_PENDING = -1;
    public static final int USER_INACTIVE = 0;
    public static final int USER_ACTIVE = 1;
    public static final int USER_BANNED = 2;

    @SerializedName("is_active")
    int isActive;
    @SerializedName("u_id")
    int userId;

    public RegisterResult(){
        isActive = -1;
    }

    public int getIsActive() {
        return isActive;
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
