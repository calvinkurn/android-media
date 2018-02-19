
package com.tokopedia.gm.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GMFeaturedProduct {

    @SerializedName("product_id")
    @Expose
    private long productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("image_uri")
    @Expose
    private String imageUri;
    @SerializedName("preorder")
    @Expose
    private boolean preorder;
    @SerializedName("returnable")
    @Expose
    private boolean returnable;
    @SerializedName("wholesale")
    @Expose
    private boolean wholesale;
    @SerializedName("cashback")
    @Expose
    private boolean cashback;
    @SerializedName("cashback_detail")
    @Expose
    private GMCashBackDetail cashbackDetail;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isPreorder() {
        return preorder;
    }

    public void setPreorder(boolean preorder) {
        this.preorder = preorder;
    }

    public boolean isReturnable() {
        return returnable;
    }

    public void setReturnable(boolean returnable) {
        this.returnable = returnable;
    }

    public boolean isWholesale() {
        return wholesale;
    }

    public void setWholesale(boolean wholesale) {
        this.wholesale = wholesale;
    }

    public boolean isCashback() {
        return cashback;
    }

    public void setCashback(boolean cashback) {
        this.cashback = cashback;
    }

    public GMCashBackDetail getCashbackDetail() {
        return cashbackDetail;
    }

    public void setCashbackDetail(GMCashBackDetail cashbackDetail) {
        this.cashbackDetail = cashbackDetail;
    }

}
