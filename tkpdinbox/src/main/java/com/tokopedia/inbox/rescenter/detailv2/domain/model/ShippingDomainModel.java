package com.tokopedia.inbox.rescenter.detailv2.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 3/17/17.
 */

public class ShippingDomainModel {
    private String shipmentRef;
    private String shipmentDate;
    private String shipmentID;
    private List<AwbAttachmentDomainModel> attachments;
    private String providerText;

    public String getShipmentRef() {
        return shipmentRef;
    }

    public void setShipmentRef(String shipmentRef) {
        this.shipmentRef = shipmentRef;
    }

    public String getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(String shipmentID) {
        this.shipmentID = shipmentID;
    }

    public List<AwbAttachmentDomainModel> getAttachment() {
        return attachments;
    }

    public void setAttachment(List<AwbAttachmentDomainModel> attachments) {
        this.attachments = attachments;
    }

    public void setProviderText(String providerText) {
        this.providerText = providerText;
    }

    public String getProviderText() {
        return providerText;
    }
}
