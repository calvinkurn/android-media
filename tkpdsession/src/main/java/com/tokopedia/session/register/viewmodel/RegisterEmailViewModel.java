package com.tokopedia.session.register.viewmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 4/13/17.
 */

public class RegisterEmailViewModel {

    public static final int USER_DELETED = -2;
    public static final int USER_PENDING = -1;
    public static final int USER_INACTIVE = 0;
    public static final int USER_ACTIVE = 1;
    public static final int USER_BANNED = 2;

    private int isActive;
    private int userId;
    private int action;
    private int isSuccess;

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

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
