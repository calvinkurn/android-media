package com.tokopedia.transaction.checkout.data.entity.response.updatestatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class UpdateStatusCartDataResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private int success;

    public String getMessage() {
        return message;
    }

    public int getSuccess() {
        return success;
    }
}
