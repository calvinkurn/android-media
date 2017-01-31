package com.tokopedia.seller.topads.model.other;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nathaniel on 1/27/2017.
 */

public class FilterStatus {

    private int value;
    private String name;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}