
package com.tokopedia.inbox.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolutionOrder implements Parcelable {

    @SerializedName("order_pdf_url")
    @Expose
    private String orderPdfUrl;
    @SerializedName("order_shipping_price_idr")
    @Expose
    private String orderShippingPriceIdr;
    @SerializedName("order_open_amount_idr")
    @Expose
    private String orderOpenAmountIdr;
    @SerializedName("order_shipping_price")
    @Expose
    private String orderShippingPrice;
    @SerializedName("order_open_amount")
    @Expose
    private String orderOpenAmount;
    @SerializedName("order_invoice_ref_num")
    @Expose
    private String orderInvoiceRefNum;
    @SerializedName("order_free_return")
    @Expose
    private Integer orderFreeReturn;

    /**
     * 
     * @return
     *     The orderPdfUrl
     */
    public String getOrderPdfUrl() {
        return orderPdfUrl;
    }

    /**
     * 
     * @param orderPdfUrl
     *     The order_pdf_url
     */
    public void setOrderPdfUrl(String orderPdfUrl) {
        this.orderPdfUrl = orderPdfUrl;
    }

    /**
     * 
     * @return
     *     The orderShippingPriceIdr
     */
    public String getOrderShippingPriceIdr() {
        return orderShippingPriceIdr;
    }

    /**
     * 
     * @param orderShippingPriceIdr
     *     The order_shipping_price_idr
     */
    public void setOrderShippingPriceIdr(String orderShippingPriceIdr) {
        this.orderShippingPriceIdr = orderShippingPriceIdr;
    }

    /**
     * 
     * @return
     *     The orderOpenAmountIdr
     */
    public String getOrderOpenAmountIdr() {
        return orderOpenAmountIdr;
    }

    /**
     * 
     * @param orderOpenAmountIdr
     *     The order_open_amount_idr
     */
    public void setOrderOpenAmountIdr(String orderOpenAmountIdr) {
        this.orderOpenAmountIdr = orderOpenAmountIdr;
    }

    /**
     * 
     * @return
     *     The orderShippingPrice
     */
    public String getOrderShippingPrice() {
        return orderShippingPrice;
    }

    /**
     * 
     * @param orderShippingPrice
     *     The order_shipping_price
     */
    public void setOrderShippingPrice(String orderShippingPrice) {
        this.orderShippingPrice = orderShippingPrice;
    }

    /**
     * 
     * @return
     *     The orderOpenAmount
     */
    public String getOrderOpenAmount() {
        return orderOpenAmount;
    }

    /**
     * 
     * @param orderOpenAmount
     *     The order_open_amount
     */
    public void setOrderOpenAmount(String orderOpenAmount) {
        this.orderOpenAmount = orderOpenAmount;
    }

    /**
     * 
     * @return
     *     The orderInvoiceRefNum
     */
    public String getOrderInvoiceRefNum() {
        return orderInvoiceRefNum;
    }

    /**
     * 
     * @param orderInvoiceRefNum
     *     The order_invoice_ref_num
     */
    public void setOrderInvoiceRefNum(String orderInvoiceRefNum) {
        this.orderInvoiceRefNum = orderInvoiceRefNum;
    }

    public Integer getOrderFreeReturn() {
        return orderFreeReturn;
    }

    public void setOrderFreeReturn(Integer orderFreeReturn) {
        this.orderFreeReturn = orderFreeReturn;
    }

    public ResolutionOrder() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderPdfUrl);
        dest.writeString(this.orderShippingPriceIdr);
        dest.writeString(this.orderOpenAmountIdr);
        dest.writeString(this.orderShippingPrice);
        dest.writeString(this.orderOpenAmount);
        dest.writeString(this.orderInvoiceRefNum);
        dest.writeValue(this.orderFreeReturn);
    }

    protected ResolutionOrder(Parcel in) {
        this.orderPdfUrl = in.readString();
        this.orderShippingPriceIdr = in.readString();
        this.orderOpenAmountIdr = in.readString();
        this.orderShippingPrice = in.readString();
        this.orderOpenAmount = in.readString();
        this.orderInvoiceRefNum = in.readString();
        this.orderFreeReturn = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<ResolutionOrder> CREATOR = new Creator<ResolutionOrder>() {
        @Override
        public ResolutionOrder createFromParcel(Parcel source) {
            return new ResolutionOrder(source);
        }

        @Override
        public ResolutionOrder[] newArray(int size) {
            return new ResolutionOrder[size];
        }
    };
}
