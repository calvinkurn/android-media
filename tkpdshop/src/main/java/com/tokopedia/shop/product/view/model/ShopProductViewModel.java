package com.tokopedia.shop.product.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.product.view.adapter.ShopProductTypeFactory;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductViewModel implements Visitable<ShopProductTypeFactory> {

    private String id;
    private String name;
    private String price;
    private String imageUrl;
    private long totalReview;
    private double rating;
    private double cashback;
    private boolean wholesale;
    private boolean po;
    private boolean freeReturn;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(long totalReview) {
        this.totalReview = totalReview;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getCashback() {
        return cashback;
    }

    public void setCashback(double cashback) {
        this.cashback = cashback;
    }

    public boolean isWholesale() {
        return wholesale;
    }

    public void setWholesale(boolean wholesale) {
        this.wholesale = wholesale;
    }

    public boolean isPo() {
        return po;
    }

    public void setPo(boolean po) {
        this.po = po;
    }

    public boolean isFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        this.freeReturn = freeReturn;
    }

    @Override
    public int type(ShopProductTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
