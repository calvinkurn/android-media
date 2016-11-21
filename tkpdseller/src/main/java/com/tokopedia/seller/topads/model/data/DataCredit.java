package com.tokopedia.seller.topads.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class DataCredit {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_bonus")
    @Expose
    private String productBonus;
    @SerializedName("product_url")
    @Expose
    private String productUrl;

    /**
     *
     * @return
     * The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     *
     * @param productId
     * The product_id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     *
     * @return
     * The productType
     */
    public String getProductType() {
        return productType;
    }

    /**
     *
     * @param productType
     * The product_type
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     *
     * @return
     * The productPrice
     */
    public String getProductPrice() {
        return productPrice;
    }

    /**
     *
     * @param productPrice
     * The product_price
     */
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    /**
     *
     * @return
     * The productBonus
     */
    public String getProductBonus() {
        return productBonus;
    }

    /**
     *
     * @param productBonus
     * The product_bonus
     */
    public void setProductBonus(String productBonus) {
        this.productBonus = productBonus;
    }

    /**
     *
     * @return
     * The productUrl
     */
    public String getProductUrl() {
        return productUrl;
    }

    /**
     *
     * @param productUrl
     * The product_url
     */
    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }
}
