package com.tokopedia.seller.topads.keyword.domain.model;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailInputDomainModel {
    private String keywordId;
    private String groudId;
    private String keywordTag;
    private String keywordTypeId;
    private String priceBid;

    public String getKeywordId() {
        return keywordId;
    }

    public String getGroudId() {
        return groudId;
    }

    public String getKeywordTag() {
        return keywordTag;
    }

    public String getKeywordTypeId() {
        return keywordTypeId;
    }

    public String getPriceBid() {
        return priceBid;
    }
}
