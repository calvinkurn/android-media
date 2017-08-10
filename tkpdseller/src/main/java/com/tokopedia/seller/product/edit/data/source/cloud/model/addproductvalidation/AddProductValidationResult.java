
package com.tokopedia.seller.product.edit.data.source.cloud.model.addproductvalidation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProductValidationResult {

    @SerializedName("post_key")
    @Expose
    private String postKey;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_desc")
    @Expose
    private String productDesc;
    @SerializedName("product_etalase")
    @Expose
    private String productEtalase;
    @SerializedName("product_dest")
    @Expose
    private String productDest;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_url")
    @Expose
    private String productUrl;

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductEtalase() {
        return productEtalase;
    }

    public void setProductEtalase(String productEtalase) {
        this.productEtalase = productEtalase;
    }

    public String getProductDest() {
        return productDest;
    }

    public void setProductDest(String productDest) {
        this.productDest = productDest;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

}
