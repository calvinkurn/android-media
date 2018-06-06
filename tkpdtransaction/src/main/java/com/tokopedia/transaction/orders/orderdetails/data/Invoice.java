package com.tokopedia.transaction.orders.orderdetails.data;

/**
 * Created by baghira on 11/05/18.
 */

public class Invoice {
    private String invoiceRefNum;
    private String invoiceUrl;

    public Invoice(String invoiceRefNum, String invoiceUrl) {
        this.invoiceRefNum = invoiceRefNum;
        this.invoiceUrl = invoiceUrl;
    }

    public String invoiceRefNum() {
        return invoiceRefNum;
    }

    public String invoiceUrl() {
        return invoiceUrl;
    }

    @Override
    public String toString() {
        return "[Invoice:{"
                + "invoiceRefNum="+invoiceRefNum +" "
                + "invoiceUrl="+invoiceUrl
                + "}]";
    }
}
