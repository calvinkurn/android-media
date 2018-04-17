package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 3/28/18. Tokopedia
 */

public class AutoApply implements Parcelable{

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("promo_code_id")
    @Expose
    private String promoCodeID;

    @SerializedName("discount_amount")
    @Expose
    private long discountAmount;

    @SerializedName("cashback_amount")
    @Expose
    private long cashbackAmount;

    @SerializedName("saldo_amount")
    @Expose
    private long saldoAmount;

    @SerializedName("cashback_top_cash_amount")
    @Expose
    private long cashBackTopCashAmount;

    @SerializedName("cashback_voucher_amount")
    @Expose
    private long cashbackVoucherAmount;

    @SerializedName("extra_amount")
    @Expose
    private long extraAmount;

    @SerializedName("cashback_voucher_description")
    @Expose
    private String cashbackVoucherDescription;

    @SerializedName("title_description")
    @Expose
    private String titleDescription;

    @SerializedName("message_success")
    @Expose
    private String messageSuccess;

    @SerializedName("is_coupon")
    @Expose
    private int isCoupon;

    @SerializedName("invoice_description")
    @Expose
    private String invoiceDescription;

    protected AutoApply(Parcel in) {
        success = in.readByte() != 0;
        code = in.readString();
        promoCodeID = in.readString();
        discountAmount = in.readLong();
        cashbackAmount = in.readLong();
        saldoAmount = in.readLong();
        cashBackTopCashAmount = in.readLong();
        cashbackVoucherAmount = in.readLong();
        extraAmount = in.readLong();
        cashbackVoucherDescription = in.readString();
        titleDescription = in.readString();
        messageSuccess = in.readString();
        isCoupon = in.readInt();
        invoiceDescription = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(code);
        dest.writeString(promoCodeID);
        dest.writeLong(discountAmount);
        dest.writeLong(cashbackAmount);
        dest.writeLong(saldoAmount);
        dest.writeLong(cashBackTopCashAmount);
        dest.writeLong(cashbackVoucherAmount);
        dest.writeLong(extraAmount);
        dest.writeString(cashbackVoucherDescription);
        dest.writeString(titleDescription);
        dest.writeString(messageSuccess);
        dest.writeInt(isCoupon);
        dest.writeString(invoiceDescription);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AutoApply> CREATOR = new Creator<AutoApply>() {
        @Override
        public AutoApply createFromParcel(Parcel in) {
            return new AutoApply(in);
        }

        @Override
        public AutoApply[] newArray(int size) {
            return new AutoApply[size];
        }
    };

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPromoCodeID() {
        return promoCodeID;
    }

    public void setPromoCodeID(String promoCodeID) {
        this.promoCodeID = promoCodeID;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public long getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(long cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    public long getSaldoAmount() {
        return saldoAmount;
    }

    public void setSaldoAmount(long saldoAmount) {
        this.saldoAmount = saldoAmount;
    }

    public long getCashBackTopCashAmount() {
        return cashBackTopCashAmount;
    }

    public void setCashBackTopCashAmount(long cashBackTopCashAmount) {
        this.cashBackTopCashAmount = cashBackTopCashAmount;
    }

    public long getCashbackVoucherAmount() {
        return cashbackVoucherAmount;
    }

    public void setCashbackVoucherAmount(long cashbackVoucherAmount) {
        this.cashbackVoucherAmount = cashbackVoucherAmount;
    }

    public long getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(long extraAmount) {
        this.extraAmount = extraAmount;
    }

    public String getCashbackVoucherDescription() {
        return cashbackVoucherDescription;
    }

    public void setCashbackVoucherDescription(String cashbackVoucherDescription) {
        this.cashbackVoucherDescription = cashbackVoucherDescription;
    }

    public String getTitleDescription() {
        return titleDescription;
    }

    public void setTitleDescription(String titleDescription) {
        this.titleDescription = titleDescription;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }

    public int getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(int isCoupon) {
        this.isCoupon = isCoupon;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }
}
