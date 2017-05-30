package com.tokopedia.seller.topads.keyword.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.topads.data.model.data.Ad;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public class KeywordAd implements Ad, Parcelable {
    private String id;
    private String groupId;
    private String keywordTypeId;

    private String keywordTag;
    private int status;
    private String statusDesc;
    private String statAvgClick;
    private String statTotalSpent;
    private String statTotalImpression;
    private String statTotalClick;
    private String statTotalCtr;
    private String statTotalConversion;
    private String priceBidFmt;
    private String labelPerClick;
    private String keywordTypeDesc;

    public KeywordAd() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setKeywordTypeId(String keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
    }

    public void setKeywordTag(String keywordTag) {
        this.keywordTag = keywordTag;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setStatAvgClick(String statAvgClick) {
        this.statAvgClick = statAvgClick;
    }

    public void setStatTotalSpent(String statTotalSpent) {
        this.statTotalSpent = statTotalSpent;
    }

    public void setStatTotalImpression(String statTotalImpression) {
        this.statTotalImpression = statTotalImpression;
    }

    public void setStatTotalClick(String statTotalClick) {
        this.statTotalClick = statTotalClick;
    }

    public void setStatTotalCtr(String statTotalCtr) {
        this.statTotalCtr = statTotalCtr;
    }

    public void setStatTotalConversion(String statTotalConversion) {
        this.statTotalConversion = statTotalConversion;
    }

    public void setPriceBidFmt(String priceBidFmt) {
        this.priceBidFmt = priceBidFmt;
    }

    public void setLabelPerClick(String labelPerClick) {
        this.labelPerClick = labelPerClick;
    }

    public void setKeywordTypeDesc(String keywordTypeDesc) {
        this.keywordTypeDesc = keywordTypeDesc;
    }

    @Override
    public String getId() {
        return id;
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
        return 0;
    }

    @Override
    public String getPriceBidFmt() {
        return priceBidFmt;
    }

    @Override
    public String getPriceDailyFmt() {
        return null;
    }

    @Override
    public String getPriceDailySpentFmt() {
        return null;
    }

    @Override
    public String getPriceDailyBar() {
        return null;
    }

    @Override
    public String getStartDate() {
        return null;
    }

    @Override
    public String getStartTime() {
        return null;
    }

    @Override
    public String getEndDate() {
        return null;
    }

    @Override
    public String getEndTime() {
        return null;
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
        return null;
    }

    @Override
    public String getLabelPerClick() {
        return labelPerClick;
    }

    @Override
    public String getLabelOf() {
        return null;
    }

    @Override
    public String getName() {
        return keywordTag;
    }

    public String getkeywordTypeDesc() {
        return keywordTypeDesc;
    }


    public String getGroupId() {
        return groupId;
    }

    public String getKeywordTypeId() {
        return keywordTypeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.groupId);
        dest.writeString(this.keywordTypeId);
        dest.writeString(this.keywordTag);
        dest.writeInt(this.status);
        dest.writeString(this.statusDesc);
        dest.writeString(this.statAvgClick);
        dest.writeString(this.statTotalSpent);
        dest.writeString(this.statTotalImpression);
        dest.writeString(this.statTotalClick);
        dest.writeString(this.statTotalCtr);
        dest.writeString(this.statTotalConversion);
        dest.writeString(this.priceBidFmt);
        dest.writeString(this.labelPerClick);
        dest.writeString(this.keywordTypeDesc);
    }

    protected KeywordAd(Parcel in) {
        this.id = in.readString();
        this.groupId = in.readString();
        this.keywordTypeId = in.readString();
        this.keywordTag = in.readString();
        this.status = in.readInt();
        this.statusDesc = in.readString();
        this.statAvgClick = in.readString();
        this.statTotalSpent = in.readString();
        this.statTotalImpression = in.readString();
        this.statTotalClick = in.readString();
        this.statTotalCtr = in.readString();
        this.statTotalConversion = in.readString();
        this.priceBidFmt = in.readString();
        this.labelPerClick = in.readString();
        this.keywordTypeDesc = in.readString();
    }

    public static final Creator<KeywordAd> CREATOR = new Creator<KeywordAd>() {
        @Override
        public KeywordAd createFromParcel(Parcel source) {
            return new KeywordAd(source);
        }

        @Override
        public KeywordAd[] newArray(int size) {
            return new KeywordAd[size];
        }
    };
}
