package com.tokopedia.inbox.attachinvoice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 28/03/18.
 */

public class GetInvoicesDataWrapper {
    @SerializedName("payload")
    @Expose
    GetInvoicesPayloadWrapper payloadWrapper;

    public GetInvoicesPayloadWrapper getPayloadWrapper() {
        return payloadWrapper;
    }

    public void setPayloadWrapper(GetInvoicesPayloadWrapper payloadWrapper) {
        this.payloadWrapper = payloadWrapper;
    }
}
