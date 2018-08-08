package com.tokopedia.seller.orderstatus.model;

import com.tokopedia.core.purchase.model.response.txlist.OrderHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 1/26/17. Tokopedia
 */

public class InvoiceModel {

    private String orderId = "";

    private String userId = "";

    private String invoicePdf = "";

    private String invoiceUrl = "";

    private List<OrderHistory> statusList = new ArrayList<>();

    private String referenceNumber = "";

    private String permission = "0";

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInvoicePdf() {
        return invoicePdf;
    }

    public void setInvoicePdf(String invoicePdf) {
        this.invoicePdf = invoicePdf;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public List<OrderHistory> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<OrderHistory> statusList) {
        this.statusList = statusList;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
