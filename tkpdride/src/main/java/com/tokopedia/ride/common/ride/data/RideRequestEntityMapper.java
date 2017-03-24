package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.DriverEntity;
import com.tokopedia.ride.common.ride.data.entity.LocationEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestEntity;
import com.tokopedia.ride.common.ride.data.entity.VehicleEntity;
import com.tokopedia.ride.common.ride.domain.model.Driver;
import com.tokopedia.ride.common.ride.domain.model.Location;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.Vehicle;

/**
 * Created by alvarisi on 3/24/17.
 */

public class RideRequestEntityMapper {
    public RideRequestEntityMapper() {
    }

    public RideRequest transform(RideRequestEntity entity) {
        RideRequest rideRequest = null;
        if (entity != null) {
            rideRequest = new RideRequest();
            rideRequest.setProductId(entity.getProductId());
            rideRequest.setEta(entity.getEta());
            rideRequest.setSurgeMultiplier(entity.getSurgeMultiplier());
            rideRequest.setDriver(transform(entity.getDriver()));
            rideRequest.setLocation(transform(entity.getLocation()));
            rideRequest.setStatus(entity.getStatus());
            rideRequest.setVehicle(transform(entity.getVehicle()));
            rideRequest.setRequestId(entity.getRequestId());
        }
        return rideRequest;
    }

    private Vehicle transform(VehicleEntity entity) {
        Vehicle vehicle = null;
        if (entity != null) {
            vehicle = new Vehicle();
            vehicle.setPictureUrl(entity.getPictureUrl());
            vehicle.setLicensePlate(entity.getLicensePlate());
            vehicle.setMake(entity.getMake());
            vehicle.setVehicleModel(entity.getVehicleModel());
        }
        return vehicle;
    }

    private Location transform(LocationEntity entity) {
        Location location = null;
        if (entity != null) {
            location = new Location();
            location.setBearing(entity.getBearing());
            location.setLongitude(entity.getLongitude());
            location.setLatitude(entity.getLatitude());
        }
        return location;
    }

    private Driver transform(DriverEntity entity) {
        Driver driver = null;
        if (entity != null) {
            driver = new Driver();
            driver.setName(entity.getName());
            driver.setPhoneNumber(entity.getName());
            driver.setPictureUrl(entity.getPictureUrl());
            driver.setRating(entity.getRating());
            driver.setSmsNumber(entity.getSmsNumber());
        }
        return driver;
    }
}
