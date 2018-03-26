package com.tokopedia.inbox.attachinvoice.domain.model;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by Hendri on 21/03/18.
 */

public class Invoice {
    String number;
    String type;
    String url;
    List<Product> products;
    String date;
    String status;
    String total;

    public Invoice(String number, String type, String url, @Nullable List<Product> products, String date, String status, String total) {
        this.number = number;
        this.type = type;
        this.url = url;
        this.products = products;
        this.date = date;
        this.status = status;
        this.total = total;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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
}
