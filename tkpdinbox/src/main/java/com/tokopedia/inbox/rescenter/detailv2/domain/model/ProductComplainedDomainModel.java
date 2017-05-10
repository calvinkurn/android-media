package com.tokopedia.inbox.rescenter.detailv2.domain.model;

/**
 * Created by hangnadi on 3/17/17.
 */

public class ProductComplainedDomainModel {
    private String productName;
    private String productImageUrl;
    private String productID;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
