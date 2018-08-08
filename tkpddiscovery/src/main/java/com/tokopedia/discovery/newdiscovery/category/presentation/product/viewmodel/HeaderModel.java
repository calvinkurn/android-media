package com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alifa on 3/24/17.
 */

public class HeaderModel implements Parcelable {

    private String categoryName="";
    private String headerImageUrl="";

    public HeaderModel() {
    }

    public HeaderModel(String categoryName, String headerImageUrl) {
        this.categoryName = categoryName;
        this.headerImageUrl = headerImageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    protected HeaderModel(Parcel in) {
        categoryName = in.readString();
        headerImageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryName);
        dest.writeString(headerImageUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HeaderModel> CREATOR = new Parcelable.Creator<HeaderModel>() {
        @Override
        public HeaderModel createFromParcel(Parcel in) {
            return new HeaderModel(in);
        }

        @Override
        public HeaderModel[] newArray(int size) {
            return new HeaderModel[size];
        }
    };
}
