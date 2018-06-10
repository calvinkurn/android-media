package com.tokopedia.inbox.attachinvoice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hendri on 28/03/18.
 */

public class GetInvoicesPayloadWrapper {

    @SerializedName("has_next")
    @Expose
    boolean hasNext;

    @SerializedName("invoices")
    @Expose
    List<InvoicesDataModel> invoices;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<InvoicesDataModel> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoicesDataModel> invoices) {
        this.invoices = invoices;
    }
}
