package com.tokopedia.transaction.pickup.alfamart.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class PickupPointResponse implements Parcelable {
    private Data data;

    public PickupPointResponse() {
    }

    protected PickupPointResponse(Parcel in) {
    }

    public static final Creator<PickupPointResponse> CREATOR = new Creator<PickupPointResponse>() {
        @Override
        public PickupPointResponse createFromParcel(Parcel in) {
            return new PickupPointResponse(in);
        }

        @Override
        public PickupPointResponse[] newArray(int size) {
            return new PickupPointResponse[size];
        }
    };

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
