package com.tokopedia.seller.gmstat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPopularProduct {

    @SerializedName("ProductId")
    @Expose
    private int productId;
    @SerializedName("Sold")
    @Expose
    private long sold;
    @SerializedName("View")
    @Expose
    private int view;
    @SerializedName("SuccessTrans")
    @Expose
    private int successTrans;
    @SerializedName("WishlistCount")
    @Expose
    private int wishlistCount;
    @SerializedName("ConversionRate")
    @Expose
    private int conversionRate;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductLink")
    @Expose
    private String productLink;
    @SerializedName("ImageLink")
    @Expose
    private String imageLink;

    /**
     *
     * @return
     * The productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     *
     * @param productId
     * The ProductId
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     *
     * @return
     * The sold
     */
    public long getSold() {
        return sold;
    }

    /**
     *
     * @param sold
     * The Sold
     */
    public void setSold(long sold) {
        this.sold = sold;
    }

    /**
     *
     * @return
     * The view
     */
    public int getView() {
        return view;
    }

    /**
     *
     * @param view
     * The View
     */
    public void setView(int view) {
        this.view = view;
    }

    /**
     *
     * @return
     * The successTrans
     */
    public int getSuccessTrans() {
        return successTrans;
    }

    /**
     *
     * @param successTrans
     * The SuccessTrans
     */
    public void setSuccessTrans(int successTrans) {
        this.successTrans = successTrans;
    }

    /**
     *
     * @return
     * The wishlistCount
     */
    public int getWishlistCount() {
        return wishlistCount;
    }

    /**
     *
     * @param wishlistCount
     * The WishlistCount
     */
    public void setWishlistCount(int wishlistCount) {
        this.wishlistCount = wishlistCount;
    }

    /**
     *
     * @return
     * The conversionRate
     */
    public int getConversionRate() {
        return conversionRate;
    }

    /**
     *
     * @param conversionRate
     * The ConversionRate
     */
    public void setConversionRate(int conversionRate) {
        this.conversionRate = conversionRate;
    }

    /**
     *
     * @return
     * The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     *
     * @param productName
     * The ProductName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     *
     * @return
     * The productLink
     */
    public String getProductLink() {
        return productLink;
    }

    /**
     *
     * @param productLink
     * The ProductLink
     */
    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    /**
     *
     * @return
     * The imageLink
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     *
     * @param imageLink
     * The ImageLink
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

}