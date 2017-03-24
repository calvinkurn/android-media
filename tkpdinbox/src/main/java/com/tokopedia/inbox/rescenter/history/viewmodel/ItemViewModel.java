package com.tokopedia.inbox.rescenter.history.viewmodel;

import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public class ItemViewModel {
    private String provider;
    private String date;
    private String historyText;
    private boolean latest;
    private List<ShippingAttachment> attachments;
    private String shippingRefNumber;
    private String shipmentID;
    private String conversationID;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDate() {
        return date;
    }

    public String getHistoryText() {
        return historyText;
    }

    public void setHistoryText(String historyText) {
        this.historyText = historyText;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public List<ShippingAttachment> getAttachment() {
        return attachments;
    }

    public void setAttachment(List<ShippingAttachment> attachments) {
        this.attachments = attachments;
    }


    public String getShippingRefNumber() {
        return shippingRefNumber;
    }

    public void setShippingRefNumber(String shippingRefNumber) {
        this.shippingRefNumber = shippingRefNumber;
    }

    public String getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(String shipmentID) {
        this.shipmentID = shipmentID;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }
}
