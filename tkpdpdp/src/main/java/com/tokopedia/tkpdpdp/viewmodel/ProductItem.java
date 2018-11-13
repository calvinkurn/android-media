package com.tokopedia.tkpdpdp.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductItem {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("shop")
    @Expose
    private Shop shop;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getRating() {
        return rating;
    }

    public Shop getShop() {
        return shop;
    }

    public static class Shop {
        @SerializedName("name")
        @Expose
        private String name;

        public String getName() {
            return name;
        }
    }
}
