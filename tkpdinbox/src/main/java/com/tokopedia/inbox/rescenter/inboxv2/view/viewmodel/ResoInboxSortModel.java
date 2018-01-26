package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfsx on 26/01/18.
 */

public class ResoInboxSortModel implements Parcelable {
    private List<SortModel> sortModelList;
    private int selectedSortId;

    public ResoInboxSortModel(List<SortModel> sortModelList, int selectedSortId) {
        this.sortModelList = sortModelList;
        this.selectedSortId = selectedSortId;
    }

    public List<SortModel> getSortModelList() {
        return sortModelList;
    }

    public void setSortModelList(List<SortModel> sortModelList) {
        this.sortModelList = sortModelList;
    }

    public int getSelectedSortId() {
        return selectedSortId;
    }

    public void setSelectedSortId(int selectedSortId) {
        this.selectedSortId = selectedSortId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.sortModelList);
        dest.writeInt(this.selectedSortId);
    }

    protected ResoInboxSortModel(Parcel in) {
        this.sortModelList = in.createTypedArrayList(SortModel.CREATOR);
        this.selectedSortId = in.readInt();
    }

    public static final Parcelable.Creator<ResoInboxSortModel> CREATOR = new Parcelable.Creator<ResoInboxSortModel>() {
        @Override
        public ResoInboxSortModel createFromParcel(Parcel source) {
            return new ResoInboxSortModel(source);
        }

        @Override
        public ResoInboxSortModel[] newArray(int size) {
            return new ResoInboxSortModel[size];
        }
    };
}
