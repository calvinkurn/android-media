package com.tokopedia.ride.common.ride.domain.model;

import android.os.Bundle;

public class ScroogeWebviewPostDataBody {

    private String ipAddress;
    private String date;
    private String userId;
    private String customerEmail;
    private String customerName;
    private String callbackUrl;
    private String token;
    private String merchantCode;
    private String profileCode;
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

    public Bundle getBundle() {
        Bundle params = new Bundle();
        params.putString("merchant_code", merchantCode);
        params.putString("profile_code", profileCode);
        params.putString("ip_address", ipAddress);
        params.putString("date", date);
        params.putString("user_id", userId);
        params.putString("customer_email", customerEmail);
        params.putString("customer_name", customerName);
        params.putString("callback_url", callbackUrl);
        params.putString("signature", signature);
        params.putString("token" , token);
        return params;
    }

}
