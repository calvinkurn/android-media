package com.tokopedia.seller.topads.keyword.domain.model;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailInputDomainModel {
    private int keywordId;
    private long groupId;
    private String keywordTag;
    private int keywordTypeId;
    private double priceBid;
    private String shopId;
    private int toggle;

    public int getKeywordId() {
        return keywordId;
    }

    public long getGroupId() {
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

    public int getToggle() {
        return toggle;
    }

    public void setKeywordId(int keywordId) {
        this.keywordId = keywordId;
    }

    public void setGroupId(long groupId) {
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

    public void setToggle(int toggle) {
        this.toggle = toggle;
    }
}
