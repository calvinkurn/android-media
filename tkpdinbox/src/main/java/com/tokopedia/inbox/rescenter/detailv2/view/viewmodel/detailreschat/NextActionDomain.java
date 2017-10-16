package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class NextActionDomain implements Parcelable {

    private String last;
    private NextActionDetailDomain detail;

    public NextActionDomain(String last, NextActionDetailDomain detail) {
        this.last = last;
        this.detail = detail;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public NextActionDetailDomain getDetail() {
        return detail;
    }

    public void setDetail(NextActionDetailDomain detail) {
        this.detail = detail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.last);
        dest.writeParcelable(this.detail, flags);
    }

    protected NextActionDomain(Parcel in) {
        this.last = in.readString();
        this.detail = in.readParcelable(NextActionDetailDomain.class.getClassLoader());
    }

    public static final Parcelable.Creator<NextActionDomain> CREATOR = new Parcelable.Creator<NextActionDomain>() {
        @Override
        public NextActionDomain createFromParcel(Parcel source) {
            return new NextActionDomain(source);
        }

        @Override
        public NextActionDomain[] newArray(int size) {
            return new NextActionDomain[size];
        }
    };
}
