package com.tokopedia.inbox.rescenter.historyawb.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 3/24/17.
 */

public class HistoryAwbItemDomainData {
    private int actionBy;
    private String actionByText;
    private List<AttachmentAwbDomainData> attachmentList;
    private String date;
    private String createTimestamp;
    private String remark;
    private String conversationID;
    private String shipmentID;
    private String shippingRefNumber;
    private boolean buttonEdit;
    private boolean buttonTrack;

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionByText(String actionByText) {
        this.actionByText = actionByText;
    }

    public String getActionByText() {
        return actionByText;
    }

    public void setAttachmentList(List<AttachmentAwbDomainData> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<AttachmentAwbDomainData> getAttachmentList() {
        return attachmentList;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setShipmentID(String shipmentID) {
        this.shipmentID = shipmentID;
    }

    public String getShipmentID() {
        return shipmentID;
    }

    public void setShippingRefNumber(String shippingRefNumber) {
        this.shippingRefNumber = shippingRefNumber;
    }

    public String getShippingRefNumber() {
        return shippingRefNumber;
    }

    public void setButtonEdit(boolean buttonEdit) {
        this.buttonEdit = buttonEdit;
    }

    public boolean isButtonEdit() {
        return buttonEdit;
    }

    public void setButtonTrack(boolean buttonTrack) {
        this.buttonTrack = buttonTrack;
    }

    public boolean isButtonTrack() {
        return buttonTrack;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
}
