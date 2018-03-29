package com.tokopedia.inbox.attachinvoice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 21/03/18.
 */

public class InvoicesDataModel {
    @SerializedName("type_id")
    @Expose

    int typeId;
    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("attributes")
    @Expose
    InvoiceAttributesDataModel attribute;

    public InvoicesDataModel(int typeId, String type, InvoiceAttributesDataModel attribute) {
        this.typeId = typeId;
        this.type = type;
        this.attribute = attribute;
    }

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

    public InvoiceAttributesDataModel getAttribute() {
        return attribute;
    }

    public void setAttribute(InvoiceAttributesDataModel attribute) {
        this.attribute = attribute;
    }
}
