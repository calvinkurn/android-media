package com.tokopedia.inbox.inboxchat.domain.pojo.invoiceselection;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hendri on 16/05/18.
 */
public class InvoicesSelectionPojo {
    @SerializedName("invoices")
    private List<InvoicesSelectionSingleItemPojo> invoices;

    public List<InvoicesSelectionSingleItemPojo> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoicesSelectionSingleItemPojo> invoices) {
        this.invoices = invoices;
    }
}
