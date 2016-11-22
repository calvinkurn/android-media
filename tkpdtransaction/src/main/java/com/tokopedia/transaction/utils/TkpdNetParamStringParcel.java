package com.tokopedia.transaction.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 11/21/16.
 */

public class TkpdNetParamStringParcel implements Parcelable {

    private List<String> keys = new ArrayList<>();
    private List<String> values = new ArrayList<>();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.keys);
        dest.writeStringList(this.values);
    }

    public void put(String key, String value) {
        this.keys.add(key);
        this.values.add(value);
    }

    public TKPDMapParam<String, String> getMapParam() {
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        for (int i = 0; i < keys.size(); i++) {
            maps.put(keys.get(i), values.get(i));
        }
        return maps;
    }

    public TkpdNetParamStringParcel() {
    }

    protected TkpdNetParamStringParcel(Parcel in) {
        this.keys = in.createStringArrayList();
        this.values = in.createStringArrayList();
    }

    public static final Parcelable.Creator<TkpdNetParamStringParcel> CREATOR
            = new Parcelable.Creator<TkpdNetParamStringParcel>() {
        @Override
        public TkpdNetParamStringParcel createFromParcel(Parcel source) {
            return new TkpdNetParamStringParcel(source);
        }

        @Override
        public TkpdNetParamStringParcel[] newArray(int size) {
            return new TkpdNetParamStringParcel[size];
        }
    };
}
