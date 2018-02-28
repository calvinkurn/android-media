package com.tokopedia.transaction.checkout.data.entity.response.checkpromocodefinal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class DataVoucher {

    @SerializedName("voucher_amount_idr")
    @Expose
    private String voucherAmountIdr;
    @SerializedName("voucher_no_other_promotion")
    @Expose
    private int voucherNoOtherPromotion;
    @SerializedName("voucher_amount")
    @Expose
    private int voucherAmount;
    @SerializedName("voucher_status")
    @Expose
    private int voucherStatus;
    @SerializedName("voucher_promo_desc")
    @Expose
    private String voucherPromoDesc;

    public String getVoucherAmountIdr() {
        return voucherAmountIdr;
    }

    public int getVoucherNoOtherPromotion() {
        return voucherNoOtherPromotion;
    }

    public int getVoucherAmount() {
        return voucherAmount;
    }

    public int getVoucherStatus() {
        return voucherStatus;
    }

    public String getVoucherPromoDesc() {
        return voucherPromoDesc;
    }
}
