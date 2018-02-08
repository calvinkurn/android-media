package com.tokopedia.topads.keyword.domain.model;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailInputDomainModel {
    private String keywordId;
    private String groupId;
    private String keywordTag;
    private String keywordTypeId;
    private double priceBid;
    private String shopId;
    private int toggle;

    public String getKeywordId() {
        return keywordId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getKeywordTag() {
        return keywordTag;
    }

    public String getKeywordTypeId() {
        return keywordTypeId;
    }

    public double getPriceBid() {
        return priceBid;
    }

    public String getShopId() {
        return shopId;
    }

    public int getToggle() {
        return toggle;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setKeywordTag(String keywordTag) {
        this.keywordTag = keywordTag;
    }

    public void setKeywordTypeId(String keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
    }

    public void setPriceBid(double priceBid) {
        this.priceBid = priceBid;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setToggle(int toggle) {
        this.toggle = toggle;
    }
}
