
package com.tokopedia.transaction.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Buttons {

    @SerializedName("ask_seller")
    @Expose
    private Integer askSeller;
    @SerializedName("request_cancel")
    @Expose
    private Integer requestCancel;
    @SerializedName("receive_confirmation")
    @Expose
    private Integer receiveConfirmation;
    @SerializedName("finish_order")
    @Expose
    private Integer finishOrder;
    @SerializedName("complaint")
    @Expose
    private Integer complaint;
    @SerializedName("cancel_peluang")
    @Expose
    private Integer cancelPeluang;
    @SerializedName("order_detail")
    @Expose
    private Integer orderDetail;
    @SerializedName("ask_buyer")
    @Expose
    private Integer askBuyer;
    @SerializedName("accept_order")
    @Expose
    private Integer acceptOrder;
    @SerializedName("reject_order")
    @Expose
    private Integer rejectOrder;
    @SerializedName("confirm_shipping")
    @Expose
    private Integer confirmShipping;
    @SerializedName("request_pickup")
    @Expose
    private Integer requestPickup;
    @SerializedName("change_awb")
    @Expose
    private Integer changeAwb;
    @SerializedName("change_courier")
    @Expose
    private Integer changeCourier;
    @SerializedName("track")
    @Expose
    private Integer track;
    @SerializedName("view_complaint")
    @Expose
    private Integer viewComplaint;

    public Integer getAskSeller() {
        return askSeller;
    }

    public void setAskSeller(Integer askSeller) {
        this.askSeller = askSeller;
    }

    public Integer getRequestCancel() {
        return requestCancel;
    }

    public void setRequestCancel(Integer requestCancel) {
        this.requestCancel = requestCancel;
    }

    public Integer getReceiveConfirmation() {
        return receiveConfirmation;
    }

    public void setReceiveConfirmation(Integer receiveConfirmation) {
        this.receiveConfirmation = receiveConfirmation;
    }

    public Integer getFinishOrder() {
        return finishOrder;
    }

    public void setFinishOrder(Integer finishOrder) {
        this.finishOrder = finishOrder;
    }

    public Integer getComplaint() {
        return complaint;
    }

    public void setComplaint(Integer complaint) {
        this.complaint = complaint;
    }

    public Integer getCancelPeluang() {
        return cancelPeluang;
    }

    public void setCancelPeluang(Integer cancelPeluang) {
        this.cancelPeluang = cancelPeluang;
    }

    public Integer getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(Integer orderDetail) {
        this.orderDetail = orderDetail;
    }

    public Integer getAskBuyer() {
        return askBuyer;
    }

    public void setAskBuyer(Integer askBuyer) {
        this.askBuyer = askBuyer;
    }

    public Integer getAcceptOrder() {
        return acceptOrder;
    }

    public void setAcceptOrder(Integer acceptOrder) {
        this.acceptOrder = acceptOrder;
    }

    public Integer getRejectOrder() {
        return rejectOrder;
    }

    public void setRejectOrder(Integer rejectOrder) {
        this.rejectOrder = rejectOrder;
    }

    public Integer getConfirmShipping() {
        return confirmShipping;
    }

    public void setConfirmShipping(Integer confirmShipping) {
        this.confirmShipping = confirmShipping;
    }

    public Integer getRequestPickup() {
        return requestPickup;
    }

    public void setRequestPickup(Integer requestPickup) {
        this.requestPickup = requestPickup;
    }

    public Integer getChangeAwb() {
        return changeAwb;
    }

    public void setChangeAwb(Integer changeAwb) {
        this.changeAwb = changeAwb;
    }

    public Integer getChangeCourier() {
        return changeCourier;
    }

    public void setChangeCourier(Integer changeCourier) {
        this.changeCourier = changeCourier;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }

    public Integer getViewComplaint() {
        return viewComplaint;
    }

    public void setViewComplaint(Integer viewComplaint) {
        this.viewComplaint = viewComplaint;
    }

}
