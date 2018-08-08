package com.tokopedia.seller.product.edit.domain.model;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class AddProductValidationDomainModel {
    private String postKey;
    private String productDesc;
    private String productDest;
    private String productEtalase;
    private Integer productId;
    private String productName;
    private String productUrl;

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public void setProductDest(String productDest) {
        this.productDest = productDest;
    }

    public void setproductEtalase(String productEtalase) {
        this.productEtalase = productEtalase;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getProductDest() {
        return productDest;
    }

    public String getProductEtalase() {
        return productEtalase;
    }

    public void setProductEtalase(String productEtalase) {
        this.productEtalase = productEtalase;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductUrl() {
        return productUrl;
    }
}
