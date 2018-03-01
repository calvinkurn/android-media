package com.tokopedia.transaction.checkout.data.entity.response.shippingaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class ShippingAddressDataResponse {

    @SerializedName("success")
    @Expose
    private int success;

    public int getSuccess() {
        return success;
    }
}
