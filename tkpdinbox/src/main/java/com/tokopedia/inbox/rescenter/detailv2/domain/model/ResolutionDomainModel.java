package com.tokopedia.inbox.rescenter.detailv2.domain.model;

/**
 * Created by hangnadi on 3/17/17.
 */

public class ResolutionDomainModel {
    private String orderID;
    private String orderAwbNumber;
    private String buyerID;
    private String buyerName;
    private String complaintDate;
    private String invoice;
    private String invoiceUrl;
    private String shopID;
    private String shopName;
    private int receivedFlag;
    private String status;
    private String responseDeadline;
    private boolean deadlineVisibility;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderAwbNumber() {
        return orderAwbNumber;
    }

    public void setOrderAwbNumber(String orderAwbNumber) {
        this.orderAwbNumber = orderAwbNumber;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getReceivedFlag() {
        return receivedFlag;
    }

    public void setReceivedFlag(int receivedFlag) {
        this.receivedFlag = receivedFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResponseDeadline(String responseDeadline) {
        this.responseDeadline = responseDeadline;
    }

    public String getResponseDeadline() {
        return responseDeadline;
    }

    public void setDeadlineVisibility(boolean deadlineVisibility) {
        this.deadlineVisibility = deadlineVisibility;
    }

    public boolean isDeadlineVisibility() {
        return deadlineVisibility;
    }
}
