package com.tokopedia.seller.topads.keyword.view.model;

import com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef;

/**
 * @author sebastianuskh on 5/26/17.
 */

public class TopAdsKeywordEditDetailViewModel{

    @KeywordTypeDef
    private int keywordTypeId;

    private String keywordTag;

    private double priceBid;

    private String keywordId;
    private String groupId;
    private String toggle;

    public int getKeywordTypeId() {
        return keywordTypeId;
    }

    public void setKeywordTypeId(int keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
    }

    public String getKeywordTag() {
        return keywordTag;
    }

    public void setKeywordTag(String keywordTag) {
        this.keywordTag = keywordTag;
    }

    public double getPriceBid() {
        return priceBid;
    }

    public void setPriceBid(double priceBid) {
        this.priceBid = priceBid;
    }

    public String getKeywordId() {
        return keywordId;
    }

    public String getGroupId() {
        return groupId;
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

    public void setToggle(String toggle) {
        this.toggle = toggle;
    }
}
