package com.tokopedia.seller.topads.keyword.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef;

/**
 * @author sebastianuskh on 5/26/17.
 */

public class TopAdsKeywordEditDetailViewModel implements Parcelable {

    @KeywordTypeDef
    private int keywordType;

    private String keywordText;

    private double costPerClick;

    public int getKeywordType() {
        return keywordType;
    }

    public void setKeywordType(int keywordType) {
        this.keywordType = keywordType;
    }

    public String getKeywordText() {
        return keywordText;
    }

    public void setKeywordText(String keywordText) {
        this.keywordText = keywordText;
    }

    public double getCostPerClick() {
        return costPerClick;
    }

    public void setCostPerClick(double costPerClick) {
        this.costPerClick = costPerClick;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.keywordType);
        dest.writeString(this.keywordText);
        dest.writeDouble(this.costPerClick);
    }

    public TopAdsKeywordEditDetailViewModel() {
    }

    protected TopAdsKeywordEditDetailViewModel(Parcel in) {
        this.keywordType = in.readInt();
        this.keywordText = in.readString();
        this.costPerClick = in.readDouble();
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
