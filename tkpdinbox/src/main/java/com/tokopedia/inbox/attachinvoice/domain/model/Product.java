package com.tokopedia.inbox.attachinvoice.domain.model;

/**
 * Created by Hendri on 21/03/18.
 */

public class Product {
    String name;
    String price;
    String thumbnailUrl;

    public Product(String name, String price, String thumbnailUrl) {
        this.name = name;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
