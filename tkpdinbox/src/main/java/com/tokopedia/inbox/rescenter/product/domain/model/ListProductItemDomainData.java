package com.tokopedia.inbox.rescenter.product.domain.model;

/**
 * Created by hangnadi on 3/27/17.
 */

public class ListProductItemDomainData {

    private String resCenterProductID;
    private String productImageUrl;
    private String productName;

    public String getResCenterProductID() {
        return resCenterProductID;
    }

    public void setResCenterProductID(String resCenterProductID) {
        this.resCenterProductID = resCenterProductID;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
