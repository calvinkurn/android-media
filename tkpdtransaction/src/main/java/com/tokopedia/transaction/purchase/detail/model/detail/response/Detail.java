
package com.tokopedia.transaction.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("payment_verified_date")
    @Expose
    private String paymentVerifiedDate;
    @SerializedName("partial_order")
    @Expose
    private int partialOrder;
    @SerializedName("preorder")
    @Expose
    private Preorder preorder;
    @SerializedName("shipment")
    @Expose
    private Shipment shipment;
    @SerializedName("receiver")
    @Expose
    private Receiver receiver;
    @SerializedName("deadline")
    @Expose
    private Deadline deadline;
    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("request_cancel")
    @Expose
    private int requestCancel;
    @SerializedName("request_cancel_reason")
    @Expose
    private String requestCancelReason;
    @SerializedName("drop_shipper")
    @Expose
    private DropShipper dropShipper;
    @SerializedName("checkout_date")
    @Expose
    private String checkoutDate;
    @SerializedName("insurance")
    @Expose
    private Insurance insurance;

    public String getPaymentVerifiedDate() {
        return paymentVerifiedDate;
    }

    public void setPaymentVerifiedDate(String paymentVerifiedDate) {
        this.paymentVerifiedDate = paymentVerifiedDate;
    }

    public int getPartialOrder() {
        return partialOrder;
    }

    public void setPartialOrder(int partialOrder) {
        this.partialOrder = partialOrder;
    }

    public Preorder getPreorder() {
        return preorder;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    public void setDeadline(Deadline deadline) {
        this.deadline = deadline;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getRequestCancel() {
        return requestCancel;
    }

    public void setRequestCancel(int requestCancel) {
        this.requestCancel = requestCancel;
    }

    public String getRequestCancelReason() {
        return requestCancelReason;
    }

    public void setRequestCancelReason(String requestCancelReason) {
        this.requestCancelReason = requestCancelReason;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public DropShipper getDropShipper() {
        return dropShipper;
    }

    public void setDropShipper(DropShipper dropShipper) {
        this.dropShipper = dropShipper;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }
}
