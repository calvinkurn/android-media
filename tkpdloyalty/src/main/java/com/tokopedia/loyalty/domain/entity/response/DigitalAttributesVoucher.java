package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 12/8/17. Tokopedia
 */

public class DigitalAttributesVoucher {

    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("discount_amount_plain")
    @Expose
    private int discountAmountPlain;
    @SerializedName("cashback_amount")
    @Expose
    private String cashbackAmount;
    @SerializedName("cashback_amount_plain")
    @Expose
    private int cashbackAmountPlain;
    @SerializedName("message")
    @Expose
    private String message;

    public String getVoucherCode() {
        return voucherCode;
    }

    public int getUserId() {
        return userId;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public int getDiscountAmountPlain() {
        return discountAmountPlain;
    }

    public String getCashbackAmount() {
        return cashbackAmount;
    }

    public int getCashbackAmountPlain() {
        return cashbackAmountPlain;
    }

    public String getMessage() {
        return message;
    }

}
