package com.tokopedia.topads.keyword.domain.model;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class EditTopAdsKeywordDetailDomainModel {
    private String keywordId;

    private String keywordTag;

    private String keywordTypeId;

    private String groupId;

    private String shopId;

    private String priceBid;

    private int toggle;

    private String source;

    public String getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPriceBid() {
        return priceBid;
    }

    public void setPriceBid(String priceBid) {
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
