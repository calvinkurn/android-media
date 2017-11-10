package com.tokopedia.topads.keyword.data.model.cloud;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class Datum {

    @SerializedName("keyword_id")
    @Expose
    private int keywordId;
    @SerializedName("keyword_tag")
    @Expose
    private String keywordTag;
    @SerializedName("group_id")
    @Expose
    private int groupId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("keyword_status")
    @Expose
    private int keywordStatus;
    @SerializedName("keyword_status_desc")
    @Expose
    private String keywordStatusDesc;
    @SerializedName("keyword_type_id")
    @Expose
    private String keywordTypeId;
    @SerializedName("keyword_type_desc")
    @Expose
    private String keywordTypeDesc;
    @SerializedName("keyword_status_toogle")
    @Expose
    private int keywordStatusToogle;
    @SerializedName("keyword_price_bid_fmt")
    @Expose
    private String keywordPriceBidFmt;
    @SerializedName("stat_avg_click")
    @Expose
    private String statAvgClick;
    @SerializedName("stat_total_spent")
    @Expose
    private String statTotalSpent;
    @SerializedName("stat_total_impression")
    @Expose
    private String statTotalImpression;
    @SerializedName("stat_total_click")
    @Expose
    private String statTotalClick;
    @SerializedName("stat_total_ctr")
    @Expose
    private String statTotalCtr;
    @SerializedName("stat_total_conversion")
    @Expose
    private String statTotalConversion;
    @SerializedName("label_edit")
    @Expose
    private String labelEdit;
    @SerializedName("label_per_click")
    @Expose
    private String labelPerClick;
    @SerializedName("label_of")
    @Expose
    private String labelOf;
    @SerializedName("group_bid")
    @Expose
    private int groupBid;

    public int getGroupBid() {
        return groupBid;
    }

    public void setGroupBid(int groupBid) {
        this.groupBid = groupBid;
    }

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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getKeywordStatus() {
        return keywordStatus;
    }

    public void setKeywordStatus(int keywordStatus) {
        this.keywordStatus = keywordStatus;
    }

    public String getKeywordStatusDesc() {
        return keywordStatusDesc;
    }

    public void setKeywordStatusDesc(String keywordStatusDesc) {
        this.keywordStatusDesc = keywordStatusDesc;
    }

    public String getKeywordTypeId() {
        return keywordTypeId;
    }

    public void setKeywordTypeId(String keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
    }

    public String getKeywordTypeDesc() {
        return keywordTypeDesc;
    }

    public void setKeywordTypeDesc(String keywordTypeDesc) {
        this.keywordTypeDesc = keywordTypeDesc;
    }

    public int getKeywordStatusToogle() {
        return keywordStatusToogle;
    }

    public void setKeywordStatusToogle(int keywordStatusToogle) {
        this.keywordStatusToogle = keywordStatusToogle;
    }

    public String getKeywordPriceBidFmt() {
        return keywordPriceBidFmt;
    }

    public void setKeywordPriceBidFmt(String keywordPriceBidFmt) {
        this.keywordPriceBidFmt = keywordPriceBidFmt;
    }

    public String getStatAvgClick() {
        return statAvgClick;
    }

    public void setStatAvgClick(String statAvgClick) {
        this.statAvgClick = statAvgClick;
    }

    public String getStatTotalSpent() {
        return statTotalSpent;
    }

    public void setStatTotalSpent(String statTotalSpent) {
        this.statTotalSpent = statTotalSpent;
    }

    public String getStatTotalImpression() {
        return statTotalImpression;
    }

    public void setStatTotalImpression(String statTotalImpression) {
        this.statTotalImpression = statTotalImpression;
    }

    public String getStatTotalClick() {
        return statTotalClick;
    }

    public void setStatTotalClick(String statTotalClick) {
        this.statTotalClick = statTotalClick;
    }

    public String getStatTotalCtr() {
        return statTotalCtr;
    }

    public void setStatTotalCtr(String statTotalCtr) {
        this.statTotalCtr = statTotalCtr;
    }

    public String getStatTotalConversion() {
        return statTotalConversion;
    }

    public void setStatTotalConversion(String statTotalConversion) {
        this.statTotalConversion = statTotalConversion;
    }

    public String getLabelEdit() {
        return labelEdit;
    }

    public void setLabelEdit(String labelEdit) {
        this.labelEdit = labelEdit;
    }

    public String getLabelPerClick() {
        return labelPerClick;
    }

    public void setLabelPerClick(String labelPerClick) {
        this.labelPerClick = labelPerClick;
    }

    public String getLabelOf() {
        return labelOf;
    }

    public void setLabelOf(String labelOf) {
        this.labelOf = labelOf;
    }

}
