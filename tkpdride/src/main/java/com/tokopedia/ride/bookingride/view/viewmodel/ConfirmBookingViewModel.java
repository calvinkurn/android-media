package com.tokopedia.ride.bookingride.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.ride.common.ride.domain.model.Product;

/**
 * Created by alvarisi on 3/23/17.
 */

public class ConfirmBookingViewModel implements Parcelable{
    private String fareId;
    private String productId;

    private String startAddress;
    private double startLatitude;
    private double startLongitude;
    private String endAddress;
    private double endLatitude;
    private double endLongitude;
    private int seatCount;
    private String paymentMethod;
    private String productImage;
    private String price;
    private String headerTitle;
    private int maxCapacity;

    public ConfirmBookingViewModel() {
    }

    protected ConfirmBookingViewModel(Parcel in) {
        fareId = in.readString();
        productId = in.readString();
        startAddress = in.readString();
        startLatitude = in.readDouble();
        startLongitude = in.readDouble();
        endAddress = in.readString();
        endLatitude = in.readDouble();
        endLongitude = in.readDouble();
        seatCount = in.readInt();
        paymentMethod = in.readString();
        productImage = in.readString();
        price = in.readString();
        headerTitle = in.readString();
        maxCapacity = in.readInt();
    }

    public static final Creator<ConfirmBookingViewModel> CREATOR = new Creator<ConfirmBookingViewModel>() {
        @Override
        public ConfirmBookingViewModel createFromParcel(Parcel in) {
            return new ConfirmBookingViewModel(in);
        }

        @Override
        public ConfirmBookingViewModel[] newArray(int size) {
            return new ConfirmBookingViewModel[size];
        }
    };

    public static ConfirmBookingViewModel createInitial(){
        ConfirmBookingViewModel confirmBookingViewModel = new ConfirmBookingViewModel();
        confirmBookingViewModel.setSeatCount(1);
        confirmBookingViewModel.setPaymentMethod(String.valueOf(1));
        return confirmBookingViewModel;
    }

    public String getFareId() {
        return fareId;
    }

    public void setFareId(String fareId) {
        this.fareId = fareId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fareId);
        parcel.writeString(productId);
        parcel.writeString(startAddress);
        parcel.writeDouble(startLatitude);
        parcel.writeDouble(startLongitude);
        parcel.writeString(endAddress);
        parcel.writeDouble(endLatitude);
        parcel.writeDouble(endLongitude);
        parcel.writeInt(seatCount);
        parcel.writeString(paymentMethod);
        parcel.writeString(productImage);
        parcel.writeString(price);
        parcel.writeString(headerTitle);
        parcel.writeInt(maxCapacity);
    }
}
