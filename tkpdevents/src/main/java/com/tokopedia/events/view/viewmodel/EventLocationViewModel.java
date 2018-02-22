package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashwanityagi on 20/11/17.
 */

public class EventLocationViewModel implements Parcelable {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String searchName;
    private String district;
    private String icon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.categoryId);
        dest.writeString(this.name);
        dest.writeString(this.searchName);
        dest.writeString(this.district);
        dest.writeString(this.icon);
    }

    public EventLocationViewModel() {
    }

    protected EventLocationViewModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.categoryId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.searchName = in.readString();
        this.district = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<EventLocationViewModel> CREATOR = new Parcelable.Creator<EventLocationViewModel>() {
        @Override
        public EventLocationViewModel createFromParcel(Parcel source) {
            return new EventLocationViewModel(source);
        }

        @Override
        public EventLocationViewModel[] newArray(int size) {
            return new EventLocationViewModel[size];
        }
    };
}
