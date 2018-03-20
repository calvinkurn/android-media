
package com.tokopedia.shop.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoPayment {

    @SerializedName("payment_default_status")
    @Expose
    private String paymentDefaultStatus;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("payment_image")
    @Expose
    private String paymentImage;
    @SerializedName("payment_info")
    @Expose
    private long paymentInfo;
    @SerializedName("payment_name")
    @Expose
    private String paymentName;

    public String getPaymentDefaultStatus() {
        return paymentDefaultStatus;
    }

    public void setPaymentDefaultStatus(String paymentDefaultStatus) {
        this.paymentDefaultStatus = paymentDefaultStatus;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentImage() {
        return paymentImage;
    }

    public void setPaymentImage(String paymentImage) {
        this.paymentImage = paymentImage;
    }

    public long getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(long paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

}
