package com.tokopedia.ride.history.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.history.view.adapter.factory.RideHistoryAdapterTypeFactory;

import java.util.List;

/**
 * Created by alvarisi on 4/11/17.
 */

public class RideHistoryViewModel implements Visitable<RideHistoryAdapterTypeFactory> {
    private String requestTime;
    private String driverCarDisplay;
    private String fare;
    private String status;
    private List<LatLng> latLngs;
    private double startLatitude, startLongitude, endLatitude, endLongitude;

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
}
