package com.tokopedia.inbox.attachinvoice.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.attachinvoice.view.adapter.AttachInvoiceListAdapterTypeFactory;
import com.tokopedia.inbox.attachinvoice.view.viewholder.InvoiceViewHolder;

/**
 * Created by Hendri on 22/03/18.
 */

public class InvoiceViewModel implements Visitable<AttachInvoiceListAdapterTypeFactory> {
    String invoiceNumber;
    String productTopName;
    String productTopImage;
    String status;
    String date;
    String total;
    String productCountDisplay;
    String invoiceType;
    String description;

    public InvoiceViewModel(String invoiceNumber, String productTopName, String productTopImage, String status, String date, String total, String productCountDisplay, String invoiceType, String description) {
        this.invoiceNumber = invoiceNumber;
        this.productTopName = productTopName;
        this.productTopImage = productTopImage;
        this.status = status;
        this.date = date;
        this.total = total;
        this.productCountDisplay = productCountDisplay;
        this.invoiceType = invoiceType;
        this.description = description;
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

    public String getProductCountDisplay() {
        return productCountDisplay;
    }

    public void setProductCountDisplay(String productCountDisplay) {
        this.productCountDisplay = productCountDisplay;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int type(AttachInvoiceListAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
