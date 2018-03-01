package com.tokopedia.transaction.checkout.data.entity.response.updatecart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class UpdateCartDataResponse {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("goto")
    @Expose
    private int _goto;

    public String getError() {
        return error;
    }

    public boolean isStatus() {
        return status;
    }

    public int get_goto() {
        return _goto;
    }
}
