package com.tokopedia.transaction.common.data.order;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 11/13/17. Tokopedia
 */

public class ButtonData implements Parcelable {

    private int askSellerVisibility;

    private int requestCancelVisibility;

    private int receiveConfirmationVisibility;

    private int finishOrderVisibility;

    private int complaintVisibility;

    private int cancelPeluangVisibility;

    private int orderDetailVisibility;

    private int askBuyerVisibility;

    private int acceptOrderVisibility;

    private int acceptPartialOrderVisibility;

    private int rejectOrderVisibility;

    private int rejectShipmentVisibility;

    private int confirmShippingVisibility;

    private int requestPickupVisibility;

    private int changeAwbVisibility;

    private int buyAgainVisibility;

    private int changeCourier;

    private int trackVisibility;

    private int viewComplaint;

    public ButtonData() {
    }

    protected ButtonData(Parcel in) {
        askSellerVisibility = in.readInt();
        requestCancelVisibility = in.readInt();
        receiveConfirmationVisibility = in.readInt();
        finishOrderVisibility = in.readInt();
        complaintVisibility = in.readInt();
        cancelPeluangVisibility = in.readInt();
        orderDetailVisibility = in.readInt();
        askBuyerVisibility = in.readInt();
        acceptOrderVisibility = in.readInt();
        acceptPartialOrderVisibility = in.readInt();
        rejectOrderVisibility = in.readInt();
        rejectShipmentVisibility = in.readInt();
        confirmShippingVisibility = in.readInt();
        requestPickupVisibility = in.readInt();
        changeAwbVisibility = in.readInt();
        buyAgainVisibility = in.readInt();
        changeCourier = in.readInt();
        trackVisibility = in.readInt();
        viewComplaint = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(askSellerVisibility);
        dest.writeInt(requestCancelVisibility);
        dest.writeInt(receiveConfirmationVisibility);
        dest.writeInt(finishOrderVisibility);
        dest.writeInt(complaintVisibility);
        dest.writeInt(cancelPeluangVisibility);
        dest.writeInt(orderDetailVisibility);
        dest.writeInt(askBuyerVisibility);
        dest.writeInt(acceptOrderVisibility);
        dest.writeInt(acceptPartialOrderVisibility);
        dest.writeInt(rejectOrderVisibility);
        dest.writeInt(rejectShipmentVisibility);
        dest.writeInt(confirmShippingVisibility);
        dest.writeInt(requestPickupVisibility);
        dest.writeInt(changeAwbVisibility);
        dest.writeInt(buyAgainVisibility);
        dest.writeInt(changeCourier);
        dest.writeInt(trackVisibility);
        dest.writeInt(viewComplaint);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ButtonData> CREATOR = new Creator<ButtonData>() {
        @Override
        public ButtonData createFromParcel(Parcel in) {
            return new ButtonData(in);
        }

        @Override
        public ButtonData[] newArray(int size) {
            return new ButtonData[size];
        }
    };

    public int getAskSellerVisibility() {
        return askSellerVisibility;
    }

    public void setAskSellerVisibility(int askSellerVisibility) {
        this.askSellerVisibility = askSellerVisibility;
    }

    public int getRequestCancelVisibility() {
        return requestCancelVisibility;
    }

    public void setRequestCancelVisibility(int requestCancelVisibility) {
        this.requestCancelVisibility = requestCancelVisibility;
    }

    public int getReceiveConfirmationVisibility() {
        return receiveConfirmationVisibility;
    }

    public void setReceiveConfirmationVisibility(int receiveConfirmationVisibility) {
        this.receiveConfirmationVisibility = receiveConfirmationVisibility;
    }

    public int getFinishOrderVisibility() {
        return finishOrderVisibility;
    }

    public void setFinishOrderVisibility(int finishOrderVisibility) {
        this.finishOrderVisibility = finishOrderVisibility;
    }

    public int getComplaintVisibility() {
        return complaintVisibility;
    }

    public void setComplaintVisibility(int complaintVisibility) {
        this.complaintVisibility = complaintVisibility;
    }

    public int getCancelPeluangVisibility() {
        return cancelPeluangVisibility;
    }

    public void setCancelPeluangVisibility(int cancelPeluangVisibility) {
        this.cancelPeluangVisibility = cancelPeluangVisibility;
    }

    public int getOrderDetailVisibility() {
        return orderDetailVisibility;
    }

    public void setOrderDetailVisibility(int orderDetailVisibility) {
        this.orderDetailVisibility = orderDetailVisibility;
    }

    public int getAskBuyerVisibility() {
        return askBuyerVisibility;
    }

    public void setAskBuyerVisibility(int askBuyerVisibility) {
        this.askBuyerVisibility = askBuyerVisibility;
    }

    public int getAcceptOrderVisibility() {
        return acceptOrderVisibility;
    }

    public void setAcceptOrderVisibility(int acceptOrderVisibility) {
        this.acceptOrderVisibility = acceptOrderVisibility;
    }

    public int getAcceptPartialOrderVisibility() {
        return acceptPartialOrderVisibility;
    }

    public void setAcceptPartialOrderVisibility(int acceptPartialOrderVisibility) {
        this.acceptPartialOrderVisibility = acceptPartialOrderVisibility;
    }

    public int getRejectOrderVisibility() {
        return rejectOrderVisibility;
    }

    public void setRejectOrderVisibility(int rejectOrderVisibility) {
        this.rejectOrderVisibility = rejectOrderVisibility;
    }

    public int getRejectShipmentVisibility() {
        return rejectShipmentVisibility;
    }

    public void setRejectShipmentVisibility(int rejectShipmentVisibility) {
        this.rejectShipmentVisibility = rejectShipmentVisibility;
    }

    public int getConfirmShippingVisibility() {
        return confirmShippingVisibility;
    }

    public void setConfirmShippingVisibility(int confirmShippingVisibility) {
        this.confirmShippingVisibility = confirmShippingVisibility;
    }

    public int getRequestPickupVisibility() {
        return requestPickupVisibility;
    }

    public void setRequestPickupVisibility(int requestPickupVisibility) {
        this.requestPickupVisibility = requestPickupVisibility;
    }

    public int getChangeAwbVisibility() {
        return changeAwbVisibility;
    }

    public void setChangeAwbVisibility(int changeAwbVisibility) {
        this.changeAwbVisibility = changeAwbVisibility;
    }

    public int getChangeCourier() {
        return changeCourier;
    }

    public void setChangeCourier(int changeCourier) {
        this.changeCourier = changeCourier;
    }

    public int getTrackVisibility() {
        return trackVisibility;
    }

    public void setTrackVisibility(int trackVisibility) {
        this.trackVisibility = trackVisibility;
    }

    public int getViewComplaint() {
        return viewComplaint;
    }

    public void setViewComplaint(int viewComplaint) {
        this.viewComplaint = viewComplaint;
    }

    public int getBuyAgainVisibility() {
        return buyAgainVisibility;
    }

    public void setBuyAgainVisibility(int buyAgainVisibility) {
        this.buyAgainVisibility = buyAgainVisibility;
    }
}
