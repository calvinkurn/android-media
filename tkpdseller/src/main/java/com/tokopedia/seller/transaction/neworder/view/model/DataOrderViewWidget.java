package com.tokopedia.seller.transaction.neworder.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.transaction.neworder.domain.model.neworder.DataOrderDetailDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 7/14/17.
 */

public class DataOrderViewWidget implements Parcelable {
    private int dataOrderCount;
    private List<DataOrderDetailView> dataOrderDetailViews;

    public void setDataOrderCount(int dataOrderCount) {
        this.dataOrderCount = dataOrderCount;
    }

    public void setDataOrderDetailViews(List<DataOrderDetailView> dataOrderDetailViews) {
        this.dataOrderDetailViews = dataOrderDetailViews;
    }

    public int getDataOrderCount() {
        return dataOrderCount;
    }

    public List<DataOrderDetailView> getDataOrderDetailViews() {
        return dataOrderDetailViews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dataOrderCount);
        dest.writeTypedList(this.dataOrderDetailViews);
    }

    public DataOrderViewWidget() {
    }

    protected DataOrderViewWidget(Parcel in) {
        this.dataOrderCount = in.readInt();
        this.dataOrderDetailViews = in.createTypedArrayList(DataOrderDetailView.CREATOR);
    }

    public static final Parcelable.Creator<DataOrderViewWidget> CREATOR = new Parcelable.Creator<DataOrderViewWidget>() {
        @Override
        public DataOrderViewWidget createFromParcel(Parcel source) {
            return new DataOrderViewWidget(source);
        }

        @Override
        public DataOrderViewWidget[] newArray(int size) {
            return new DataOrderViewWidget[size];
        }
    };
}
