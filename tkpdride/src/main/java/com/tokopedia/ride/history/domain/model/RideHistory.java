package com.tokopedia.ride.history.domain.model;

import com.tokopedia.ride.common.ride.domain.model.Driver;
import com.tokopedia.ride.common.ride.domain.model.LocationLatLng;
import com.tokopedia.ride.common.ride.domain.model.Vehicle;

/**
 * Created by alvarisi on 4/11/17.
 */

public class RideHistory {
    private String requestId;
    private String productId;
    private String status;
    private Vehicle vehicle;
    private Driver driver;
    private LocationLatLng pickup;
    private LocationLatLng destination;
    private boolean shared;
    private Payment payment;
    private String requestTime;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public LocationLatLng getPickup() {
        return pickup;
    }

    public void setPickup(LocationLatLng pickup) {
        this.pickup = pickup;
    }

    public LocationLatLng getDestination() {
        return destination;
    }

    public void setDestination(LocationLatLng destination) {
        this.destination = destination;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}
