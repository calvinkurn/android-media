package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pranaymohapatra on 25/01/18.
 */

public class SearchViewModel implements Parcelable {
    private String url;
    private String displayName;
    private String title;
    private Integer salesPrice;
    private String cityName;
    private Integer minStartDate;
    private Integer maxEndDate;
    private String imageApp;
    private Integer isTop;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Integer salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getMinStartDate() {
        return minStartDate;
    }

    public void setMinStartDate(Integer minStartDate) {
        this.minStartDate = minStartDate;
    }

    public Integer getMaxEndDate() {
        return maxEndDate;
    }

    public void setMaxEndDate(Integer maxEndDate) {
        this.maxEndDate = maxEndDate;
    }

    public String getImageApp() {
        return imageApp;
    }

    public void setImageApp(String imageApp) {
        this.imageApp = imageApp;
    }

    public SearchViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.displayName);
        dest.writeString(this.title);
        dest.writeValue(this.salesPrice);
        dest.writeString(this.cityName);
        dest.writeValue(this.minStartDate);
        dest.writeValue(this.maxEndDate);
        dest.writeString(this.imageApp);
        dest.writeValue(this.isTop);
    }

    protected SearchViewModel(Parcel in) {
        this.url = in.readString();
        this.displayName = in.readString();
        this.title = in.readString();
        this.salesPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cityName = in.readString();
        this.minStartDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maxEndDate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.imageApp = in.readString();
        this.isTop = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<SearchViewModel> CREATOR = new Creator<SearchViewModel>() {
        @Override
        public SearchViewModel createFromParcel(Parcel source) {
            return new SearchViewModel(source);
        }

        @Override
        public SearchViewModel[] newArray(int size) {
            return new SearchViewModel[size];
        }
    };
}
