package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

import android.support.annotation.NonNull;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class ProductFeedDomain {

    private
    @Nullable
    Integer id;

    private
    @Nullable
    String name;

    private
    @Nullable
    String price;

    private
    @Nullable
    String image;

    private
    @Nullable
    String imageSingle;

    private
    @Nullable
    List<WholesaleDomain> wholesale;

    private
    @Nullable
    Boolean freeReturns;

    private
    @Nullable
    Boolean preorder;

    private
    @Nullable
    String cashback;

    private
    @Nullable
    Object url;

    private
    @Nullable
    String productLink;

    private
    @Nullable
    Boolean wishlist;

    private
    @Nullable
    Integer rating;
    private
    @NonNull
    String cursor;

    public ProductFeedDomain(@Nullable Integer id, @Nullable String name, @Nullable String price,
                             @Nullable String image, @Nullable String image_single,
                             @Nullable List<WholesaleDomain> wholesale, @Nullable Boolean freereturns,
                             @Nullable Boolean preorder, @Nullable String cashback, @Nullable Object url,
                             @Nullable String productLink, @Nullable Boolean wishlist, @Nullable Integer
                                     rating, @NonNull String cursor) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.imageSingle = image_single;
        this.wholesale = wholesale;
        this.freeReturns = freereturns;
        this.preorder = preorder;
        this.cashback = cashback;
        this.url = url;
        this.productLink = productLink;
        this.wishlist = wishlist;
        this.rating = rating;
        this.cursor = cursor;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getPrice() {
        return price;
    }

    public void setPrice(@Nullable String price) {
        this.price = price;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    public void setImage(@Nullable String image) {
        this.image = image;
    }

    @Nullable
    public String getImageSingle() {
        return imageSingle;
    }

    public void setImageSingle(@Nullable String imageSingle) {
        this.imageSingle = imageSingle;
    }

    @Nullable
    public Boolean getFreeReturns() {
        return freeReturns;
    }

    public void setFreeReturns(@Nullable Boolean freeReturns) {
        this.freeReturns = freeReturns;
    }

    @Nullable
    public Boolean getPreorder() {
        return preorder;
    }

    public void setPreorder(@Nullable Boolean preorder) {
        this.preorder = preorder;
    }

    @Nullable
    public String getCashback() {
        return cashback;
    }

    public void setCashback(@Nullable String cashback) {
        this.cashback = cashback;
    }

    @Nullable
    public Object getUrl() {
        return url;
    }

    public void setUrl(@Nullable Object url) {
        this.url = url;
    }

    @Nullable
    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(@Nullable String productLink) {
        this.productLink = productLink;
    }

    @Nullable
    public Boolean getWishlist() {
        return wishlist;
    }

    public void setWishlist(@Nullable Boolean wishlist) {
        this.wishlist = wishlist;
    }

    @Nullable
    public Integer getRating() {
        return rating;
    }

    public void setRating(@Nullable Integer rating) {
        this.rating = rating;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
