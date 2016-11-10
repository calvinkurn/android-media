
package com.tokopedia.core.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


@Parcel
public class OrderPayment {

    @SerializedName("payment_process_due_date")
    @Expose
    String paymentProcessDueDate;
    @SerializedName("payment_komisi")
    @Expose
    String paymentKomisi;
    @SerializedName("payment_verify_date")
    @Expose
    String paymentVerifyDate;
    @SerializedName("payment_shipping_due_date")
    @Expose
    String paymentShippingDueDate;
    @SerializedName("payment_process_day_left")
    @Expose
    Integer paymentProcessDayLeft;
    @SerializedName("payment_gateway_id")
    @Expose
    Integer paymentGatewayId;
    @SerializedName("payment_gateway_image")
    @Expose
    String paymentGatewayImage;
    @SerializedName("payment_shipping_day_left")
    @Expose
    Integer paymentShippingDayLeft;
    @SerializedName("payment_gateway_name")
    @Expose
    String paymentGatewayName;

    /**
     * 
     * @return
     *     The paymentProcessDueDate
     */
    public String getPaymentProcessDueDate() {
        return paymentProcessDueDate;
    }

    /**
     * 
     * @param paymentProcessDueDate
     *     The payment_process_due_date
     */
    public void setPaymentProcessDueDate(String paymentProcessDueDate) {
        this.paymentProcessDueDate = paymentProcessDueDate;
    }

    /**
     * 
     * @return
     *     The paymentKomisi
     */
    public String getPaymentKomisi() {
        return paymentKomisi;
    }

    /**
     * 
     * @param paymentKomisi
     *     The payment_komisi
     */
    public void setPaymentKomisi(String paymentKomisi) {
        this.paymentKomisi = paymentKomisi;
    }

    /**
     * 
     * @return
     *     The paymentVerifyDate
     */
    public String getPaymentVerifyDate() {
        return paymentVerifyDate;
    }

    /**
     * 
     * @param paymentVerifyDate
     *     The payment_verify_date
     */
    public void setPaymentVerifyDate(String paymentVerifyDate) {
        this.paymentVerifyDate = paymentVerifyDate;
    }

    /**
     * 
     * @return
     *     The paymentShippingDueDate
     */
    public String getPaymentShippingDueDate() {
        return paymentShippingDueDate;
    }

    /**
     * 
     * @param paymentShippingDueDate
     *     The payment_shipping_due_date
     */
    public void setPaymentShippingDueDate(String paymentShippingDueDate) {
        this.paymentShippingDueDate = paymentShippingDueDate;
    }

    /**
     * 
     * @return
     *     The paymentProcessDayLeft
     */
    public Integer getPaymentProcessDayLeft() {
        return paymentProcessDayLeft;
    }

    /**
     * 
     * @param paymentProcessDayLeft
     *     The payment_process_day_left
     */
    public void setPaymentProcessDayLeft(Integer paymentProcessDayLeft) {
        this.paymentProcessDayLeft = paymentProcessDayLeft;
    }

    /**
     * 
     * @return
     *     The paymentGatewayId
     */
    public Integer getPaymentGatewayId() {
        return paymentGatewayId;
    }

    /**
     * 
     * @param paymentGatewayId
     *     The payment_gateway_id
     */
    public void setPaymentGatewayId(Integer paymentGatewayId) {
        this.paymentGatewayId = paymentGatewayId;
    }

    /**
     * 
     * @return
     *     The paymentGatewayImage
     */
    public String getPaymentGatewayImage() {
        return paymentGatewayImage;
    }

    /**
     * 
     * @param paymentGatewayImage
     *     The payment_gateway_image
     */
    public void setPaymentGatewayImage(String paymentGatewayImage) {
        this.paymentGatewayImage = paymentGatewayImage;
    }

    /**
     * 
     * @return
     *     The paymentShippingDayLeft
     */
    public Integer getPaymentShippingDayLeft() {
        return paymentShippingDayLeft;
    }

    /**
     * 
     * @param paymentShippingDayLeft
     *     The payment_shipping_day_left
     */
    public void setPaymentShippingDayLeft(Integer paymentShippingDayLeft) {
        this.paymentShippingDayLeft = paymentShippingDayLeft;
    }

    /**
     * 
     * @return
     *     The paymentGatewayName
     */
    public String getPaymentGatewayName() {
        return paymentGatewayName;
    }

    /**
     * 
     * @param paymentGatewayName
     *     The payment_gateway_name
     */
    public void setPaymentGatewayName(String paymentGatewayName) {
        this.paymentGatewayName = paymentGatewayName;
    }

}
