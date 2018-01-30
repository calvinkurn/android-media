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
public class Shop {
    private String id;
    private String name;
    private String domain;
    private String tagline;
    private String location;
    private String city;
    private ImageShop imageShop;
    private boolean goldShop;
    private boolean goldShopBadge;
    private String luckyShop;
    private boolean shopIsOfficial;
    private String uri;
    private List<ImageProduct> imageProduct = new ArrayList<>();
    private String ownerId;
    private boolean isOwner;
    private List<Badge> badges = new ArrayList<>();

    public Shop(String id, String name, String domain, String tagline, String location,
                String city, ImageShop imageShop, boolean goldShop, boolean goldShopBadge,
                String luckyShop, boolean shopIsOfficial, String uri,
                List<ImageProduct> imageProduct, String ownerId, boolean isOwner,
                List<Badge> badges) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.tagline = tagline;
        this.location = location;
        this.city = city;
        this.imageShop = imageShop;
        this.goldShop = goldShop;
        this.goldShopBadge = goldShopBadge;
        this.luckyShop = luckyShop;
        this.shopIsOfficial = shopIsOfficial;
        this.uri = uri;
        this.imageProduct = imageProduct;
        this.ownerId = ownerId;
        this.isOwner = isOwner;
        this.badges = badges;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ImageShop getImageShop() {
        return imageShop;
    }

    public void setImageShop(ImageShop imageShop) {
        this.imageShop = imageShop;
    }

    public boolean isGoldShop() {
        return goldShop;
    }

    public void setGoldShop(boolean goldShop) {
        this.goldShop = goldShop;
    }

    public boolean isGoldShopBadge() {
        return goldShopBadge;
    }

    public void setGoldShopBadge(boolean goldShopBadge) {
        this.goldShopBadge = goldShopBadge;
    }

    public String getLuckyShop() {
        return luckyShop;
    }

    public void setLuckyShop(String luckyShop) {
        this.luckyShop = luckyShop;
    }

    public boolean isShop_is_official() {
        return shopIsOfficial;
    }

    public void setShop_is_official(boolean shop_is_official) {
        this.shopIsOfficial = shop_is_official;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<ImageProduct> getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(List<ImageProduct> imageProduct) {
        this.imageProduct = imageProduct;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean is_owner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        this.isOwner = owner;
    }
}
