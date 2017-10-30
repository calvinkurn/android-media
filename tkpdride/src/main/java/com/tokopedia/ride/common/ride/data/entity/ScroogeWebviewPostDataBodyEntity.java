package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScroogeWebviewPostDataBodyEntity {

    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("callback_url")
    @Expose
    private String callbackUrl;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("merchant_code")
    @Expose
    private String merchantCode;
    @SerializedName("profile_code")
    @Expose
    private String profileCode;
    @SerializedName("signature")
    @Expose
    private String signature;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
