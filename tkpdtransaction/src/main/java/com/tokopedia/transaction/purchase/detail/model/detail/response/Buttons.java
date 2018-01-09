
package com.tokopedia.transaction.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Buttons {

    @SerializedName("ask_seller")
    @Expose
    private int askSeller;
    @SerializedName("request_cancel")
    @Expose
    private int requestCancel;
    @SerializedName("receive_confirmation")
    @Expose
    private int receiveConfirmation;
    @SerializedName("finish_order")
    @Expose
    private int finishOrder;
    @SerializedName("complaint")
    @Expose
    private int complaint;
    @SerializedName("cancel_peluang")
    @Expose
    private int cancelPeluang;
    @SerializedName("order_detail")
    @Expose
    private int orderDetail;
    @SerializedName("ask_buyer")
    @Expose
    private int askBuyer;
    @SerializedName("accept_order")
    @Expose
    private int acceptOrder;
    @SerializedName("accept_partial")
    @Expose
    private int acceptPartial;
    @SerializedName("reject_order")
    @Expose
    private int rejectOrder;
    @SerializedName("reject_shipping")
    @Expose
    private int rejectShipment;
    @SerializedName("confirm_shipping")
    @Expose
    private int confirmShipping;
    @SerializedName("request_pickup")
    @Expose
    private int requestPickup;
    @SerializedName("change_awb")
    @Expose
    private int changeAwb;
    @SerializedName("change_courier")
    @Expose
    private int changeCourier;
    @SerializedName("track")
    @Expose
    private int track;
    @SerializedName("view_complaint")
    @Expose
    private int viewComplaint;

    public int getAskSeller() {
        return askSeller;
    }

    public int getRequestCancel() {
        return requestCancel;
    }

    public int getReceiveConfirmation() {
        return receiveConfirmation;
    }

    public int getFinishOrder() {
        return finishOrder;
    }

    public int getComplaint() {
        return complaint;
    }

    public int getCancelPeluang() {
        return cancelPeluang;
    }

    public int getOrderDetail() {
        return orderDetail;
    }

    public int getAskBuyer() {
        return askBuyer;
    }

    public int getAcceptOrder() {
        return acceptOrder;
    }

    public int getAcceptOrderPartial() {
        return acceptPartial;
    }

    public int getRejectOrder() {
        return rejectOrder;
    }

    public int getRejectShipment() {
        return rejectShipment;
    }

    public int getConfirmShipping() {
        return confirmShipping;
    }

    public int getRequestPickup() {
        return requestPickup;
    }

    public int getChangeAwb() {
        return changeAwb;
    }

    public int getChangeCourier() {
        return changeCourier;
    }

    public int getTrack() {
        return track;
    }

    public int getViewComplaint() {
        return viewComplaint;
    }

}
