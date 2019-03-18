package com.tokopedia.discovery.search.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author erry on 23/02/17.
 */

public class SearchItem {
    @SerializedName("keyword")
    private String keyword;
    @SerializedName("url")
    private String url;
    @SerializedName("recom")
    private String recom;
    @SerializedName("applink")
    private String applink;
    @SerializedName("sc")
    private String sc;
    @SerializedName("imageURI")
    private String imageURI;
    @SerializedName("isOfficial")
    private boolean isOfficial;
    @SerializedName("location")
    private String location;
    @SerializedName("id")
    private String productId;
    @SerializedName("price")
    private String price;
    @SerializedName("id")
    private String peopleId;
    @SerializedName("affiliate_username")
    private String affiliateUserName;
    @SerializedName("iskol")
    private boolean isKOL;
    @SerializedName("post_count")
    private int postCount;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String eventAction;

    public String getEventAction() {
        return eventAction;
    }

    public void setEventAction(String eventAction) {
        this.eventAction = eventAction;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public String getRecom() {
        return recom;
    }

    public void setRecom(String recom) {
        this.recom = recom;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }

    public String getAffiliateUserName() {
        return affiliateUserName;
    }

    public void setAffiliateUserName(String affiliateUserName) {
        this.affiliateUserName = affiliateUserName;
    }

    public boolean isKOL() {
        return isKOL;
    }

    public void setKOL(boolean KOL) {
        isKOL = KOL;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }
}

