package com.tokopedia.seller.transaction.neworder.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zulfikarrahman on 7/17/17.
 */

public class DataOrderDetailView implements Parcelable {
    private int paymentProcessDayLeft;
    private int orderId;
    private String detailOrderDate;
    private String customerName;

    public int getPaymentProcessDayLeft() {
        return paymentProcessDayLeft;
    }

    public void setPaymentProcessDayLeft(int paymentProcessDayLeft) {
        this.paymentProcessDayLeft = paymentProcessDayLeft;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDetailOrderDate() {
        return detailOrderDate;
    }

    public void setDetailOrderDate(String detailOrderDate) {
        this.detailOrderDate = detailOrderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.paymentProcessDayLeft);
        dest.writeInt(this.orderId);
        dest.writeString(this.detailOrderDate);
        dest.writeString(this.customerName);
    }

    public DataOrderDetailView() {
    }

    protected DataOrderDetailView(Parcel in) {
        this.paymentProcessDayLeft = in.readInt();
        this.orderId = in.readInt();
        this.detailOrderDate = in.readString();
        this.customerName = in.readString();
    }

    public static final Parcelable.Creator<DataOrderDetailView> CREATOR = new Parcelable.Creator<DataOrderDetailView>() {
        @Override
        public DataOrderDetailView createFromParcel(Parcel source) {
            return new DataOrderDetailView(source);
        }

        @Override
        public DataOrderDetailView[] newArray(int size) {
            return new DataOrderDetailView[size];
        }
    };
}
