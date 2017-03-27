package com.tokopedia.ride.bookingride.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.ride.common.ride.domain.model.Product;

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
    private String price;
    private String headerTitle;
    private int maxCapacity;

    public ConfirmBookingViewModel() {
    }

    private ConfirmBookingViewModel(Parcel in) {
        fareId = in.readString();
        productId = in.readString();
        source = in.readParcelable(PlacePassViewModel.class.getClassLoader());
        destination = in.readParcelable(PlacePassViewModel.class.getClassLoader());
        seatCount = in.readInt();
        paymentMethod = in.readString();
        productImage = in.readString();
        price = in.readString();
        headerTitle = in.readString();
        maxCapacity = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fareId);
        dest.writeString(productId);
        dest.writeParcelable(source, flags);
        dest.writeParcelable(destination, flags);
        dest.writeInt(seatCount);
        dest.writeString(paymentMethod);
        dest.writeString(productImage);
        dest.writeString(price);
        dest.writeString(headerTitle);
        dest.writeInt(maxCapacity);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
