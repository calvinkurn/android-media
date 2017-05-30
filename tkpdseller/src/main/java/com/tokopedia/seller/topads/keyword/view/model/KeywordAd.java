package com.tokopedia.seller.topads.keyword.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public class KeywordAd implements Parcelable {
    private String id;
    private String groupId;
    private String keywordType;
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

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getkeywordType() {
        return keywordType;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public String getStatAvgClick() {
        return statAvgClick;
    }

    public String getStatTotalSpent() {
        return statTotalSpent;
    }

    public String getStatTotalImpression() {
        return statTotalImpression;
    }

    public String getStatTotalClick() {
        return statTotalClick;
    }

    public String getStatTotalCtr() {
        return statTotalCtr;
    }

    public String getStatTotalConversion() {
        return statTotalConversion;
    }

    public String getPriceBidFmt() {
        return priceBidFmt;
    }

    public String getLabelPerClick() {
        return labelPerClick;
    }

    public KeywordAd() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.groupId);
        dest.writeString(this.keywordType);
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
    }

    protected KeywordAd(Parcel in) {
        this.id = in.readString();
        this.groupId = in.readString();
        this.keywordType = in.readString();
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
