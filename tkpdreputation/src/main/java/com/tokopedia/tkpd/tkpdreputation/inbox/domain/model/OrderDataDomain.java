package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class OrderDataDomain {

    private String invoiceRefNum;
    private CreateTimeFmtDomain createTimeFmt;
    private String invoiceUrl;

    public OrderDataDomain(String invoiceRefNum, CreateTimeFmtDomain createTimeFmt,
                           String invoiceUrl) {
        this.invoiceRefNum = invoiceRefNum;
        this.createTimeFmt = createTimeFmt;
        this.invoiceUrl = invoiceUrl;
    }

    public String getInvoiceRefNum() {
        return invoiceRefNum;
    }

    public CreateTimeFmtDomain getCreateTimeFmt() {
        return createTimeFmt;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }
}
