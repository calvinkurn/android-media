
package com.tokopedia.transaction.purchase.detail.model.detail.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("order_status")
    @Expose
    private Integer orderStatus;
    @SerializedName("reso_id")
    @Expose
    private Integer resoId;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getResoId() {
        return resoId;
    }

    public void setResoId(Integer resoId) {
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

}
