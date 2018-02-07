package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

import android.support.annotation.NonNull;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class ProductFeedDomain {


    @Nullable
    private int id;


    @Nullable
    private String name;


    @Nullable
    private String price;


    @Nullable
    private String image;


    @Nullable
    private String imageSingle;


    @Nullable
    private List<WholesaleDomain> wholesale;


    @Nullable
    private boolean freeReturns;


    @Nullable
    private boolean preorder;


    @Nullable
    private String cashback;


    @Nullable
    private String url;


    @Nullable
    private String productLink;


    @Nullable
    private boolean wishlist;


    @Nullable
    private double rating;

    @NonNull
    private String cursor;

    @Nullable
    private String priceInt;

    public ProductFeedDomain(@Nullable int id,
                             @Nullable String name,
                             @Nullable String price,
                             @Nullable String image,
                             @Nullable String image_single,
                             @Nullable List<WholesaleDomain> wholesale,
                             @Nullable boolean freereturns,
                             @Nullable boolean preorder,
                             @Nullable String cashback,
                             @Nullable String url,
                             @Nullable String productLink,
                             @Nullable boolean wishlist,
                             @Nullable double rating,
                             @Nullable String priceInt,
                             @NonNull String cursor) {
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
        this.priceInt = priceInt;
    }

    @Nullable
    public int getId() {
        return id;
    }

    public void setId(@Nullable int id) {
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
    public boolean getFreeReturns() {
        return freeReturns;
    }

    public void setFreeReturns(@Nullable boolean freeReturns) {
        this.freeReturns = freeReturns;
    }

    @Nullable
    public boolean getPreorder() {
        return preorder;
    }

    public void setPreorder(@Nullable boolean preorder) {
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
    public String getUrl() {
        return url;
    }

    public void setUrl(@Nullable String url) {
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
    public boolean getWishlist() {
        return wishlist;
    }

    public void setWishlist(@Nullable boolean wishlist) {
        this.wishlist = wishlist;
    }

    @Nullable
    public double getRating() {
        return rating;
    }

    public void setRating(@Nullable double rating) {
        this.rating = rating;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    @Nullable
    public String getPriceInt() {
        return priceInt;
    }

    public void setPriceInt(@Nullable String priceInt) {
        this.priceInt = priceInt;
    }
}
