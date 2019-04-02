package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class OrderDetailViewModel implements Parcelable {
    private int id;
    private int returnable;

    public OrderDetailViewModel(int id, int returnable) {
        this.id = id;
        this.returnable = returnable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReturnable() {
        return returnable;
    }

    public void setReturnable(int returnable) {
        this.returnable = returnable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.returnable);
    }

    protected OrderDetailViewModel(Parcel in) {
        this.id = in.readInt();
        this.returnable = in.readInt();
    }

    public static final Creator<OrderDetailViewModel> CREATOR = new Creator<OrderDetailViewModel>() {
        @Override
        public OrderDetailViewModel createFromParcel(Parcel source) {
            return new OrderDetailViewModel(source);
        }

        @Override
        public OrderDetailViewModel[] newArray(int size) {
            return new OrderDetailViewModel[size];
        }
    };
}
