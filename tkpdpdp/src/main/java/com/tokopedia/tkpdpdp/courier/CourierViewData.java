package com.tokopedia.tkpdpdp.courier;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.ArrayList;
import java.util.List;

public class CourierViewData implements Visitable<CourierTypeFactory>,Parcelable {

    private String courierId;
    private String logo;
    private List<String> packageName = new ArrayList<>();
    private String courierName;

    @Override
    public int type(CourierTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public CourierViewData() {
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    public String getCourierId() {
        return courierId;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setPackageName(List<String> packageName) {
        if (packageName != null) this.packageName = packageName;
    }

    public List<String> getPackageName() {
        return packageName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierName() {
        return courierName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.courierId);
        dest.writeString(this.logo);
        dest.writeStringList(this.packageName);
        dest.writeString(this.courierName);
    }

    protected CourierViewData(Parcel in) {
        this.courierId = in.readString();
        this.logo = in.readString();
        this.packageName = in.createStringArrayList();
        this.courierName = in.readString();
    }

    public static final Creator<CourierViewData> CREATOR = new Creator<CourierViewData>() {
        @Override
        public CourierViewData createFromParcel(Parcel source) {
            return new CourierViewData(source);
        }

        @Override
        public CourierViewData[] newArray(int size) {
            return new CourierViewData[size];
        }
    };
}
