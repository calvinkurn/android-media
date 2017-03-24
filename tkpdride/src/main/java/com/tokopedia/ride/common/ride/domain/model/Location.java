package com.tokopedia.ride.common.ride.domain.model;

/**
 * Created by alvarisi on 3/24/17.
 */

public class Location {
    private double latitude;
    private double longitude;
    private int bearing;

    public Location() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getBearing() {
        return bearing;
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
    }
}
