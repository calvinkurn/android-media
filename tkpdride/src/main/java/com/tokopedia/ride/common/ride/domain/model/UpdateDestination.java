package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vishal 28th Aug, 2017
 */

public class UpdateDestination implements Parcelable {
    private PendingPayment pendingPayment;


    public UpdateDestination() {

    }


    public PendingPayment getPendingPayment() {
        return pendingPayment;
    }

    public void setPendingPayment(PendingPayment pendingPayment) {
        this.pendingPayment = pendingPayment;
    }

    protected UpdateDestination(Parcel in) {
        pendingPayment = (PendingPayment) in.readValue(PendingPayment.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pendingPayment);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UpdateDestination> CREATOR = new Parcelable.Creator<UpdateDestination>() {
        @Override
        public UpdateDestination createFromParcel(Parcel in) {
            return new UpdateDestination(in);
        }

        @Override
        public UpdateDestination[] newArray(int size) {
            return new UpdateDestination[size];
        }
    };
}