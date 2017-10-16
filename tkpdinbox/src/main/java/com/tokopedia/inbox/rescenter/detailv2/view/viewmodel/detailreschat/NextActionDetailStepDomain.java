package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class NextActionDetailStepDomain implements Parcelable {

    private int status;

    private String name;

    public NextActionDetailStepDomain(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.name);
    }

    protected NextActionDetailStepDomain(Parcel in) {
        this.status = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<NextActionDetailStepDomain> CREATOR = new Parcelable.Creator<NextActionDetailStepDomain>() {
        @Override
        public NextActionDetailStepDomain createFromParcel(Parcel source) {
            return new NextActionDetailStepDomain(source);
        }

        @Override
        public NextActionDetailStepDomain[] newArray(int size) {
            return new NextActionDetailStepDomain[size];
        }
    };
}
