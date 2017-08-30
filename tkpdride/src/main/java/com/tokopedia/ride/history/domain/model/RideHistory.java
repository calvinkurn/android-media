package com.tokopedia.ride.history.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.ride.common.ride.domain.model.Driver;
import com.tokopedia.ride.common.ride.domain.model.LocationLatLng;
import com.tokopedia.ride.common.ride.domain.model.Rating;
import com.tokopedia.ride.common.ride.domain.model.Vehicle;

/**
 * Created by alvarisi on 4/11/17.
 */

public class RideHistory implements Parcelable {
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
    private float cashbackAmount;
    private float discountAmount;
    private Rating rating;
    private String helpUrl;


    protected RideHistory(Parcel in) {
        requestId = in.readString();
        productId = in.readString();
        status = in.readString();
        vehicle = in.readParcelable(Vehicle.class.getClassLoader());
        driver = in.readParcelable(Driver.class.getClassLoader());
        pickup = in.readParcelable(LocationLatLng.class.getClassLoader());
        destination = in.readParcelable(LocationLatLng.class.getClassLoader());
        shared = in.readByte() != 0;
        payment = in.readParcelable(Payment.class.getClassLoader());
        requestTime = in.readString();
        cashbackAmount = in.readFloat();
        discountAmount = in.readFloat();
        rating = in.readParcelable(Rating.class.getClassLoader());
        helpUrl = in.readString();
    }

    public static final Creator<RideHistory> CREATOR = new Creator<RideHistory>() {
        @Override
        public RideHistory createFromParcel(Parcel in) {
            return new RideHistory(in);
        }

        @Override
        public RideHistory[] newArray(int size) {
            return new RideHistory[size];
        }
    };

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

    public float getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(float cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(float discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public RideHistory() {

    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(requestId);
        parcel.writeString(productId);
        parcel.writeString(status);
        parcel.writeParcelable(vehicle, i);
        parcel.writeParcelable(driver, i);
        parcel.writeParcelable(pickup, i);
        parcel.writeParcelable(destination, i);
        parcel.writeByte((byte) (shared ? 1 : 0));
        parcel.writeParcelable(payment, i);
        parcel.writeString(requestTime);
        parcel.writeFloat(cashbackAmount);
        parcel.writeFloat(discountAmount);
        parcel.writeParcelable(rating, i);
        parcel.writeString(helpUrl);
    }
}
