package com.tokopedia.inbox.attachinvoice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 28/03/18.
 */

public class InvoiceAttributesDataModel {
    @SerializedName("id")
    @Expose
    int invoiceId;
    @SerializedName("code")
    @Expose
    String invoiceNo;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("desc")
    @Expose
    String description;
    @SerializedName("create_time")
    @Expose
    String invoiceDate;
    @SerializedName("image_url")
    @Expose
    String imageUrl;
    @SerializedName("status_id")
    @Expose
    int statusId;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("total_amount")
    @Expose
    String amount;

    @SerializedName("url")
    @Expose
    String url;

    public InvoiceAttributesDataModel(int invoiceId, String invoiceNo, String title, String description, String invoiceDate, String imageUrl, int statusId, String status, String amount, String url) {
        this.invoiceId = invoiceId;
        this.invoiceNo = invoiceNo;
        this.title = title;
        this.description = description;
        this.invoiceDate = invoiceDate;
        this.imageUrl = imageUrl;
        this.statusId = statusId;
        this.status = status;
        this.amount = amount;
        this.url = url;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
