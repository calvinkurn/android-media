package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Vishal Gupta on 11/10/17.
 */

public class PayPendingEntity {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("merchant_code")
    @Expose
    private String merchantCode;
    @SerializedName("profile_code")
    @Expose
    private String profileCode;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("items[name]")
    @Expose
    private List<String> itemsName = null;
    @SerializedName("items[quantity]")
    @Expose
    private List<String> itemsQuantity = null;
    @SerializedName("items[price]")
    @Expose
    private List<String> itemsPrice = null;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("user_defined_value")
    @Expose
    private String userDefinedValue;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("hitPG")
    @Expose
    private String hitPG;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<String> getItemsName() {
        return itemsName;
    }

    public void setItemsName(List<String> itemsName) {
        this.itemsName = itemsName;
    }

    public List<String> getItemsQuantity() {
        return itemsQuantity;
    }

    public void setItemsQuantity(List<String> itemsQuantity) {
        this.itemsQuantity = itemsQuantity;
    }

    public List<String> getItemsPrice() {
        return itemsPrice;
    }

    public void setItemsPrice(List<String> itemsPrice) {
        this.itemsPrice = itemsPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUserDefinedValue() {
        return userDefinedValue;
    }

    public void setUserDefinedValue(String userDefinedValue) {
        this.userDefinedValue = userDefinedValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHitPG() {
        return hitPG;
    }

    public void setHitPG(String hitPG) {
        this.hitPG = hitPG;
    }
}
