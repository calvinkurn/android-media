package com.tokopedia.inbox.attachinvoice.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.attachinvoice.view.adapter.AttachInvoiceListAdapterTypeFactory;
import com.tokopedia.inbox.attachinvoice.view.viewholder.InvoiceViewHolder;

/**
 * Created by Hendri on 22/03/18.
 */

public class InvoiceViewModel implements Visitable<AttachInvoiceListAdapterTypeFactory> {

    int invoiceId;
    int invoiceType;
    int statusId;
    String invoiceNumber;
    String productTopName;
    String productTopImage;
    String status;
    String date;
    String total;
    String invoiceTypeStr;
    String description;
    String invoiceUrl;

    public InvoiceViewModel(int invoiceId, int invoiceType, int statusId, String invoiceNumber, String productTopName, String productTopImage, String status, String date, String total, String invoiceTypeStr, String description, String invoiceUrl) {
        this.invoiceId = invoiceId;
        this.invoiceType = invoiceType;
        this.statusId = statusId;
        this.invoiceNumber = invoiceNumber;
        this.productTopName = productTopName;
        this.productTopImage = productTopImage;
        this.status = status;
        this.date = date;
        this.total = total;
        this.invoiceTypeStr = invoiceTypeStr;
        this.description = description;
        this.invoiceUrl = invoiceUrl;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getProductTopName() {
        return productTopName;
    }

    public void setProductTopName(String productTopName) {
        this.productTopName = productTopName;
    }

    public String getProductTopImage() {
        return productTopImage;
    }

    public void setProductTopImage(String productTopImage) {
        this.productTopImage = productTopImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getInvoiceTypeStr() {
        return invoiceTypeStr;
    }

    public void setInvoiceTypeStr(String invoiceTypeStr) {
        this.invoiceTypeStr = invoiceTypeStr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    @Override
    public int type(AttachInvoiceListAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
