package com.tokopedia.inbox.inboxchat.domain.pojo.invoicesent;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 16/05/18.
 */

public class InvoiceSentPayloadPojo {

    @SerializedName("type_id")
    private int typeId;
    @SerializedName("type")
    private String type;
    @SerializedName("attributes")
    private PayloadAttributePojo attributes;

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PayloadAttributePojo getAttributes() {
        return attributes;
    }

    public void setAttributes(PayloadAttributePojo attributes) {
        this.attributes = attributes;
    }

}
