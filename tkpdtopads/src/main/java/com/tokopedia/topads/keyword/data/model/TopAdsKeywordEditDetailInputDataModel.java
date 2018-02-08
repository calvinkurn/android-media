package com.tokopedia.topads.keyword.data.model;


import com.google.gson.annotations.SerializedName;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailInputDataModel {

    @SerializedName("keyword_id")
    private String keywordId;

    @SerializedName("group_id")
    private String groupId;

    @SerializedName("keyword_tag")
    private String keywordTag;

    @SerializedName("keyword_type_id")
    private String keywordTypeId;

    @SerializedName("price_bid")
    private double priceBid;

    @SerializedName("shop_id")
    private String shopId;

    @SerializedName("source")
    private String source;

    @SerializedName("toggle")
    private String toggle;

    public String getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getKeywordTag() {
        return keywordTag;
    }

    public void setKeywordTag(String keywordTag) {
        this.keywordTag = keywordTag;
    }

    public String getKeywordTypeId() {
        return keywordTypeId;
    }

    public void setKeywordTypeId(String keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
    }

    public double getPriceBid() {
        return priceBid;
    }

    public void setPriceBid(double priceBid) {
        this.priceBid = priceBid;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getToggle() {
        return toggle;
    }

    public void setToggle(String toggle) {
        this.toggle = toggle;
    }
}
