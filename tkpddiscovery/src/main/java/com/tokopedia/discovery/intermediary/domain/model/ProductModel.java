package com.tokopedia.discovery.intermediary.domain.model;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.CurrencyFormatHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private int trackerPosition;
    private String trackerAttribution;
    private String trackerListName;

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

    public void setTrackerPosition(int trackerPosition) {
        this.trackerPosition = trackerPosition;
    }

    public int getTrackerPosition() {
        return trackerPosition;
    }

    public void setTrackerAttribution(String trackerAttribution) {
        this.trackerAttribution = trackerAttribution;
    }

    public String getTrackerAttribution() {
        return trackerAttribution;
    }

    public void setTrackerListName(String trackerListName) {
        this.trackerListName = trackerListName;
    }

    public String getTrackerListName() {
        return trackerListName;
    }

    public Map<String, Object> generateImpressionDataLayer() {
        return DataLayer.mapOf(
                "name", getName(),
                "id", getId(),
                "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                        getPrice()
                )),
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "list", getTrackerListName(),
                "position", getTrackerPosition(),
                "home_attribution", getTrackerAttribution()
        );
    }

    public Map<String, Object> generateClickDataLayer() {
        return DataLayer.mapOf(
                "name", getName(),
                "id", getId(),
                "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(getPrice())),
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "position", getTrackerPosition(),
                "home_attribution", getTrackerAttribution()
        );
    }
}
