package com.tokopedia.transaction.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 20/06/2016.
 */
public class Order {
    private static final String TAG = Order.class.getSimpleName();

    @SerializedName("order_left_amount_idr")
    @Expose
    private String orderLeftAmountIdr;
    @SerializedName("order_deposit_used_idr")
    @Expose
    private String orderDepositUsedIdr;
    @SerializedName("order_invoice")
    @Expose
    private String orderInvoice;
    @SerializedName("order_confirmation_code_idr")
    @Expose
    private String orderConfirmationCodeIdr;
    @SerializedName("order_grand_total_idr")
    @Expose
    private String orderGrandTotalIdr;
    @SerializedName("order_left_amount")
    @Expose
    private String orderLeftAmount;
    @SerializedName("order_confirmation_code")
    @Expose
    private String orderConfirmationCode;
    @SerializedName("order_deposit_used")
    @Expose
    private String orderDepositUsed;
    @SerializedName("order_depositable")
    @Expose
    private String orderDepositable;
    @SerializedName("order_grand_total")
    @Expose
    private String orderGrandTotal;

    public String getOrderLeftAmountIdr() {
        return orderLeftAmountIdr;
    }

    public void setOrderLeftAmountIdr(String orderLeftAmountIdr) {
        this.orderLeftAmountIdr = orderLeftAmountIdr;
    }

    public String getOrderDepositUsedIdr() {
        return orderDepositUsedIdr;
    }

    public void setOrderDepositUsedIdr(String orderDepositUsedIdr) {
        this.orderDepositUsedIdr = orderDepositUsedIdr;
    }

    public String getOrderInvoice() {
        return orderInvoice;
    }

    public void setOrderInvoice(String orderInvoice) {
        this.orderInvoice = orderInvoice;
    }

    public String getOrderConfirmationCodeIdr() {
        return orderConfirmationCodeIdr;
    }

    public void setOrderConfirmationCodeIdr(String orderConfirmationCodeIdr) {
        this.orderConfirmationCodeIdr = orderConfirmationCodeIdr;
    }

    public String getOrderGrandTotalIdr() {
        return orderGrandTotalIdr;
    }

    public void setOrderGrandTotalIdr(String orderGrandTotalIdr) {
        this.orderGrandTotalIdr = orderGrandTotalIdr;
    }

    public String getOrderLeftAmount() {
        return orderLeftAmount;
    }

    public void setOrderLeftAmount(String orderLeftAmount) {
        this.orderLeftAmount = orderLeftAmount;
    }

    public String getOrderConfirmationCode() {
        return orderConfirmationCode;
    }

    public void setOrderConfirmationCode(String orderConfirmationCode) {
        this.orderConfirmationCode = orderConfirmationCode;
    }

    public String getOrderDepositUsed() {
        return orderDepositUsed;
    }

    public void setOrderDepositUsed(String orderDepositUsed) {
        this.orderDepositUsed = orderDepositUsed;
    }

    public String getOrderDepositable() {
        return orderDepositable;
    }

    public void setOrderDepositable(String orderDepositable) {
        this.orderDepositable = orderDepositable;
    }

    public String getOrderGrandTotal() {
        return orderGrandTotal;
    }

    public void setOrderGrandTotal(String orderGrandTotal) {
        this.orderGrandTotal = orderGrandTotal;
    }
}
