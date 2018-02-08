package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class OrderData implements Parcelable {

    public static final Parcelable.Creator<OrderData> CREATOR = new Parcelable.Creator<OrderData>() {
        @Override
        public OrderData createFromParcel(Parcel source) {
            return new OrderData(source);
        }

        @Override
        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };
    private int id;
    private InvoiceData invoice;
    private int openAmount;
    private int shippingPrice;
    private String awb;
    private String pdf;

    public OrderData(int id, InvoiceData invoice, int openAmount, int shippingPrice, String awb, String pdf) {
        this.id = id;
        this.invoice = invoice;
        this.openAmount = openAmount;
        this.shippingPrice = shippingPrice;
        this.awb = awb;
        this.pdf = pdf;
    }

    protected OrderData(Parcel in) {
        this.id = in.readInt();
        this.invoice = in.readParcelable(InvoiceData.class.getClassLoader());
        this.openAmount = in.readInt();
        this.shippingPrice = in.readInt();
        this.awb = in.readString();
        this.pdf = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InvoiceData getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceData invoice) {
        this.invoice = invoice;
    }

    public int getOpenAmount() {
        return openAmount;
    }

    public void setOpenAmount(int openAmount) {
        this.openAmount = openAmount;
    }

    public int getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(int shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.invoice, flags);
        dest.writeInt(this.openAmount);
        dest.writeInt(this.shippingPrice);
        dest.writeString(this.awb);
        dest.writeString(this.pdf);
    }
}
