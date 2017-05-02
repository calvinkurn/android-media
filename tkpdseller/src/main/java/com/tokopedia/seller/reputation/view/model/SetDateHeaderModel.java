package com.tokopedia.seller.reputation.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.topads.view.model.TypeBasedModel;

/**
 * Created by normansyahputa on 3/17/17.
 */

public class SetDateHeaderModel extends TypeBasedModel implements Parcelable {
    public static final int TYPE = 1292832;
    public static final Parcelable.Creator<SetDateHeaderModel> CREATOR = new Parcelable.Creator<SetDateHeaderModel>() {
        @Override
        public SetDateHeaderModel createFromParcel(Parcel source) {
            return new SetDateHeaderModel(source);
        }

        @Override
        public SetDateHeaderModel[] newArray(int size) {
            return new SetDateHeaderModel[size];
        }
    };
    String startDate;
    String endDate;
    long sDate;
    long eDate;

    public SetDateHeaderModel() {
        super(TYPE);
    }

    protected SetDateHeaderModel(Parcel in) {
        this();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.sDate = in.readLong();
        this.eDate = in.readLong();
    }

    public long getsDate() {
        return sDate;
    }

    public void setsDate(long sDate) {
        this.sDate = sDate;
    }

    public long geteDate() {
        return eDate;
    }

    public void seteDate(long eDate) {
        this.eDate = eDate;
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
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeLong(this.sDate);
        dest.writeLong(this.eDate);
    }
}
