package com.tokopedia.transaction.checkout.domain.response.checkpromocodecartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class CheckPromoCodeCartListDataResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data_voucher")
    @Expose
    private DataVoucher dataVoucher;

    public String getError() {
        return error;
    }

    public DataVoucher getDataVoucher() {
        return dataVoucher;
    }
}
