package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ResolutionDomain implements Parcelable {

    public static final Parcelable.Creator<ResolutionDomain> CREATOR = new Parcelable.Creator<ResolutionDomain>() {
        @Override
        public ResolutionDomain createFromParcel(Parcel source) {
            return new ResolutionDomain(source);
        }

        @Override
        public ResolutionDomain[] newArray(int size) {
            return new ResolutionDomain[size];
        }
    };
    private int id;
    private int freeReturn;
    private int status;

    public ResolutionDomain(int id, int freeReturn, int status) {
        this.id = id;
        this.freeReturn = freeReturn;
        this.status = status;
    }

    protected ResolutionDomain(Parcel in) {
        this.id = in.readInt();
        this.freeReturn = in.readInt();
        this.status = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(int freeReturn) {
        this.freeReturn = freeReturn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.freeReturn);
        dest.writeInt(this.status);
    }
}
