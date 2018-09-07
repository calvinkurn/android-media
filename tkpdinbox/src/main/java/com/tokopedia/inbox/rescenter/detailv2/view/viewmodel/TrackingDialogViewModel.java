package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import java.util.List;

/**
 * Created by hangnadi on 3/16/17.
 */

public class TrackingDialogViewModel {

    private boolean delivered;
    private String shippingRefNum;
    private String receiverName;
    private List<TrackingHistoryDialogViewModel> trackHistory;
    private boolean timeOut;
    private String messageError;
    private boolean success;

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getShippingRefNum() {
        return shippingRefNum;
    }

    public void setShippingRefNum(String shippingRefNum) {
        this.shippingRefNum = shippingRefNum;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public List<TrackingHistoryDialogViewModel> getTrackHistory() {
        return trackHistory;
    }

    public void setTrackHistory(List<TrackingHistoryDialogViewModel> trackHistory) {
        this.trackHistory = trackHistory;
    }

    public boolean isTimeOut() {
        return timeOut;
    }

    public void setTimeOut(boolean timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
