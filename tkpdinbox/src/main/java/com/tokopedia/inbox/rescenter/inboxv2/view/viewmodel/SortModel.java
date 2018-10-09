package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.inbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 26/01/18.
 */

public class SortModel implements Parcelable {
    public String sortName;
    public int sortId;
    public int sortById;
    public int ascId;

    public SortModel() {
    }

    public SortModel(String sortName, int sortId, int sortById, int ascId) {
        this.sortName = sortName;
        this.sortId = sortId;
        this.sortById = sortById;
        this.ascId = ascId;
    }

    public static List<SortModel> getSortList(Context context) {
        List<SortModel> list = new ArrayList<>();
        list.add(new SortModel(
                context.getResources().getString(R.string.string_sort_2), 2, 2, 0));
        list.add(new SortModel(
                context.getResources().getString(R.string.string_sort_3), 3, 2, 1));
        list.add(new SortModel(
                context.getResources().getString(R.string.string_sort_1), 1, 1, 0));
        return list;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public int getSortById() {
        return sortById;
    }

    public void setSortById(int sortById) {
        this.sortById = sortById;
    }

    public int getAscId() {
        return ascId;
    }

    public void setAscId(int ascId) {
        this.ascId = ascId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sortName);
        dest.writeInt(this.sortId);
        dest.writeInt(this.sortById);
        dest.writeInt(this.ascId);
    }

    protected SortModel(Parcel in) {
        this.sortName = in.readString();
        this.sortId = in.readInt();
        this.sortById = in.readInt();
        this.ascId = in.readInt();
    }

    public static final Parcelable.Creator<SortModel> CREATOR = new Parcelable.Creator<SortModel>() {
        @Override
        public SortModel createFromParcel(Parcel source) {
            return new SortModel(source);
        }

        @Override
        public SortModel[] newArray(int size) {
            return new SortModel[size];
        }
    };
}
