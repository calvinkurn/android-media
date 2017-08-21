package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author by nisie on 8/21/17.
 */

public class FilterViewModel implements Parcelable {

    private String name;
    private ArrayList<OptionViewModel> listChild;
    private boolean isSelected;
    private boolean isActive;
    private int position;

    public FilterViewModel() {
    }

    public FilterViewModel(String name, ArrayList<OptionViewModel> listChild) {
        this.name = name;
        this.listChild = listChild;
        this.isSelected = false;
        this.isActive = false;
        this.position = 0;
    }

    protected FilterViewModel(Parcel in) {
        name = in.readString();
        listChild = in.createTypedArrayList(OptionViewModel.CREATOR);
        isSelected = in.readByte() != 0;
        isActive = in.readByte() != 0;
        position = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(listChild);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeInt(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilterViewModel> CREATOR = new Creator<FilterViewModel>() {
        @Override
        public FilterViewModel createFromParcel(Parcel in) {
            return new FilterViewModel(in);
        }

        @Override
        public FilterViewModel[] newArray(int size) {
            return new FilterViewModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<OptionViewModel> getListChild() {
        return listChild;
    }

    public void setListChild(ArrayList<OptionViewModel> listChild) {
        this.listChild = listChild;
    }
}

