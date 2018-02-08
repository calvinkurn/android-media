package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by vishal.gupta on 8/22/17.
 */

public class TipList implements Parcelable {

    private Boolean enabled;
    private ArrayList<Integer> list = null;
    private ArrayList<String> formattedCurrecyList = null;

    public TipList() {
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ArrayList<Integer> getList() {
        return list;
    }

    public void setList(ArrayList<Integer> list) {
        this.list = list;
    }

    public ArrayList<String> getFormattedCurrecyList() {
        return formattedCurrecyList;
    }

    public void setFormattedCurrecyList(ArrayList<String> formattedCurrecyList) {
        this.formattedCurrecyList = formattedCurrecyList;
    }

    protected TipList(Parcel in) {
        byte enabledVal = in.readByte();
        enabled = enabledVal == 0x02 ? null : enabledVal != 0x00;
        if (in.readByte() == 0x01) {
            list = new ArrayList<Integer>();
            in.readList(list, Integer.class.getClassLoader());
        } else {
            list = null;
        }
        if (in.readByte() == 0x01) {
            formattedCurrecyList = new ArrayList<String>();
            in.readList(formattedCurrecyList, String.class.getClassLoader());
        } else {
            formattedCurrecyList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (enabled == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        }
        if (list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(list);
        }
        if (formattedCurrecyList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(formattedCurrecyList);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<TipList> CREATOR = new Creator<TipList>() {
        @Override
        public TipList createFromParcel(Parcel in) {
            return new TipList(in);
        }

        @Override
        public TipList[] newArray(int size) {
            return new TipList[size];
        }
    };
}
