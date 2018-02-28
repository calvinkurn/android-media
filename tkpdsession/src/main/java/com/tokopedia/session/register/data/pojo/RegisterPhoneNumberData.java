package com.tokopedia.session.register.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author yfsx on 28/2/18.
 */

public class RegisterPhoneNumberData {

    @SerializedName("u_id")
    @Expose
    private int uId;
    @SerializedName("is_active")
    @Expose
    private int isActive;
    @SerializedName("action")
    @Expose
    private int action;
    @SerializedName("is_success")
    @Expose
    private int isSuccess;

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
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
