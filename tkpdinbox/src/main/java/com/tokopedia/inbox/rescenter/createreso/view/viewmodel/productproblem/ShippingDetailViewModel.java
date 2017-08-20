package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ShippingDetailViewModel implements Parcelable {
    private int id;
    private String name;

    public ShippingDetailViewModel(int id, String name) {
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

    protected ShippingDetailViewModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<ShippingDetailViewModel> CREATOR = new Creator<ShippingDetailViewModel>() {
        @Override
        public ShippingDetailViewModel createFromParcel(Parcel source) {
            return new ShippingDetailViewModel(source);
        }

        @Override
        public ShippingDetailViewModel[] newArray(int size) {
            return new ShippingDetailViewModel[size];
        }
    };
}
