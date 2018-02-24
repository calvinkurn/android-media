
package com.tokopedia.gm.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GMFeaturedProduct {

    @SerializedName("product_id")
    @Expose
    private String productId;
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
    private GMFeaturedCashBackDetail cashbackDetail;
    @SerializedName("labels")
    @Expose
    private List<GMFeaturedLabel> labels = null;
    @SerializedName("is_rated")
    @Expose
    private boolean isRated;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("total_review")
    @Expose
    private String totalReview;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
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

    public GMFeaturedCashBackDetail getCashbackDetail() {
        return cashbackDetail;
    }

    public void setCashbackDetail(GMFeaturedCashBackDetail cashbackDetail) {
        this.cashbackDetail = cashbackDetail;
    }

    public List<GMFeaturedLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<GMFeaturedLabel> labels) {
        this.labels = labels;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(String totalReview) {
        this.totalReview = totalReview;
    }
}
