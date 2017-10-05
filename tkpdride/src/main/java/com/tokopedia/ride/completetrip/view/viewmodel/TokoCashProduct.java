package com.tokopedia.ride.completetrip.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 7/10/17.
 */

public class TokoCashProduct implements Parcelable {
    private String id;
    private String value;
    private String title;

    public TokoCashProduct() {
    }

    protected TokoCashProduct(Parcel in) {
        id = in.readString();
        value = in.readString();
        title = in.readString();
    }

    public static final Creator<TokoCashProduct> CREATOR = new Creator<TokoCashProduct>() {
        @Override
        public TokoCashProduct createFromParcel(Parcel in) {
            return new TokoCashProduct(in);
        }

        @Override
        public TokoCashProduct[] newArray(int size) {
            return new TokoCashProduct[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(value);
        parcel.writeString(title);
    }
}
