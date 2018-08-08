package com.tokopedia.ride.common.place.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sachinbansal on 1/22/18.
 */

public class NearbyRoads {

    @SerializedName("snappedPoints")
    @Expose
    private ArrayList<SnappedPoints> snappedPointsArrayList;


    public class SnappedPoints {
        @SerializedName("placeId")
        @Expose
        String placeId;

        @SerializedName("originalIndex")
        @Expose
        String originalIndex;

        @SerializedName("location")
        @Expose
        Location location;


        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getOriginalIndex() {
            return originalIndex;
        }

        public void setOriginalIndex(String originalIndex) {
            this.originalIndex = originalIndex;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public class Location {


        @SerializedName("latitude")
        @Expose
        double latitude;

        @SerializedName("longitude")
        @Expose
        double longitude;

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
    }

    public ArrayList<SnappedPoints> getSnappedPointsArrayList() {
        return snappedPointsArrayList;
    }

    public void setSnappedPointsArrayList(ArrayList<SnappedPoints> snappedPointsArrayList) {
        this.snappedPointsArrayList = snappedPointsArrayList;
    }
}
