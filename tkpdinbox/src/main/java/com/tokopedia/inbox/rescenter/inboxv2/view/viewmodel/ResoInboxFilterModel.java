package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 26/01/18.
 */

public class ResoInboxFilterModel implements Parcelable {
    private String startID;
    private int sortBy;
    private int asc;
    private List<Integer> filters;
    private String startDate;
    private String endDate;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
    }

    public ResoInboxFilterModel() {
        setStartDate("");
        setSortBy(0);
        setAsc(0);
        setEndDate("");
        setStartID("");
        setFilters(new ArrayList<Integer>());
    }

    protected ResoInboxFilterModel(Parcel in) {
        this.startID = in.readString();
        this.sortBy = in.readInt();
        this.asc = in.readInt();
        this.filters = new ArrayList<Integer>();
        in.readList(this.filters, Integer.class.getClassLoader());
        this.startDate = in.readString();
        this.endDate = in.readString();
    }

    public static final Parcelable.Creator<ResoInboxFilterModel> CREATOR = new Parcelable.Creator<ResoInboxFilterModel>() {
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

