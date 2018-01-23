package com.tokopedia.transaction.pickuppoint.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class Data implements Parcelable {
    private ArrayList<Store> stores;
    private int page;
    private boolean prevPage;
    private boolean nextPage;

    public Data() {
    }

    protected Data(Parcel in) {
        stores = in.createTypedArrayList(Store.CREATOR);
        page = in.readInt();
        prevPage = in.readByte() != 0;
        nextPage = in.readByte() != 0;
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isPrevPage() {
        return prevPage;
    }

    public void setPrevPage(boolean prevPage) {
        this.prevPage = prevPage;
    }

    public boolean isNextPage() {
        return nextPage;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(stores);
        dest.writeInt(page);
        dest.writeByte((byte) (prevPage ? 1 : 0));
        dest.writeByte((byte) (nextPage ? 1 : 0));
    }
}
