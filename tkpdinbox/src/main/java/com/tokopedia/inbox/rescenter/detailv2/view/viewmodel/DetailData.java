package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

/**
 * Created by hangnadi on 3/10/17.
 */

public class DetailData {
    private String awbNumber;
    private String complaintDate;
    private String invoice;
    private String shopName;
    private String responseDeadline;
    private boolean buyerDeadlineVisibility;
    private boolean sellerDeadlineVisibility;

    public String getAwbNumber() {
        return awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getResponseDeadline() {
        return responseDeadline;
    }

    public void setResponseDeadline(String responseDeadline) {
        this.responseDeadline = responseDeadline;
    }

    public boolean isBuyerDeadlineVisibility() {
        return buyerDeadlineVisibility;
    }

    public void setBuyerDeadlineVisibility(boolean buyerDeadlineVisibility) {
        this.buyerDeadlineVisibility = buyerDeadlineVisibility;
    }

    public boolean isSellerDeadlineVisibility() {
        return sellerDeadlineVisibility;
    }

    public void setSellerDeadlineVisibility(boolean sellerDeadlineVisibility) {
        this.sellerDeadlineVisibility = sellerDeadlineVisibility;
    }
}
