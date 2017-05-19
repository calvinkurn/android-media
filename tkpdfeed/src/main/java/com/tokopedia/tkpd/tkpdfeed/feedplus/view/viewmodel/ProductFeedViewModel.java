package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 5/16/17.
 */

public class ProductFeedViewModel implements Parcelable{

    private String name;
    private String price;
    private String imageSource;
    private String url;

    private String shopName;
    private String shopAva;
    private boolean isFavorited;

    public ProductFeedViewModel(String produk1, String s, String s1) {
        this.name = produk1;
        this.price = s;
        this.imageSource = s1;
        this.url = "https://www.tokopedia.com/casemacbookjak/case-macbook-air-11-electric-blue-matte";
    }

    public ProductFeedViewModel(String name, String price, String imageSource, String shopAva, String shopName, boolean isFavorited) {
        this(name, price, imageSource);
        this.shopAva = shopAva;
        this.shopName = shopName;
        this.isFavorited = isFavorited;
    }

    public ProductFeedViewModel(ProductFeedViewModel model, String shopAva, String shopName, boolean isFavorited){
        this(model.getName(), model.getPrice(), model.getImageSource());
        this.shopAva = shopAva;
        this.shopName = shopName;
        this.isFavorited = isFavorited;
    }

    protected ProductFeedViewModel(Parcel in) {
        name = in.readString();
        price = in.readString();
        imageSource = in.readString();
        url = in.readString();
    }

    public static final Creator<ProductFeedViewModel> CREATOR = new Creator<ProductFeedViewModel>() {
        @Override
        public ProductFeedViewModel createFromParcel(Parcel in) {
            return new ProductFeedViewModel(in);
        }

        @Override
        public ProductFeedViewModel[] newArray(int size) {
            return new ProductFeedViewModel[size];
        }
    };

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

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAva() {
        return shopAva;
    }

    public void setShopAva(String shopAva) {
        this.shopAva = shopAva;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(imageSource);
        dest.writeString(url);
    }
}
