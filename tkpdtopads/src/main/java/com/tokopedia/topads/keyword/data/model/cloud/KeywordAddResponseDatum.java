package com.tokopedia.topads.keyword.data.model.cloud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class KeywordAddResponseDatum {
    @SerializedName("keyword_tag")
    @Expose
    private String keywordTag;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("keyword_type_id")
    @Expose
    private String keywordTypeId;
    @SerializedName("toggle")
    @Expose
    private int toggle;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("shop_id")
    @Expose
    private String shopId;

    public String getKeywordTag() {
        return keywordTag;
    }

    public void setKeywordTag(String keywordTag) {
        this.keywordTag = keywordTag;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getKeywordTypeId() {
        return keywordTypeId;
    }

    public void setKeywordTypeId(String keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
    }

    public int getToggle() {
        return toggle;
    }

    public void setToggle(int toggle) {
        this.toggle = toggle;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
