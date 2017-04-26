package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/11/17.
 */

public class RideHistoryEntity {
    /*
     * {
     "payment": {
     "currency_code": "IDR",
     "amount": 75000
     },
     "driver": {
     "name": "John",
     "phone_number": "(555)555-5555",
     "picture_url": "https://d1a3f4spazzrp4.cloudfront.net/uberex-sandbox/images/driver.jpg",
     "rating": 4.9
     },
     "guest": {
     "phone_number": "+14150001234",
     "first_name": "Jane",
     "last_name": "Smith",
     "email": "jane.smith@email.com"
     },
     "pickup": {
     "latitude": -6.1901543,
     "longitude": 106.7986657
     },
     "destination": {
     "latitude": -6.1713426,
     "longitude": 106.8237374
     },
     "product_id": "89da0988-cb4f-4c85-b84f-aac2f5115068",
     "request_id": "aa48107a-b8ea-4538-82a4-0be351f6cac4",
     "shared": false,
     "status": "accepted",
     "vehicle": {
     "license_plate": "UBER-PLATE",
     "make": "Toyota",
     "model": "Prius",
     "picture_url": "https://d1a3f4spazzrp4.cloudfront.net/uberex-sandbox/images/prius.jpg"
     }
     }
     */

    @SerializedName("payment")
    @Expose
    PaymentEntity paymentEntity;
    @SerializedName("driver")
    @Expose
    DriverEntity driverEntity;
    @SerializedName("guest")
    @Expose
    GuestEntity guestEntity;
    @SerializedName("pickup")
    @Expose
    LocationLatLngEntity pickupEntity;
    @SerializedName("destination")
    @Expose
    LocationLatLngEntity destination;
    @SerializedName("request_id")
    @Expose
    String requestId;
    @SerializedName("product_id")
    @Expose
    String productId;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("shared")
    @Expose
    boolean shared;
    @SerializedName("vehicle")
    @Expose
    VehicleEntity vehicleEntity;
    @SerializedName("create_time")
    @Expose
    String requestTime;

    public RideHistoryEntity() {
    }

    public DriverEntity getDriverEntity() {
        return driverEntity;
    }

    public GuestEntity getGuestEntity() {
        return guestEntity;
    }

    public LocationLatLngEntity getPickupEntity() {
        return pickupEntity;
    }

    public LocationLatLngEntity getDestination() {
        return destination;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getProductId() {
        return productId;
    }

    public String getStatus() {
        return status;
    }

    public boolean isShared() {
        return shared;
    }

    public VehicleEntity getVehicleEntity() {
        return vehicleEntity;
    }

    public PaymentEntity getPaymentEntity() {
        return paymentEntity;
    }

    public String getRequestTime() {
        return requestTime;
    }
}
