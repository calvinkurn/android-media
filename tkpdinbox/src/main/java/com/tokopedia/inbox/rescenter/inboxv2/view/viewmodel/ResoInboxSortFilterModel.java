package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yfsx on 26/01/18.
 */

public class ResoInboxSortFilterModel implements Parcelable {
    private String startID;
    private int sortBy;
    private int asc;
    private List<Integer> filters;
    private String startDateString;
    private String endDateString;
    private Date startDate;
    private Date endDate;

    public String getStartID() {
        return startID;
    }

    public void setStartID(String startID) {
        this.startID = startID;
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public int getAsc() {
        return asc;
    }

    public void setAsc(int asc) {
        this.asc = asc;
    }

    public List<Integer> getFilters() {
        return filters;
    }

    public void setFilters(List<Integer> filters) {
        this.filters = filters;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ResoInboxSortFilterModel() {
        setStartDateString("");
        setSortBy(0);
        setAsc(0);
        setEndDateString("");
        setStartID("");
        setFilters(new ArrayList<Integer>());
        setStartDate(new Date());
        setEndDate(new Date());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startID);
        dest.writeInt(this.sortBy);
        dest.writeInt(this.asc);
        dest.writeList(this.filters);
        dest.writeString(this.startDateString);
        dest.writeString(this.endDateString);
        dest.writeLong(this.startDate != null ? this.startDate.getTime() : -1);
        dest.writeLong(this.endDate != null ? this.endDate.getTime() : -1);
    }

    protected ResoInboxSortFilterModel(Parcel in) {
        this.startID = in.readString();
        this.sortBy = in.readInt();
        this.asc = in.readInt();
        this.filters = new ArrayList<Integer>();
        in.readList(this.filters, Integer.class.getClassLoader());
        this.startDateString = in.readString();
        this.endDateString = in.readString();
        long tmpStartDate = in.readLong();
        this.startDate = tmpStartDate == -1 ? null : new Date(tmpStartDate);
        long tmpEndDate = in.readLong();
        this.endDate = tmpEndDate == -1 ? null : new Date(tmpEndDate);
    }

    public static final Creator<ResoInboxSortFilterModel> CREATOR = new Creator<ResoInboxSortFilterModel>() {
        @Override
        public ResoInboxSortFilterModel createFromParcel(Parcel source) {
            return new ResoInboxSortFilterModel(source);
        }

        @Override
        public ResoInboxSortFilterModel[] newArray(int size) {
            return new ResoInboxSortFilterModel[size];
        }
    };
}

