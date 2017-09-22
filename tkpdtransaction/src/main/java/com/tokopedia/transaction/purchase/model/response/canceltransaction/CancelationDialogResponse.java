package com.tokopedia.transaction.purchase.model.response.canceltransaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 9/13/17. Tokopedia
 */

public class CancelationDialogResponse {

    @SerializedName("success")
    @Expose
    private int success;

    @SerializedName("data")
    @Expose
    private CancelationDialogData data;

    public int getResponseCode() {
        return success;
    }

    public void setSuccess(int status) {
        this.success = status;
    }

    public CancelationDialogData getData() {
        return data;
    }

    public void setMessage(CancelationDialogData message) {
        this.data = data;
    }

}
