package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 3/27/17.
 * Copied to feed by milhamj 1/18/17.
 */

public class Product {
    private String id;
    private String name;
    private ProductImage image;
    private String uri;
    private String relativeUri;
    private String priceFormat;
    private String countTalkFormat;
    private String countReviewFormat;
    private Category category;
    private boolean productPreorder;
    private boolean productWholesale;
    private String freeReturn;
    private boolean productCashback;
    private String productCashbackRate;
    private int productRating;
    private List<WholesalePrice> wholesalePrice = new ArrayList<>();
    private List<Label> labels = new ArrayList<>();

    public Product(String id, String name, ProductImage image, String uri, String relativeUri,
                   String priceFormat, String countTalkFormat, String countReviewFormat,
                   Category category, boolean productPreorder, boolean productWholesale,
                   String freeReturn, boolean productCashback, String productCashbackRate,
                   int productRating, List<WholesalePrice> wholesalePrice, List<Label> labels) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.uri = uri;
        this.relativeUri = relativeUri;
        this.priceFormat = priceFormat;
        this.countTalkFormat = countTalkFormat;
        this.countReviewFormat = countReviewFormat;
        this.category = category;
        this.productPreorder = productPreorder;
        this.productWholesale = productWholesale;
        this.freeReturn = freeReturn;
        this.productCashback = productCashback;
        this.productCashbackRate = productCashbackRate;
        this.productRating = productRating;
        this.wholesalePrice = wholesalePrice;
        this.labels = labels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductImage getImage() {
        return image;
    }

    public void setImage(ProductImage image) {
        this.image = image;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRelativeUri() {
        return relativeUri;
    }

    public void setRelativeUri(String relativeUri) {
        this.relativeUri = relativeUri;
    }

    public String getPriceFormat() {
        return priceFormat;
    }

    public void setPriceFormat(String priceFormat) {
        this.priceFormat = priceFormat;
    }

    public String getCountTalkFormat() {
        return countTalkFormat;
    }

    public void setCountTalkFormat(String countTalkFormat) {
        this.countTalkFormat = countTalkFormat;
    }

    public String getCountReviewFormat() {
        return countReviewFormat;
    }

    public void setCountReviewFormat(String countReviewFormat) {
        this.countReviewFormat = countReviewFormat;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(boolean productPreorder) {
        this.productPreorder = productPreorder;
    }

    public boolean isProductWholesale() {
        return productWholesale;
    }

    public void setProductWholesale(boolean productWholesale) {
        this.productWholesale = productWholesale;
    }

    public String getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(String freeReturn) {
        this.freeReturn = freeReturn;
    }

    public boolean isProductCashback() {
        return productCashback;
    }

    public void setProductCashback(boolean productCashback) {
        this.productCashback = productCashback;
    }

    public String getProductCashbackRate() {
        return productCashbackRate;
    }

    public void setProductCashbackRate(String productCashbackRate) {
        this.productCashbackRate = productCashbackRate;
    }

    public List<WholesalePrice> getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(List<WholesalePrice> wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public int getProductRating() {
        return productRating;
    }

    public void setProductRating(int productRating) {
        this.productRating = productRating;
    }
}
