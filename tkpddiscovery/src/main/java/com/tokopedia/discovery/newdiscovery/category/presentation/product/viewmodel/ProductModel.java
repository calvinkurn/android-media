package com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 3/27/17.
 */

public class ProductModel {

    private int id;
    private String imageUrl = "";
    private String name = "";
    private String price = "";
    private String shopName = "";
    private String shopLocation = "";
    private List<BadgeModel> badges = new ArrayList<>();
    private List<LabelModel> labels = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public List<BadgeModel> getBadges() {
        return badges;
    }

    public void setBadges(List<BadgeModel> badges) {
        this.badges = badges;
    }

    public List<LabelModel> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelModel> labels) {
        this.labels = labels;
    }
}
