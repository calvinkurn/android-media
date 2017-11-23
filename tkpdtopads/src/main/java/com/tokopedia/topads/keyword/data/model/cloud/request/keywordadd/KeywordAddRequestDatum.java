
package com.tokopedia.topads.keyword.data.model.cloud.request.keywordadd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeywordAddRequestDatum {

    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("keyword_tag")
    @Expose
    private String keywordTag;
    @SerializedName("keyword_type_id")
    @Expose
    private String keywordTypeId;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("toggle")
    @Expose
    private String toggle;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("source")
    @Expose
    private String source;

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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getToggle() {
        return toggle;
    }

    public void setToggle(String toggle) {
        this.toggle = toggle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
