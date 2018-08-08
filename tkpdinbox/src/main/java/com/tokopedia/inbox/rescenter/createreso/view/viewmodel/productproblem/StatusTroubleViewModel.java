package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class StatusTroubleViewModel implements Parcelable {
    private int id;
    private String name;

    public StatusTroubleViewModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected StatusTroubleViewModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<StatusTroubleViewModel> CREATOR = new Creator<StatusTroubleViewModel>() {
        @Override
        public StatusTroubleViewModel createFromParcel(Parcel source) {
            return new StatusTroubleViewModel(source);
        }

        @Override
        public StatusTroubleViewModel[] newArray(int size) {
            return new StatusTroubleViewModel[size];
        }
    };
}
