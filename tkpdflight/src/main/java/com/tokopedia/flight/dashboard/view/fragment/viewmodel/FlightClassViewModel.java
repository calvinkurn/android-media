package com.tokopedia.flight.dashboard.view.fragment.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.dashboard.view.adapter.viewholder.FlightClassViewHolder;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassViewModel implements ItemType, Parcelable {
    private String title;
    private String id;

    public FlightClassViewModel() {
    }

    protected FlightClassViewModel(Parcel in) {
        title = in.readString();
        id = in.readString();
    }

    public static final Creator<FlightClassViewModel> CREATOR = new Creator<FlightClassViewModel>() {
        @Override
        public FlightClassViewModel createFromParcel(Parcel in) {
            return new FlightClassViewModel(in);
        }

        @Override
        public FlightClassViewModel[] newArray(int size) {
            return new FlightClassViewModel[size];
        }
    };

    @Override
    public int getType() {
        return FlightClassViewHolder.LAYOUT;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(id);
    }
}
