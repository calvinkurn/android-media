package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ShippingViewModel implements Parcelable {
    private int id;
    private String name;
    private ShippingDetailViewModel detail;

    public ShippingViewModel(int id, String name, ShippingDetailViewModel detail) {
        this.id = id;
        this.name = name;
        this.detail = detail;
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

    public ShippingDetailViewModel getDetail() {
        return detail;
    }

    public void setDetail(ShippingDetailViewModel detail) {
        this.detail = detail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.detail, flags);
    }

    protected ShippingViewModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.detail = in.readParcelable(ShippingDetailViewModel.class.getClassLoader());
    }

    public static final Creator<ShippingViewModel> CREATOR = new Creator<ShippingViewModel>() {
        @Override
        public ShippingViewModel createFromParcel(Parcel source) {
            return new ShippingViewModel(source);
        }

        @Override
        public ShippingViewModel[] newArray(int size) {
            return new ShippingViewModel[size];
        }
    };
}
