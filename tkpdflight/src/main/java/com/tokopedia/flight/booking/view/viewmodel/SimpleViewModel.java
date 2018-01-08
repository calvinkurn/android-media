package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alvarisi on 11/21/17.
 */

public class SimpleViewModel implements Parcelable {

    private String label;
    private String description;

    public SimpleViewModel() {
    }

    public SimpleViewModel(String label, String description) {
        this.label = label;
        this.description = description;
    }

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeString(this.description);
    }

    protected SimpleViewModel(Parcel in) {
        this.label = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<SimpleViewModel> CREATOR = new Parcelable.Creator<SimpleViewModel>() {
        @Override
        public SimpleViewModel createFromParcel(Parcel source) {
            return new SimpleViewModel(source);
        }

        @Override
        public SimpleViewModel[] newArray(int size) {
            return new SimpleViewModel[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SimpleViewModel && ((SimpleViewModel) obj).getLabel().equalsIgnoreCase(label);
    }
}
