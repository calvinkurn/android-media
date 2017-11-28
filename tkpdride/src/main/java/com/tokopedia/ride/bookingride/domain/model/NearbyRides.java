package com.tokopedia.ride.bookingride.domain.model;

import com.tokopedia.ride.common.ride.domain.model.Location;

import java.util.ArrayList;

/**
 * Created by Vishal on 10/6/17.
 */

public class NearbyRides {
    private ArrayList<Location> cars;
    private ArrayList<Location> bikes;

    public ArrayList<Location> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Location> cars) {
        this.cars = cars;
    }

    public ArrayList<Location> getBikes() {
        return bikes;
    }

    public void setBikes(ArrayList<Location> bikes) {
        this.bikes = bikes;
    }
}
