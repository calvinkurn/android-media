package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

/**
 * @author by alvarisi on 11/21/17.
 */

public class SimpleViewModel implements Parcelable, ItemType {


    public static final int TYPE = 983;
    private String label;
    private String description;

    public SimpleViewModel() {
    }

    public SimpleViewModel(String label, String description) {
        this.label = label;
        this.description = description;
    }

    protected SimpleViewModel(Parcel in) {
        label = in.readString();
        description = in.readString();
    }

    public static final Creator<SimpleViewModel> CREATOR = new Creator<SimpleViewModel>() {
        @Override
        public SimpleViewModel createFromParcel(Parcel in) {
            return new SimpleViewModel(in);
        }

        @Override
        public SimpleViewModel[] newArray(int size) {
            return new SimpleViewModel[size];
        }
    };

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(label);
        parcel.writeString(description);
    }
}
