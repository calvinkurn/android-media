package com.tokopedia.seller.topads.keyword.domain.model;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class EditTopAdsKeywordDetailDomainModel {
    private int keywordId;

    private String keywordTag;

    private int keywordTypeId;

    private long groupId;

    private String shopId;

    private double priceBid;

    private int toggle;

    private String source;

    public int getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(int keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordTag() {
        return keywordTag;
    }

    public void setKeywordTag(String keywordTag) {
        this.keywordTag = keywordTag;
    }

    public int getKeywordTypeId() {
        return keywordTypeId;
    }

    public void setKeywordTypeId(int keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public double getPriceBid() {
        return priceBid;
    }

    public void setPriceBid(double priceBid) {
        this.priceBid = priceBid;
    }

    public int getToggle() {
        return toggle;
    }

    public void setToggle(int toggle) {
        this.toggle = toggle;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
