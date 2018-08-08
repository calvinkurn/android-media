package com.tokopedia.transaction.cart.model.toppaydata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anggaprasetiyo on 11/23/16.
 */

public class Parameter implements Parcelable {

    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("gateway_code")
    @Expose
    private String gatewayCode;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("merchant_code")
    @Expose
    private String merchantCode;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<Item>();
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("profile_code")
    @Expose
    private String profileCode;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getGatewayCode() {
        return gatewayCode;
    }

    public void setGatewayCode(String gatewayCode) {
        this.gatewayCode = gatewayCode;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.customerName);
        dest.writeString(this.gatewayCode);
        dest.writeString(this.transactionDate);
        dest.writeString(this.merchantCode);
        dest.writeString(this.signature);
        dest.writeString(this.amount);
        dest.writeString(this.currency);
        dest.writeString(this.customerEmail);
        dest.writeString(this.transactionId);
        dest.writeList(this.items);
        dest.writeString(this.language);
        dest.writeString(this.profileCode);
    }

    public Parameter() {
    }

    protected Parameter(Parcel in) {
        this.customerName = in.readString();
        this.gatewayCode = in.readString();
        this.transactionDate = in.readString();
        this.merchantCode = in.readString();
        this.signature = in.readString();
        this.amount = in.readString();
        this.currency = in.readString();
        this.customerEmail = in.readString();
        this.transactionId = in.readString();
        this.items = new ArrayList<Item>();
        in.readList(this.items, Item.class.getClassLoader());
        this.language = in.readString();
        this.profileCode = in.readString();
    }

    public static final Creator<Parameter> CREATOR = new Creator<Parameter>() {
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
