package com.tokopedia.transaction.cart.model.thankstoppaydata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.payment.model.responsethankspayment.PriceDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 1/23/17.
 */

public class Parameter implements Parcelable {
    @SerializedName("voucher_amt")
    @Expose
    private String voucherAmt;
    @SerializedName("donation_amt")
    @Expose
    private String donationAmt;
    @SerializedName("price_detail")
    @Expose
    private List<PriceDetail> priceDetail = new ArrayList<PriceDetail>();
    @SerializedName("lp_amount")
    @Expose
    private String lpAmount;
    @SerializedName("payment_code")
    @Expose
    private String paymentCode;
    @SerializedName("payment_ref_num")
    @Expose
    private String paymentRefNum;
    @SerializedName("additional_notes")
    @Expose
    private String additionalNotes;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("gateway_name")
    @Expose
    private String gatewayName;
    @SerializedName("deposit_amt")
    @Expose
    private String depositAmt;
    @SerializedName("cashback")
    @Expose
    private String cashback;
    @SerializedName("additional_fee")
    @Expose
    private String additionalFee;
    @SerializedName("extra_fee_amt")
    @Expose
    private String extraFeeAmt;
    @SerializedName("order_open_amt")
    @Expose
    private String orderOpenAmt;
    @SerializedName("payment_left")
    @Expose
    private String paymentLeft;
    @SerializedName("gateway_id")
    @Expose
    private String gatewayId;
    @SerializedName("is_transfer")
    @Expose
    private Integer isTransfer;

    public String getVoucherAmt() {
        return voucherAmt;
    }

    public void setVoucherAmt(String voucherAmt) {
        this.voucherAmt = voucherAmt;
    }

    public String getDonationAmt() {
        return donationAmt;
    }

    public void setDonationAmt(String donationAmt) {
        this.donationAmt = donationAmt;
    }

    public List<PriceDetail> getPriceDetail() {
        return priceDetail;
    }

    public void setPriceDetail(List<PriceDetail> priceDetail) {
        this.priceDetail = priceDetail;
    }

    public String getLpAmount() {
        return lpAmount;
    }

    public void setLpAmount(String lpAmount) {
        this.lpAmount = lpAmount;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentRefNum() {
        return paymentRefNum;
    }

    public void setPaymentRefNum(String paymentRefNum) {
        this.paymentRefNum = paymentRefNum;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getDepositAmt() {
        return depositAmt;
    }

    public void setDepositAmt(String depositAmt) {
        this.depositAmt = depositAmt;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(String additionalFee) {
        this.additionalFee = additionalFee;
    }

    public String getExtraFeeAmt() {
        return extraFeeAmt;
    }

    public void setExtraFeeAmt(String extraFeeAmt) {
        this.extraFeeAmt = extraFeeAmt;
    }

    public String getOrderOpenAmt() {
        return orderOpenAmt;
    }

    public void setOrderOpenAmt(String orderOpenAmt) {
        this.orderOpenAmt = orderOpenAmt;
    }

    public String getPaymentLeft() {
        return paymentLeft;
    }

    public void setPaymentLeft(String paymentLeft) {
        this.paymentLeft = paymentLeft;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public Integer getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(Integer isTransfer) {
        this.isTransfer = isTransfer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.voucherAmt);
        dest.writeString(this.donationAmt);
        dest.writeList(this.priceDetail);
        dest.writeString(this.lpAmount);
        dest.writeString(this.paymentCode);
        dest.writeString(this.paymentRefNum);
        dest.writeString(this.additionalNotes);
        dest.writeString(this.paymentId);
        dest.writeString(this.gatewayName);
        dest.writeString(this.depositAmt);
        dest.writeString(this.cashback);
        dest.writeString(this.additionalFee);
        dest.writeString(this.extraFeeAmt);
        dest.writeString(this.orderOpenAmt);
        dest.writeString(this.paymentLeft);
        dest.writeString(this.gatewayId);
        dest.writeValue(this.isTransfer);
    }

    public Parameter() {
    }

    protected Parameter(Parcel in) {
        this.voucherAmt = in.readString();
        this.donationAmt = in.readString();
        this.priceDetail = new ArrayList<PriceDetail>();
        in.readList(this.priceDetail, PriceDetail.class.getClassLoader());
        this.lpAmount = in.readString();
        this.paymentCode = in.readString();
        this.paymentRefNum = in.readString();
        this.additionalNotes = in.readString();
        this.paymentId = in.readString();
        this.gatewayName = in.readString();
        this.depositAmt = in.readString();
        this.cashback = in.readString();
        this.additionalFee = in.readString();
        this.extraFeeAmt = in.readString();
        this.orderOpenAmt = in.readString();
        this.paymentLeft = in.readString();
        this.gatewayId = in.readString();
        this.isTransfer = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Parameter> CREATOR = new Parcelable.Creator<Parameter>() {
        @Override
        public Parameter createFromParcel(Parcel source) {
            return new Parameter(source);
        }

        @Override
        public Parameter[] newArray(int size) {
            return new Parameter[size];
        }
    };
}
