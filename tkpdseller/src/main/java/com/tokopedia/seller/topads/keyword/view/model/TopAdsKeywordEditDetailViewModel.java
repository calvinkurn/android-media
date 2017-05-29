package com.tokopedia.seller.topads.keyword.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef;

/**
 * @author sebastianuskh on 5/26/17.
 */

public class TopAdsKeywordEditDetailViewModel implements Parcelable {

    @KeywordTypeDef
    private int keywordTypeId;

    private String keywordTag;

    private double priceBid;

    private int keywordId;

    private long groupId;

    private int toggle;

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

    public int getKeywordId() {
        return keywordId;
    }

    public long getGroupId() {
        return groupId;
    }

    public int getToggle() {
        return toggle;
    }

    public void setKeywordId(int keywordId) {
        this.keywordId = keywordId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public void setToggle(int toggle) {
        this.toggle = toggle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.keywordTypeId);
        dest.writeString(this.keywordTag);
        dest.writeDouble(this.priceBid);
        dest.writeInt(this.keywordId);
        dest.writeLong(this.groupId);
        dest.writeInt(this.toggle);
    }

    public TopAdsKeywordEditDetailViewModel() {
    }

    protected TopAdsKeywordEditDetailViewModel(Parcel in) {
        this.keywordTypeId = in.readInt();
        this.keywordTag = in.readString();
        this.priceBid = in.readDouble();
        this.keywordId = in.readInt();
        this.groupId = in.readLong();
        this.toggle = in.readInt();
    }

    public static final Parcelable.Creator<TopAdsKeywordEditDetailViewModel> CREATOR = new Parcelable.Creator<TopAdsKeywordEditDetailViewModel>() {
        @Override
        public TopAdsKeywordEditDetailViewModel createFromParcel(Parcel source) {
            return new TopAdsKeywordEditDetailViewModel(source);
        }

        @Override
        public TopAdsKeywordEditDetailViewModel[] newArray(int size) {
            return new TopAdsKeywordEditDetailViewModel[size];
        }
    };
}
