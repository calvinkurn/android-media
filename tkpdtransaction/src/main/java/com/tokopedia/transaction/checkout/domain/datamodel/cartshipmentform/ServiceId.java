package com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ServiceId implements Parcelable {

    private String serviceId;
    private String spIds;

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setSpIds(String spIds) {
        this.spIds = spIds;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getSpIds() {
        return spIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serviceId);
        dest.writeString(this.spIds);
    }

    public ServiceId() {
    }

    protected ServiceId(Parcel in) {
        this.serviceId = in.readString();
        this.spIds = in.readString();
    }

    public static final Parcelable.Creator<ServiceId> CREATOR = new Parcelable.Creator<ServiceId>() {
        @Override
        public ServiceId createFromParcel(Parcel source) {
            return new ServiceId(source);
        }

        @Override
        public ServiceId[] newArray(int size) {
            return new ServiceId[size];
        }
    };
}
