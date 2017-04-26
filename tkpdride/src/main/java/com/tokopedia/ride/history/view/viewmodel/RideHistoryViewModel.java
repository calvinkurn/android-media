package com.tokopedia.ride.history.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.history.view.adapter.factory.RideHistoryAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 4/11/17.
 */

public class RideHistoryViewModel implements Visitable<RideHistoryAdapterTypeFactory>, Parcelable {
    private String requestId;
    private String requestTime;
    private String driverCarDisplay;
    private String fare;
    private String status;
    private String driverName;
    private String driverPictureUrl;
    private List<LatLng> latLngs;
    private double startLatitude, startLongitude, endLatitude, endLongitude;
    private String mapImage;

    public RideHistoryViewModel() {
    }

    @Override
    public int type(RideHistoryAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getDriverCarDisplay() {
        return driverCarDisplay;
    }

    public void setDriverCarDisplay(String driverCarDisplay) {
        this.driverCarDisplay = driverCarDisplay;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(List<LatLng> latLngs) {
        this.latLngs = latLngs;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPictureUrl() {
        return driverPictureUrl;
    }

    public void setDriverPictureUrl(String driverPictureUrl) {
        this.driverPictureUrl = driverPictureUrl;
    }

    public String getMapImage() {
        return mapImage;
    }

    public void setMapImage(String mapImage) {
        this.mapImage = mapImage;
    }

    protected RideHistoryViewModel(Parcel in) {
        requestId = in.readString();
        requestTime = in.readString();
        driverCarDisplay = in.readString();
        fare = in.readString();
        status = in.readString();
        driverName = in.readString();
        driverPictureUrl = in.readString();
        if (in.readByte() == 0x01) {
            latLngs = new ArrayList<LatLng>();
            in.readList(latLngs, LatLng.class.getClassLoader());
        } else {
            latLngs = null;
        }
        mapImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(requestId);
        dest.writeString(requestTime);
        dest.writeString(driverCarDisplay);
        dest.writeString(fare);
        dest.writeString(status);
        dest.writeString(driverName);
        dest.writeString(driverPictureUrl);
        if (latLngs == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(latLngs);
        }
        dest.writeString(mapImage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RideHistoryViewModel> CREATOR = new Parcelable.Creator<RideHistoryViewModel>() {
        @Override
        public RideHistoryViewModel createFromParcel(Parcel in) {
            return new RideHistoryViewModel(in);
        }

        @Override
        public RideHistoryViewModel[] newArray(int size) {
            return new RideHistoryViewModel[size];
        }
    };
}