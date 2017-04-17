package com.tokopedia.seller.product.domain.model;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class AddProductDomainModel {
    private int productId;
    private String productDesc;
    private String productEtalase;
    private String productDest;
    private String productName;
    private String productUrl;

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public void setProductEtalase(String productEtalase) {
        this.productEtalase = productEtalase;
    }

    public void setProductDest(String productDest) {
        this.productDest = productDest;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }
}
