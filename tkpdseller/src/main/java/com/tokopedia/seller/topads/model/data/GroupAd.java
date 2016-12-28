package com.tokopedia.seller.topads.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 12/14/16.
 */

public class GroupAd {
    @SerializedName("group_id")
    @Expose
    private int groupId;
    @SerializedName("total_item")
    @Expose
    private int totalItem;
    @SerializedName("group_status")
    @Expose
    private int groupStatus;
    @SerializedName("group_status_desc")
    @Expose
    private String groupStatusDesc;
    @SerializedName("group_status_toogle")
    @Expose
    private int groupStatusToogle;
    @SerializedName("group_price_bid_fmt")
    @Expose
    private String groupPriceBidFmt;
    @SerializedName("group_price_daily_fmt")
    @Expose
    private String groupPriceDailyFmt;
    @SerializedName("group_price_daily_spent_fmt")
    @Expose
    private String groupPriceDailySpentFmt;
    @SerializedName("group_price_daily_bar")
    @Expose
    private String groupPriceDailyBar;
    @SerializedName("group_editable")
    @Expose
    private int groupEditable;
    @SerializedName("group_start_date")
    @Expose
    private String groupStartDate;
    @SerializedName("group_start_time")
    @Expose
    private String groupStartTime;
    @SerializedName("group_end_date")
    @Expose
    private String groupEndDate;
    @SerializedName("group_end_time")
    @Expose
    private String groupEndTime;
    @SerializedName("group_moderated")
    @Expose
    private int groupModerated;
    @SerializedName("group_moderated_reason")
    @Expose
    private String groupModeratedReason;
    @SerializedName("group_name")
    @Expose
    private String groupName;
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

    /**
     * @return The groupId
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * @param groupId The group_id
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * @return The totalItem
     */
    public int getTotalItem() {
        return totalItem;
    }

    /**
     * @param totalItem The total_item
     */
    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    /**
     * @return The groupStatus
     */
    public int getGroupStatus() {
        return groupStatus;
    }

    /**
     * @param groupStatus The group_status
     */
    public void setGroupStatus(int groupStatus) {
        this.groupStatus = groupStatus;
    }

    /**
     * @return The groupStatusDesc
     */
    public String getGroupStatusDesc() {
        return groupStatusDesc;
    }

    /**
     * @param groupStatusDesc The group_status_desc
     */
    public void setGroupStatusDesc(String groupStatusDesc) {
        this.groupStatusDesc = groupStatusDesc;
    }

    /**
     * @return The groupStatusToogle
     */
    public int getGroupStatusToogle() {
        return groupStatusToogle;
    }

    /**
     * @param groupStatusToogle The group_status_toogle
     */
    public void setGroupStatusToogle(int groupStatusToogle) {
        this.groupStatusToogle = groupStatusToogle;
    }

    /**
     * @return The groupPriceBidFmt
     */
    public String getGroupPriceBidFmt() {
        return groupPriceBidFmt;
    }

    /**
     * @param groupPriceBidFmt The group_price_bid_fmt
     */
    public void setGroupPriceBidFmt(String groupPriceBidFmt) {
        this.groupPriceBidFmt = groupPriceBidFmt;
    }

    /**
     * @return The groupPriceDailyFmt
     */
    public String getGroupPriceDailyFmt() {
        return groupPriceDailyFmt;
    }

    /**
     * @param groupPriceDailyFmt The group_price_daily_fmt
     */
    public void setGroupPriceDailyFmt(String groupPriceDailyFmt) {
        this.groupPriceDailyFmt = groupPriceDailyFmt;
    }

    /**
     * @return The groupPriceDailySpentFmt
     */
    public String getGroupPriceDailySpentFmt() {
        return groupPriceDailySpentFmt;
    }

    /**
     * @param groupPriceDailySpentFmt The group_price_daily_spent_fmt
     */
    public void setGroupPriceDailySpentFmt(String groupPriceDailySpentFmt) {
        this.groupPriceDailySpentFmt = groupPriceDailySpentFmt;
    }

    /**
     * @return The groupPriceDailyBar
     */
    public String getGroupPriceDailyBar() {
        return groupPriceDailyBar;
    }

    /**
     * @param groupPriceDailyBar The group_price_daily_bar
     */
    public void setGroupPriceDailyBar(String groupPriceDailyBar) {
        this.groupPriceDailyBar = groupPriceDailyBar;
    }

    /**
     * @return The groupEditable
     */
    public int getGroupEditable() {
        return groupEditable;
    }

