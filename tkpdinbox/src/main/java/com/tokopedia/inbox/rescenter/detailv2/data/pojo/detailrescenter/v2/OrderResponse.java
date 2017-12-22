package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 07/11/17.
 */
public class OrderResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("invoice")
    private InvoiceResponse invoice;
    @SerializedName("openAmount")
    private int openAmount;
    @SerializedName("shippingPrice")
    private int shippingPrice;
    @SerializedName("awb")
    private String awb;
    @SerializedName("pdf")
    private String pdf;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InvoiceResponse getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceResponse invoice) {
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

}
