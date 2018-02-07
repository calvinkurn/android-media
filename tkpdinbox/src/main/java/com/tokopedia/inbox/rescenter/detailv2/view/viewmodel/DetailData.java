package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/10/17.
 */

public class DetailData implements Parcelable {
    private String awbNumber;
    private String complaintDate;
    private String complaintDateTimestamp;
    private String invoice;
    private String shopID;
    private String shopName;
    private String responseDeadline;
    private boolean deadlineVisibility;
    private String buyerID;
    private String buyerName;
    private String invoiceUrl;
    private String orderID;
    private boolean received;
    private boolean cancel;
    private boolean finish;
    private int resolutionStatus;
    private boolean canAskHelp;

    public DetailData() {
    }

    public boolean isCanAskHelp() {
        return canAskHelp;
    }

    public void setCanAskHelp(boolean canAskHelp) {
        this.canAskHelp = canAskHelp;
    }

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

    public String getComplaintDateTimestamp() {
        return complaintDateTimestamp;
    }

    public void setComplaintDateTimestamp(String complaintDateTimestamp) {
        this.complaintDateTimestamp = complaintDateTimestamp;
    }

    public boolean isDeadlineVisibility() {
        return deadlineVisibility;
    }

    public void setDeadlineVisibility(boolean deadlineVisibility) {
        this.deadlineVisibility = deadlineVisibility;
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

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public int getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(int resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.awbNumber);
        dest.writeString(this.complaintDate);
        dest.writeString(this.complaintDateTimestamp);
        dest.writeString(this.invoice);
        dest.writeString(this.shopID);
        dest.writeString(this.shopName);
        dest.writeString(this.responseDeadline);
        dest.writeByte(this.deadlineVisibility ? (byte) 1 : (byte) 0);
        dest.writeString(this.buyerID);
        dest.writeString(this.buyerName);
        dest.writeString(this.invoiceUrl);
        dest.writeString(this.orderID);
        dest.writeByte(this.received ? (byte) 1 : (byte) 0);
        dest.writeByte(this.cancel ? (byte) 1 : (byte) 0);
        dest.writeByte(this.finish ? (byte) 1 : (byte) 0);
        dest.writeInt(this.resolutionStatus);
        dest.writeByte(this.canAskHelp ? (byte) 1 : (byte) 0);
    }

    protected DetailData(Parcel in) {
        this.awbNumber = in.readString();
        this.complaintDate = in.readString();
        this.complaintDateTimestamp = in.readString();
        this.invoice = in.readString();
        this.shopID = in.readString();
        this.shopName = in.readString();
        this.responseDeadline = in.readString();
        this.deadlineVisibility = in.readByte() != 0;
        this.buyerID = in.readString();
        this.buyerName = in.readString();
        this.invoiceUrl = in.readString();
        this.orderID = in.readString();
        this.received = in.readByte() != 0;
        this.cancel = in.readByte() != 0;
        this.finish = in.readByte() != 0;
        this.resolutionStatus = in.readInt();
        this.canAskHelp = in.readByte() != 0;
    }

    public static final Creator<DetailData> CREATOR = new Creator<DetailData>() {
        @Override
        public DetailData createFromParcel(Parcel source) {
            return new DetailData(source);
        }

        @Override
        public DetailData[] newArray(int size) {
            return new DetailData[size];
        }
    };
}
