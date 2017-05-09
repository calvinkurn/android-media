package com.tokopedia.inbox.rescenter.detailv2.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 3/16/17.
 */

public class TrackingAwbReturProduct {
    private boolean success;
    private int errorCode;
    private String messageError;
    private String shippingRefNum;
    private String receiverName;
    private boolean delivered;
    private List<TrackingAwbReturProductHistory> trackingHistory;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getMessageError() {
        return messageError;
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

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public List<TrackingAwbReturProductHistory> getTrackingHistory() {
        return trackingHistory;
    }

    public void setTrackingHistory(List<TrackingAwbReturProductHistory> trackingHistory) {
        this.trackingHistory = trackingHistory;
    }
}
