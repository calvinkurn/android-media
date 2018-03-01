package com.tokopedia.transaction.checkout.data.entity.response.checkpromocodecartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class DataVoucher {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("promo_code_id")
    @Expose
    private int promoCodeId;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("cashback_amount")
    @Expose
    private int cashbackAmount;
    @SerializedName("saldo_amount")
    @Expose
    private int saldoAmount;
    @SerializedName("cashback_top_cash_amount")
    @Expose
    private int cashbackTopCashAmount;
    @SerializedName("cashback_voucher_amount")
    @Expose
    private int cashbackVoucherAmount;
    @SerializedName("extra_amount")
    @Expose
    private int extraAmount;
    @SerializedName("cashback_voucher_description")
    @Expose
    private String cashbackVoucherDescription;
    @SerializedName("lp")
    @Expose
    private Object lp;
    @SerializedName("gateway_id")
    @Expose
    private String gatewayId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("message_success")
    @Expose
    private String messageSuccess;

    public String getCode() {
        return code;
    }

    public int getPromoCodeId() {
        return promoCodeId;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public int getCashbackAmount() {
        return cashbackAmount;
    }

    public int getSaldoAmount() {
        return saldoAmount;
    }

    public int getCashbackTopCashAmount() {
        return cashbackTopCashAmount;
    }

    public int getCashbackVoucherAmount() {
        return cashbackVoucherAmount;
    }

    public int getExtraAmount() {
        return extraAmount;
    }

    public String getCashbackVoucherDescription() {
        return cashbackVoucherDescription;
    }

    public Object getLp() {
        return lp;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public String getToken() {
        return token;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }
}
