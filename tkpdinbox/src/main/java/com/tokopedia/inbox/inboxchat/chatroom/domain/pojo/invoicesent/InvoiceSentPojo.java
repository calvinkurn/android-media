package com.tokopedia.inbox.inboxchat.domain.pojo.invoicesent;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 16/05/18.
 */

public class InvoiceSentPojo {

    @SerializedName("invoice_link")
    private InvoiceLinkPojo invoiceLink;

    public InvoiceLinkPojo getInvoiceLink() {
        return invoiceLink;
    }

    public void setInvoiceLink(InvoiceLinkPojo invoiceLink) {
        this.invoiceLink = invoiceLink;
    }

}
