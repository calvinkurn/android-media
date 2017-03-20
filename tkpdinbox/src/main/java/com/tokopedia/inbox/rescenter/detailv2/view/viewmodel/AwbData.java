package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import java.util.List;

/**
 * Created by hangnadi on 3/13/17.
 */

public class AwbData {

    private String shipmentRef;
    private String awbDate;
    private String shipmentID;
    private List<AwbAttachmentViewModel> attachments;

    public String getShipmentRef() {
        return shipmentRef;
    }

    public void setShipmentRef(String shipmentRef) {
        this.shipmentRef = shipmentRef;
    }

    public String getAwbDate() {
        return awbDate;
    }

    public void setAwbDate(String awbDate) {
        this.awbDate = awbDate;
    }


    public String getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(String shipmentID) {
        this.shipmentID = shipmentID;
    }

    public void setAttachments(List<AwbAttachmentViewModel> attachment) {
        this.attachments = attachment;
    }

    public List<AwbAttachmentViewModel> getAttachments() {
        return attachments;
    }
}
