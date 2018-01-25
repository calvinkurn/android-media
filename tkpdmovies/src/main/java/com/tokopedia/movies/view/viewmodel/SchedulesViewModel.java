package com.tokopedia.movies.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.movies.data.entity.response.Group;

import java.util.ArrayList;
import java.util.List;

public class SchedulesViewModel implements Parcelable {

    int startDate;
    int endDate;
    String aDdress;
    private List<GroupViewModel> groups = null;

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public String getaDdress() {
        return aDdress;
    }

    public void setaDdress(String aDdress) {
        this.aDdress = aDdress;
    }

    public List<GroupViewModel> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupViewModel> groups) {
        this.groups = groups;
    }

    public SchedulesViewModel() {
    }

    protected SchedulesViewModel(Parcel in) {
        startDate = in.readInt();
        endDate = in.readInt();
        aDdress = in.readString();
        if (in.readByte() == 0x01) {
            groups = new ArrayList<GroupViewModel>();
            in.readList(groups, Group.class.getClassLoader());
        } else {
            groups = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(startDate);
        dest.writeInt(endDate);
        dest.writeString(aDdress);
        if (groups == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(groups);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<SchedulesViewModel> CREATOR = new Creator<SchedulesViewModel>() {
        @Override
        public SchedulesViewModel createFromParcel(Parcel in) {
            return new SchedulesViewModel(in);
        }

        @Override
        public SchedulesViewModel[] newArray(int size) {
            return new SchedulesViewModel[size];
        }
    };
}