package com.tokopedia.ride.bookingride.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 3/23/17.
 */

public class ConfirmBookingViewModel implements Parcelable {
    private String fareId;
    private String productId;

    private PlacePassViewModel source;
    private PlacePassViewModel destination;
    private int seatCount;
    private String paymentMethod;
    private String productImage;
    private String priceFmt;
    private float price;
    private String headerTitle;
    private int maxCapacity;

    public ConfirmBookingViewModel() {
    }

    protected ConfirmBookingViewModel(Parcel in) {
        fareId = in.readString();
        productId = in.readString();
        source = in.readParcelable(PlacePassViewModel.class.getClassLoader());
        destination = in.readParcelable(PlacePassViewModel.class.getClassLoader());
        seatCount = in.readInt();
        paymentMethod = in.readString();
        productImage = in.readString();
        priceFmt = in.readString();
        price = in.readFloat();
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

    public static ConfirmBookingViewModel createInitial() {
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

    public String getPriceFmt() {
        return priceFmt;
    }

    public void setPriceFmt(String priceFmt) {
        this.priceFmt = priceFmt;
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

    public PlacePassViewModel getSource() {
        return source;
    }

    public void setSource(PlacePassViewModel source) {
        this.source = source;
    }

    public PlacePassViewModel getDestination() {
        return destination;
    }

    public void setDestination(PlacePassViewModel destination) {
        this.destination = destination;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fareId);
        parcel.writeString(productId);
        parcel.writeParcelable(source, i);
        parcel.writeParcelable(destination, i);
        parcel.writeInt(seatCount);
        parcel.writeString(paymentMethod);
        parcel.writeString(productImage);
        parcel.writeString(priceFmt);
        parcel.writeFloat(price);
        parcel.writeString(headerTitle);
        parcel.writeInt(maxCapacity);
    }
}
