package com.tokopedia.transaction.purchase.model.response.txconfirmation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 12/05/2016.
 */
public class Confirmation implements Parcelable {
    private static final String TAG = Confirmation.class.getSimpleName();

    @SerializedName("left_amount")
    @Expose
    private String leftAmount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pay_due_date")
    @Expose
    private String payDueDate;
    @SerializedName("total_weight")
    @Expose
    private String totalWeight;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("open_amount_before_fee")
    @Expose
    private String openAmountBeforeFee;
    @SerializedName("confirmation_id")
    @Expose
    private String confirmationId;
    @SerializedName("deposit_amount")
    @Expose
    private String depositAmount;
    @SerializedName("open_amount")
    @Expose
    private String openAmount;
    @SerializedName("deposit_amount_plain")
    @Expose
    private String depositAmountPlain;
    @SerializedName("voucher_amount")
    @Expose
    private String voucherAmount;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("total_item")
    @Expose
    private String totalItem;
    @SerializedName("shop_list")
    @Expose
    private String shopList;

    public String getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(String leftAmount) {
        this.leftAmount = leftAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayDueDate() {
        return payDueDate;
    }

    public void setPayDueDate(String payDueDate) {
        this.payDueDate = payDueDate;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOpenAmountBeforeFee() {
        return openAmountBeforeFee;
    }

    public void setOpenAmountBeforeFee(String openAmountBeforeFee) {
        this.openAmountBeforeFee = openAmountBeforeFee;
    }

    public String getConfirmationId() {
        return confirmationId;
    }

    public void setConfirmationId(String confirmationId) {
        this.confirmationId = confirmationId;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getOpenAmount() {
        return openAmount;
    }

    public void setOpenAmount(String openAmount) {
        this.openAmount = openAmount;
    }

    public String getDepositAmountPlain() {
        return depositAmountPlain;
    }

    public void setDepositAmountPlain(String depositAmountPlain) {
        this.depositAmountPlain = depositAmountPlain;
    }

    public String getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(String voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(String totalItem) {
        this.totalItem = totalItem;
    }

    public String getShopList() {
        return shopList;
    }

    public void setShopList(String shopList) {
        this.shopList = shopList;
    }

    protected Confirmation(Parcel in) {
        leftAmount = in.readString();
        status = in.readString();
        payDueDate = in.readString();
        totalWeight = in.readString();
        createTime = in.readString();
        openAmountBeforeFee = in.readString();
        confirmationId = in.readString();
        depositAmount = in.readString();
        openAmount = in.readString();
        depositAmountPlain = in.readString();
        voucherAmount = in.readString();
        customerId = in.readString();
        paymentType = in.readString();
        totalItem = in.readString();
        shopList = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(leftAmount);
        dest.writeString(status);
        dest.writeString(payDueDate);
        dest.writeString(totalWeight);
        dest.writeString(createTime);
        dest.writeString(openAmountBeforeFee);
        dest.writeString(confirmationId);
        dest.writeString(depositAmount);
        dest.writeString(openAmount);
        dest.writeString(depositAmountPlain);
        dest.writeString(voucherAmount);
        dest.writeString(customerId);
        dest.writeString(paymentType);
        dest.writeString(totalItem);
        dest.writeString(shopList);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Confirmation> CREATOR = new Parcelable.Creator<Confirmation>() {
        @Override
        public Confirmation createFromParcel(Parcel in) {
            return new Confirmation(in);
        }

        @Override
        public Confirmation[] newArray(int size) {
            return new Confirmation[size];
        }
    };
}