    /**
     * @param groupEditable The group_editable
     */
    public void setGroupEditable(int groupEditable) {
        this.groupEditable = groupEditable;
    }

    /**
     * @return The groupStartDate
     */
    public String getGroupStartDate() {
        return groupStartDate;
    }

    /**
     * @param groupStartDate The group_start_date
     */
    public void setGroupStartDate(String groupStartDate) {
        this.groupStartDate = groupStartDate;
    }

    /**
     * @return The groupStartTime
     */
    public String getGroupStartTime() {
        return groupStartTime;
    }

    /**
     * @param groupStartTime The group_start_time
     */
    public void setGroupStartTime(String groupStartTime) {
        this.groupStartTime = groupStartTime;
    }

    /**
     * @return The groupEndDate
     */
    public String getGroupEndDate() {
        return groupEndDate;
    }

    /**
     * @param groupEndDate The group_end_date
     */
    public void setGroupEndDate(String groupEndDate) {
        this.groupEndDate = groupEndDate;
    }

    /**
     * @return The groupEndTime
     */
    public String getGroupEndTime() {
        return groupEndTime;
    }

    /**
     * @param groupEndTime The group_end_time
     */
    public void setGroupEndTime(String groupEndTime) {
        this.groupEndTime = groupEndTime;
    }

    /**
     * @return The groupModerated
     */
    public int getGroupModerated() {
        return groupModerated;
    }

    /**
     * @param groupModerated The group_moderated
     */
    public void setGroupModerated(int groupModerated) {
        this.groupModerated = groupModerated;
    }

    /**
     * @return The groupModeratedReason
     */
    public String getGroupModeratedReason() {
        return groupModeratedReason;
    }

    /**
     * @param groupModeratedReason The group_moderated_reason
     */
    public void setGroupModeratedReason(String groupModeratedReason) {
        this.groupModeratedReason = groupModeratedReason;
    }

    /**
     * @return The groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName The group_name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return The statAvgClick
     */
    public String getStatAvgClick() {
        return statAvgClick;
    }

    /**
     * @param statAvgClick The stat_avg_click
     */
    public void setStatAvgClick(String statAvgClick) {
        this.statAvgClick = statAvgClick;
    }

    /**
     * @return The statTotalSpent
     */
    public String getStatTotalSpent() {
        return statTotalSpent;
    }

    /**
     * @param statTotalSpent The stat_total_spent
     */
    public void setStatTotalSpent(String statTotalSpent) {
        this.statTotalSpent = statTotalSpent;
    }

    /**
     * @return The statTotalImpression
     */
    public String getStatTotalImpression() {
        return statTotalImpression;
    }

    /**
     * @param statTotalImpression The stat_total_impression
     */
    public void setStatTotalImpression(String statTotalImpression) {
        this.statTotalImpression = statTotalImpression;
    }

    /**
     * @return The statTotalClick
     */
    public String getStatTotalClick() {
        return statTotalClick;
    }

    /**
     * @param statTotalClick The stat_total_click
     */
    public void setStatTotalClick(String statTotalClick) {
        this.statTotalClick = statTotalClick;
    }

    /**
     * @return The statTotalCtr
     */
    public String getStatTotalCtr() {
        return statTotalCtr;
    }

    /**
     * @param statTotalCtr The stat_total_ctr
     */
    public void setStatTotalCtr(String statTotalCtr) {
        this.statTotalCtr = statTotalCtr;
    }

    /**
     * @return The statTotalConversion
     */
    public String getStatTotalConversion() {
        return statTotalConversion;
    }

    /**
     * @param statTotalConversion The stat_total_conversion
     */
    public void setStatTotalConversion(String statTotalConversion) {
        this.statTotalConversion = statTotalConversion;
    }

    /**
     * @return The labelEdit
     */
    public String getLabelEdit() {
        return labelEdit;
    }

    /**
     * @param labelEdit The label_edit
     */
    public void setLabelEdit(String labelEdit) {
        this.labelEdit = labelEdit;
    }

    /**
     * @return The labelPerClick
     */
    public String getLabelPerClick() {
        return labelPerClick;
    }

    /**
     * @param labelPerClick The label_per_click
     */
    public void setLabelPerClick(String labelPerClick) {
        this.labelPerClick = labelPerClick;
    }

    /**
     * @return The labelOf
     */
    public String getLabelOf() {
        return labelOf;
    }

    /**
     * @param labelOf The label_of
     */
    public void setLabelOf(String labelOf) {
        this.labelOf = labelOf;
    }

}
