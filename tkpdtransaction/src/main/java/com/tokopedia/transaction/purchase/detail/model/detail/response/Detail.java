
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
    @SerializedName("drop_shipper")
    @Expose
    private DropShipper dropShipper;
    @SerializedName("checkout_date")
    @Expose
    private String checkoutDate;

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
}
