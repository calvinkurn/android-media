package com.tokopedia.flight.dashboard.view.fragment.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.dashboard.view.adapter.FlightClassesAdapterTypeFactory;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassViewModel implements Visitable<FlightClassesAdapterTypeFactory>, Parcelable {
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
    private String title;
    private int id;

    public FlightClassViewModel() {
    }

    protected FlightClassViewModel(Parcel in) {
        title = in.readString();
        id = in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(id);
    }

    @Override
    public int type(FlightClassesAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
