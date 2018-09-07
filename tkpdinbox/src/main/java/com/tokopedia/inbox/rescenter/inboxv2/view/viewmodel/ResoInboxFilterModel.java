package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yfsx on 29/01/18.
 */

public class ResoInboxFilterModel implements Parcelable {
    private List<FilterViewModel> filterViewModelList;
    private List<Integer> selectedFilterList;
    private String dateFromString;
    private String dateToString;
    private Date dateFrom;
    private Date dateTo;

    public ResoInboxFilterModel() {
        this.filterViewModelList = new ArrayList<>();
        this.selectedFilterList = new ArrayList<>();
        this.dateFromString = "";
        this.dateToString = "";
        this.dateTo = null;
        this.dateFrom = null;
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

    public String getDateFromString() {
        return dateFromString;
    }

    public void setDateFromString(String dateFromString) {
        this.dateFromString = dateFromString;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
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
        dest.writeString(this.dateFromString);
        dest.writeString(this.dateToString);
        dest.writeLong(this.dateFrom != null ? this.dateFrom.getTime() : -1);
        dest.writeLong(this.dateTo != null ? this.dateTo.getTime() : -1);
    }

    protected ResoInboxFilterModel(Parcel in) {
        this.filterViewModelList = in.createTypedArrayList(FilterViewModel.CREATOR);
        this.selectedFilterList = new ArrayList<Integer>();
        in.readList(this.selectedFilterList, Integer.class.getClassLoader());
        this.dateFromString = in.readString();
        this.dateToString = in.readString();
        long tmpDateFrom = in.readLong();
        this.dateFrom = tmpDateFrom == -1 ? null : new Date(tmpDateFrom);
        long tmpDateTo = in.readLong();
        this.dateTo = tmpDateTo == -1 ? null : new Date(tmpDateTo);
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
