package com.tokopedia.seller.opportunity.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/6/17.
 */

public class SortingTypeViewModel implements Parcelable{
    int sortingTypeId;
    String sortingTypeName;

    public SortingTypeViewModel() {
    }

    public SortingTypeViewModel(String sortingTypeName, int sortingTypeId) {
        this.sortingTypeId = sortingTypeId;
        this.sortingTypeName= sortingTypeName;
    }

    protected SortingTypeViewModel(Parcel in) {
        sortingTypeId = in.readInt();
        sortingTypeName = in.readString();
    }

    public static final Creator<SortingTypeViewModel> CREATOR = new Creator<SortingTypeViewModel>() {
        @Override
        public SortingTypeViewModel createFromParcel(Parcel in) {
            return new SortingTypeViewModel(in);
        }

        @Override
        public SortingTypeViewModel[] newArray(int size) {
            return new SortingTypeViewModel[size];
        }
    };

    public int getSortingTypeId() {
        return sortingTypeId;
    }

    public void setSortingTypeId(int sortingTypeId) {
        this.sortingTypeId = sortingTypeId;
    }

    public String getSortingTypeName() {
        return sortingTypeName;
    }

    public void setSortingTypeName(String sortingTypeName) {
        this.sortingTypeName = sortingTypeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sortingTypeId);
        dest.writeString(sortingTypeName);
    }
}
