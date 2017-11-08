package com.tokopedia.topads.dashboard.data.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.dashboard.view.model.StateTypeBasedModel;

/**
 * Created by zulfikarrahman on 12/14/16.
 */

public class GroupAd extends StateTypeBasedModel implements Ad, Parcelable {

    public static final int TYPE = 19294123;
    @SerializedName("group_id")
    @Expose
    private long id;
    @SerializedName("group_status")
    @Expose
    private int status;
    @SerializedName("group_status_desc")
    @Expose
    private String statusDesc;
    @SerializedName("group_status_toogle")
    @Expose
    private int statusToogle;
    @SerializedName("group_price_bid_fmt")
    @Expose
    private String priceBidFmt;
    @SerializedName("group_price_daily_fmt")
    @Expose
    private String priceDailyFmt;
    @SerializedName("group_price_daily_spent_fmt")
    @Expose
    private String priceDailySpentFmt;
    @SerializedName("group_price_daily_bar")
    @Expose
    private String priceDailyBar;
    @SerializedName("group_editable")
    @Expose
    private int editable;
    @SerializedName("group_start_date")
    @Expose
    private String startDate;
    @SerializedName("group_start_time")
    @Expose
    private String startTime;
    @SerializedName("group_end_date")
    @Expose
    private String endDate;
    @SerializedName("group_end_time")
    @Expose
    private String endTime;
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
    @SerializedName("group_name")
    @Expose
    private String name;
    @SerializedName("group_moderated")
    @Expose
    private int adModerated;
    @SerializedName("group_moderated_reason")
    @Expose
    private String adModeratedReason;
    @SerializedName("total_item")
    @Expose
    private int totalItem;
    @SerializedName("positive_count")
    @Expose
    private int positiveCount;
    @SerializedName("negative_count")
    @Expose
    private int negativeCount;

    private String keywordTotal;

    public String getKeywordTotal() {
        return keywordTotal;
    }

    public void setKeywordTotal(String keywordTotal) {
        this.keywordTotal = keywordTotal;
    }

    private GetSuggestionResponse.Datum datum;

    public GroupAd() {

    }

    public int getPositiveCount() {
        return positiveCount;
    }

    public void setPositiveCount(int positiveCount) {
        this.positiveCount = positiveCount;
    }

    public int getNegativeCount() {
        return negativeCount;
    }

    public void setNegativeCount(int negativeCount) {
        this.negativeCount = negativeCount;
    }

    @Override
    public String getPriceDailySpentFmt() {
        return priceDailySpentFmt;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getStatusDesc() {
        return statusDesc;
    }

    @Override
    public int getStatusToogle() {
        return statusToogle;
    }

    @Override
    public String getPriceBidFmt() {
        return priceBidFmt;
    }

    @Override
    public String getPriceDailyFmt() {
        return priceDailyFmt;
    }

    @Override
    public String getPriceDailyBar() {
        return priceDailyBar;
    }

    public int getEditable() {
        return editable;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }

    @Override
    public String getStatAvgClick() {
        return statAvgClick;
    }

    @Override
    public String getStatTotalSpent() {
        return statTotalSpent;
    }

    @Override
    public String getStatTotalImpression() {
        return statTotalImpression;
    }

    @Override
    public String getStatTotalClick() {
        return statTotalClick;
    }

    @Override
    public String getStatTotalCtr() {
        return statTotalCtr;
    }

    @Override
    public String getStatTotalConversion() {
        return statTotalConversion;
    }

    @Override
    public String getLabelEdit() {
        return labelEdit;
    }

    @Override
    public String getLabelPerClick() {
        return labelPerClick;
    }

    @Override
    public String getLabelOf() {
        return labelOf;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAdModerated() {
        return adModerated;
    }

    public String getAdModeratedReason() {
        return adModeratedReason;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public GetSuggestionResponse.Datum getDatum() {
        return datum;
    }

    public void setDatum(GetSuggestionResponse.Datum datum) {
        this.datum = datum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupAd groupAd = (GroupAd) o;

        if (id != groupAd.id) return false;
        return name != null ? name.equals(groupAd.name) : groupAd.name == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.status);
        dest.writeString(this.statusDesc);
        dest.writeInt(this.statusToogle);
        dest.writeString(this.priceBidFmt);
        dest.writeString(this.priceDailyFmt);
        dest.writeString(this.priceDailySpentFmt);
        dest.writeString(this.priceDailyBar);
        dest.writeInt(this.editable);
        dest.writeString(this.startDate);
        dest.writeString(this.startTime);
        dest.writeString(this.endDate);
        dest.writeString(this.endTime);
        dest.writeString(this.statAvgClick);
        dest.writeString(this.statTotalSpent);
        dest.writeString(this.statTotalImpression);
        dest.writeString(this.statTotalClick);
        dest.writeString(this.statTotalCtr);
        dest.writeString(this.statTotalConversion);
        dest.writeString(this.labelEdit);
        dest.writeString(this.labelPerClick);
        dest.writeString(this.labelOf);
        dest.writeString(this.name);
        dest.writeInt(this.adModerated);
        dest.writeString(this.adModeratedReason);
        dest.writeInt(this.totalItem);
        dest.writeInt(this.positiveCount);
        dest.writeInt(this.negativeCount);
        dest.writeString(this.keywordTotal);
        dest.writeParcelable(this.datum, flags);
    }

    protected GroupAd(Parcel in) {
        this.id = in.readLong();
        this.status = in.readInt();
        this.statusDesc = in.readString();
        this.statusToogle = in.readInt();
        this.priceBidFmt = in.readString();
        this.priceDailyFmt = in.readString();
        this.priceDailySpentFmt = in.readString();
        this.priceDailyBar = in.readString();
        this.editable = in.readInt();
        this.startDate = in.readString();
        this.startTime = in.readString();
        this.endDate = in.readString();
        this.endTime = in.readString();
        this.statAvgClick = in.readString();
        this.statTotalSpent = in.readString();
        this.statTotalImpression = in.readString();
        this.statTotalClick = in.readString();
        this.statTotalCtr = in.readString();
        this.statTotalConversion = in.readString();
        this.labelEdit = in.readString();
        this.labelPerClick = in.readString();
        this.labelOf = in.readString();
        this.name = in.readString();
        this.adModerated = in.readInt();
        this.adModeratedReason = in.readString();
        this.totalItem = in.readInt();
        this.positiveCount = in.readInt();
        this.negativeCount = in.readInt();
        this.keywordTotal = in.readString();
        this.datum = in.readParcelable(GetSuggestionResponse.Datum.class.getClassLoader());
    }

    public static final Creator<GroupAd> CREATOR = new Creator<GroupAd>() {
        @Override
        public GroupAd createFromParcel(Parcel source) {
            return new GroupAd(source);
        }

        @Override
        public GroupAd[] newArray(int size) {
            return new GroupAd[size];
        }
    };
}
