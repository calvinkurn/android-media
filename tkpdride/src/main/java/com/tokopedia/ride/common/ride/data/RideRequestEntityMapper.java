package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.configuration.PaymentMode;
import com.tokopedia.ride.common.ride.data.entity.DriverEntity;
import com.tokopedia.ride.common.ride.data.entity.LocationEntity;
import com.tokopedia.ride.common.ride.data.entity.LocationLatLngEntity;
import com.tokopedia.ride.common.ride.data.entity.PaymentEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestAddressEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestEntity;
import com.tokopedia.ride.common.ride.data.entity.VehicleEntity;
import com.tokopedia.ride.common.ride.domain.model.Driver;
import com.tokopedia.ride.common.ride.domain.model.Location;
import com.tokopedia.ride.common.ride.domain.model.LocationLatLng;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.RideRequestAddress;
import com.tokopedia.ride.common.ride.domain.model.Vehicle;
import com.tokopedia.ride.history.domain.model.Payment;

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
            rideRequest.setSurgeMultiplier(entity.getSurgeMultiplier());
            rideRequest.setDriver(transform(entity.getDriver()));
            rideRequest.setLocation(transform(entity.getLocation()));
            rideRequest.setStatus(entity.getStatus());
            rideRequest.setVehicle(transform(entity.getVehicle()));
            rideRequest.setRequestId(entity.getRequestId());
            rideRequest.setPickup(transform(entity.getPickupd()));
            rideRequest.setDestination(transform(entity.getDestination()));
            rideRequest.setShared(entity.isShared());
            rideRequest.setPayment(transform(entity.getPayment()));
            rideRequest.setPollWait(entity.getPollWait());
            rideRequest.setCancelChargeTimestamp(entity.getCancelChargeTimestamp());
        }
        return rideRequest;
    }

    private RideRequestAddress transform(RideRequestAddressEntity entity) {
        RideRequestAddress address = null;
        if (entity != null) {
            address = new RideRequestAddress();
            address.setStartAddress(entity.getStartAddress());
            address.setStartAddressName(entity.getStartAddressName());
            address.setEndAddress(entity.getEndAddress());
            address.setEndAddressName(entity.getEndAddressName());
        }
        return address;
    }

    public LocationLatLng transform(LocationLatLngEntity entity) {
        LocationLatLng location = null;
        if (entity != null) {
            location = new LocationLatLng();
            location.setEta(entity.getEta());
            location.setLongitude(entity.getLongitude());
            location.setLatitude(entity.getLatitude());
            location.setAddress(entity.getAddress());
            location.setAddressName(entity.getAddressName());
        }
        return location;
    }

    public Vehicle transform(VehicleEntity entity) {
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

    public Location transform(LocationEntity entity) {
        Location location = null;
        if (entity != null && entity.getLatitude() != 0 && entity.getLongitude() != 0) {
            location = new Location();
            location.setBearing(entity.getBearing());
            location.setLongitude(entity.getLongitude());
            location.setLatitude(entity.getLatitude());
        }
        return location;
    }

    public Driver transform(DriverEntity entity) {
        Driver driver = null;
        if (entity != null) {
            driver = new Driver();
            driver.setName(entity.getName());
            driver.setPhoneNumber(entity.getPhoneNumber());
            driver.setPictureUrl(entity.getPictureUrl());
            driver.setRating(entity.getRating());
            driver.setSmsNumber(entity.getSmsNumber());
        }
        return driver;
    }

    public Payment transform(PaymentEntity entity) {
        Payment payment = null;
        if (entity != null) {
            payment = new Payment();
            payment.setCurrency(transformCurrency(entity.getCurrencyCode()));
            payment.setTotalAmount(entity.getTotalAmount());
            payment.setReceiptReady(entity.isReceiptReady());
            payment.setPaymentMethod(transformPaymentMethod(entity.getPaymentMethod()));
        }
        return payment;
    }

    public String transformCurrency(String currencyCode) {
        if (currencyCode != null && currencyCode.equalsIgnoreCase("IDR")) {
            return "Rp";
        }

        return currencyCode;
    }

    private String transformPaymentMethod(String paymentMethod) {
        if (paymentMethod != null && paymentMethod.equalsIgnoreCase(PaymentMode.CC)) {
            return PaymentMode.CC_DISPLAY_NAME;
        } else if (paymentMethod != null && paymentMethod.equalsIgnoreCase(PaymentMode.WALLET)) {
            return PaymentMode.WALLET_DISPLAY_NAME;
        }

        return PaymentMode.WALLET_DISPLAY_NAME;
    }
}
