package com.tokopedia.ride.history.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.common.ride.domain.model.Rating;
import com.tokopedia.ride.history.view.adapter.factory.RideHistoryAdapterTypeFactory;

import java.util.List;

/**
 * Created by alvarisi on 4/11/17.
 */

public class RideHistoryViewModel implements Visitable<RideHistoryAdapterTypeFactory>, Parcelable {
    private static final String IND_CURRENCY = "IDR";
    private static final String IND_LOCAL_CURRENCY = "Rp";

    private String requestId;
    private String requestTime;
    private String driverCarDisplay;
    private String status;
    private String driverName;
    private String driverPictureUrl;
    private List<LatLng> latLngs;
    private double startLatitude, startLongitude, endLatitude, endLongitude;
    private String startAddress;
    private String endAddress;
    private String mapImage;
    private String displayStatus;
    private String licensePlateNumber;
    private String totalFare;
    private float discount;
    private float cashback;
    private Rating rating;
    private String helpUrl;
    private String cashbackDisplayFormat;
    private String discountDisplayFormat;
    private String pendingAmountDisplayFormat;
    private String tokoCashCharged;
    private int pendingAmount;
    private String paymentMethod;

    public RideHistoryViewModel() {
    }

    protected RideHistoryViewModel(Parcel in) {
        requestId = in.readString();
        requestTime = in.readString();
        driverCarDisplay = in.readString();
        status = in.readString();
        driverName = in.readString();
        driverPictureUrl = in.readString();
        latLngs = in.createTypedArrayList(LatLng.CREATOR);
        startLatitude = in.readDouble();
        startLongitude = in.readDouble();
        endLatitude = in.readDouble();
        endLongitude = in.readDouble();
        startAddress = in.readString();
        endAddress = in.readString();
        mapImage = in.readString();
        displayStatus = in.readString();
        licensePlateNumber = in.readString();
        totalFare = in.readString();
        discount = in.readFloat();
        cashback = in.readFloat();
        rating = in.readParcelable(Rating.class.getClassLoader());
        helpUrl = in.readString();
        cashbackDisplayFormat = in.readString();
        discountDisplayFormat = in.readString();
        pendingAmountDisplayFormat = in.readString();
        tokoCashCharged = in.readString();
        pendingAmount = in.readInt();
        paymentMethod = in.readString();
    }

    public static final Creator<RideHistoryViewModel> CREATOR = new Creator<RideHistoryViewModel>() {
        @Override
        public RideHistoryViewModel createFromParcel(Parcel in) {
            return new RideHistoryViewModel(in);
        }

        @Override
        public RideHistoryViewModel[] newArray(int size) {
            return new RideHistoryViewModel[size];
        }
    };

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

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getCashback() {
        return cashback;
    }

    public void setCashback(float cashback) {
        this.cashback = cashback;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public String getCashbackDisplayFormat() {
        return cashbackDisplayFormat;
    }

    public void setCashbackDisplayFormat(String cashbackDisplayFormat) {
        this.cashbackDisplayFormat = cashbackDisplayFormat;
    }

    public String getDiscountDisplayFormat() {
        return discountDisplayFormat;
    }

    public void setDiscountDisplayFormat(String discountDisplayFormat) {
        this.discountDisplayFormat = discountDisplayFormat;
    }

    public String getPendingAmountDisplayFormat() {
        return pendingAmountDisplayFormat;
    }

    public void setPendingAmountDisplayFormat(String pendingAmount) {
        this.pendingAmountDisplayFormat = pendingAmount;
    }

    public String getTokoCashCharged() {
        return tokoCashCharged;
    }

    public void setTokoCashCharged(String tokoCashCharged) {
        this.tokoCashCharged = tokoCashCharged;
    }

    public int getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(int pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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
        parcel.writeString(requestTime);
        parcel.writeString(driverCarDisplay);
        parcel.writeString(status);
        parcel.writeString(driverName);
        parcel.writeString(driverPictureUrl);
        parcel.writeTypedList(latLngs);
        parcel.writeDouble(startLatitude);
        parcel.writeDouble(startLongitude);
        parcel.writeDouble(endLatitude);
        parcel.writeDouble(endLongitude);
        parcel.writeString(startAddress);
        parcel.writeString(endAddress);
        parcel.writeString(mapImage);
        parcel.writeString(displayStatus);
        parcel.writeString(licensePlateNumber);
        parcel.writeString(totalFare);
        parcel.writeFloat(discount);
        parcel.writeFloat(cashback);
        parcel.writeParcelable(rating, i);
        parcel.writeString(helpUrl);
        parcel.writeString(cashbackDisplayFormat);
        parcel.writeString(discountDisplayFormat);
        parcel.writeString(pendingAmountDisplayFormat);
        parcel.writeString(tokoCashCharged);
        parcel.writeInt(pendingAmount);
        parcel.writeString(paymentMethod);
    }
}