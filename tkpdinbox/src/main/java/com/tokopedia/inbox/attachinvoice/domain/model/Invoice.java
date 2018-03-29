package com.tokopedia.inbox.attachinvoice.domain.model;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by Hendri on 21/03/18.
 */

public class Invoice {
    int statusInt;
    String number;
    String type;
    String url;
    String title;
    String desc;
    String date;
    String status;
    String total;
    String imageUrl;
    int invoiceTypeInt;
    int invoiceId;
    public Invoice(int statusInt, String number, String type, String url, String title, String desc, String date, String status, String total, String imageUrl, int invoiceTypeInt, int invoiceId) {
        this.statusInt = statusInt;
        this.number = number;
        this.type = type;
        this.url = url;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.status = status;
        this.total = total;
        this.imageUrl = imageUrl;
        this.invoiceTypeInt = invoiceTypeInt;
        this.invoiceId = invoiceId;
    }

    public int getStatusInt() {
        return statusInt;
    }

    public void setStatusInt(int statusInt) {
        this.statusInt = statusInt;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getInvoiceTypeInt() {
        return invoiceTypeInt;
    }

    public void setInvoiceTypeInt(int invoiceTypeInt) {
        this.invoiceTypeInt = invoiceTypeInt;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }
}
