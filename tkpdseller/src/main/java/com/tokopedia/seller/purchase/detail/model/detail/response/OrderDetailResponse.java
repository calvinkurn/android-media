
package com.tokopedia.seller.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailResponse {

    @SerializedName("order_id")
    @Expose
    private int orderId;
    @SerializedName("order_status")
    @Expose
    private int orderStatus;
    @SerializedName("reso_id")
    @Expose
    private int resoId;
    @SerializedName("invoice")
    @Expose
    private String invoice;
    @SerializedName("invoice_url")
    @Expose
    private String invoiceUrl;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("detail")
    @Expose
    private Detail detail;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    @SerializedName("summary")
    @Expose
    private Summary summary;
    @SerializedName("buttons")
    @Expose
    private Buttons buttons;
    @SerializedName("is_free_shipping")
    @Expose
    private Boolean isFreeShipping;
    @SerializedName("origin_info")
    @Expose
    private OriginInfo originInfo;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getResoId() {
        return resoId;
    }

    public void setResoId(int resoId) {
        this.resoId = resoId;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public Buttons getButtons() {
        return buttons;
    }

    public void setButtons(Buttons buttons) {
        this.buttons = buttons;
    }

    public Boolean getFreeShipping() { return isFreeShipping; }

    public void setFreeShipping(Boolean freeShipping) { isFreeShipping = freeShipping; }

    public OriginInfo getOriginInfo() { return originInfo; }

    public void setOriginInfo(OriginInfo originInfo) { this.originInfo = originInfo; }
}
