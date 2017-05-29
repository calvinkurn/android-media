package com.tokopedia.seller.topads.keyword.domain.model;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailInputDomainModel {
    private String keywordId;
    private String groupId;
    private String keywordTag;
    private int keywordTypeId;
    private double priceBid;
    private String shopId;
    private String toggle;

    public String getKeywordId() {
        return keywordId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getKeywordTag() {
        return keywordTag;
    }

    public int getKeywordTypeId() {
        return keywordTypeId;
    }

    public double getPriceBid() {
        return priceBid;
    }

    public String getShopId() {
        return shopId;
    }

    public String getToggle() {
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

    public void setKeywordTypeId(int keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
    }

    public void setPriceBid(double priceBid) {
        this.priceBid = priceBid;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setToggle(String toggle) {
        this.toggle = toggle;
    }
}
