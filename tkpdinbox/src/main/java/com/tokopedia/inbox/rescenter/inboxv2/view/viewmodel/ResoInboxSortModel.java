package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 26/01/18.
 */

public class ResoInboxSortModel implements Parcelable {
    private List<SortModel> sortModelList;
    private int selectedSortId;
    private SortModel selectedSortModel;

    public ResoInboxSortModel() {
        this.sortModelList = new ArrayList<>();
        this.selectedSortId = 0;
        this.selectedSortModel = new SortModel();
    }

    public ResoInboxSortModel(List<SortModel> sortModelList, int selectedSortId, SortModel selectedSortModel) {
        this.sortModelList = sortModelList;
        this.selectedSortId = selectedSortId;
        this.selectedSortModel = selectedSortModel;
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

    public SortModel getSelectedSortModel() {
        return selectedSortModel;
    }

    public void setSelectedSortModel(SortModel selectedSortModel) {
        this.selectedSortModel = selectedSortModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.sortModelList);
        dest.writeInt(this.selectedSortId);
        dest.writeParcelable(this.selectedSortModel, flags);
    }

    protected ResoInboxSortModel(Parcel in) {
        this.sortModelList = in.createTypedArrayList(SortModel.CREATOR);
        this.selectedSortId = in.readInt();
        this.selectedSortModel = in.readParcelable(SortModel.class.getClassLoader());
    }

    public static final Creator<ResoInboxSortModel> CREATOR = new Creator<ResoInboxSortModel>() {
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
