package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class SchedulesViewModel implements Parcelable {

    int startDate;
    int endDate;
    String aDdress;
    private List<PackageViewModel> packages = null;

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


    public List<PackageViewModel> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageViewModel> packages) {
        this.packages = packages;
    }

    public SchedulesViewModel() {
    }

    protected SchedulesViewModel(Parcel in) {
        startDate = in.readInt();
        endDate = in.readInt();
        aDdress = in.readString();
        if (in.readByte() == 0x01) {
            packages = new ArrayList<PackageViewModel>();
            in.readList(packages, PackageViewModel.class.getClassLoader());
        } else {
            packages = null;
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
        if (packages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(packages);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SchedulesViewModel> CREATOR = new Parcelable.Creator<SchedulesViewModel>() {
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