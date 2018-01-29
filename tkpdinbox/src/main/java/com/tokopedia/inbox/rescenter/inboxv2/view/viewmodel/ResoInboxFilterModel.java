package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 29/01/18.
 */

public class ResoInboxFilterModel implements Parcelable {
    private List<FilterViewModel> filterViewModelList;
    private List<Integer> selectedFilterList;
    private String dateFrom;
    private String dateTo;

    public ResoInboxFilterModel() {
    }

    public List<FilterViewModel> getFilterViewModelList() {
        return filterViewModelList;
    }

    public void setFilterViewModelList(List<FilterViewModel> filterViewModelList) {
        this.filterViewModelList = filterViewModelList;
    }

    public List<Integer> getSelectedFilterList() {
        return selectedFilterList;
    }

    public void setSelectedFilterList(List<Integer> selectedFilterList) {
        this.selectedFilterList = selectedFilterList;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.filterViewModelList);
        dest.writeList(this.selectedFilterList);
        dest.writeString(this.dateFrom);
        dest.writeString(this.dateTo);
    }

    protected ResoInboxFilterModel(Parcel in) {
        this.filterViewModelList = in.createTypedArrayList(FilterViewModel.CREATOR);
        this.selectedFilterList = new ArrayList<Integer>();
        in.readList(this.selectedFilterList, Integer.class.getClassLoader());
        this.dateFrom = in.readString();
        this.dateTo = in.readString();
    }

    public static final Creator<ResoInboxFilterModel> CREATOR = new Creator<ResoInboxFilterModel>() {
        @Override
        public ResoInboxFilterModel createFromParcel(Parcel source) {
            return new ResoInboxFilterModel(source);
        }

        @Override
        public ResoInboxFilterModel[] newArray(int size) {
            return new ResoInboxFilterModel[size];
        }
    };
}
