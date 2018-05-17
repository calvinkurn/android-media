package com.tokopedia.inbox.inboxchat.domain.pojo.invoicesent;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 16/05/18.
 */
public class InvoiceLinkPojo {

    @SerializedName("attributes")
    private InvoiceLinkAttributePojo attributes;
    @SerializedName("type")
    private String type;
    @SerializedName("type_id")
    private int typeId;

    public InvoiceLinkAttributePojo getAttributes() {
        return attributes;
    }

    public void setAttributes(InvoiceLinkAttributePojo attributes) {
        this.attributes = attributes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

}
