package com.tokopedia.ride.bookingride.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 3/23/17.
 */

public class ConfirmBookingViewModel implements Parcelable {
    public static final String EXTRA_CONFIRM_PARAM = "EXTRA_CONFIRM_PARAM";
    private String fareId;
    private String productId;
    private String productDisplayName;
    private String promoCode;
    private String promoDescription;
    private PlacePassViewModel source;
    private PlacePassViewModel destination;
    private int seatCount;
    private String paymentMethod;
    private String productImage;
    private String priceFmt;
    private float price;
    private String headerTitle;
    private int maxCapacity;
    private float surgeMultiplier;
    private String surgeConfirmationHref;
    private String cancellationFee;

    public ConfirmBookingViewModel() {
    }

    protected ConfirmBookingViewModel(Parcel in) {
        fareId = in.readString();
        productId = in.readString();
        productDisplayName = in.readString();
        promoCode = in.readString();
        promoDescription = in.readString();
        source = in.readParcelable(PlacePassViewModel.class.getClassLoader());
        destination = in.readParcelable(PlacePassViewModel.class.getClassLoader());
        seatCount = in.readInt();
        paymentMethod = in.readString();
        productImage = in.readString();
        priceFmt = in.readString();
        price = in.readFloat();
        headerTitle = in.readString();
        maxCapacity = in.readInt();
        surgeMultiplier = in.readFloat();
        surgeConfirmationHref = in.readString();
        cancellationFee = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fareId);
        dest.writeString(productId);
        dest.writeString(productDisplayName);
        dest.writeString(promoCode);
        dest.writeString(promoDescription);
        dest.writeParcelable(source, flags);
        dest.writeParcelable(destination, flags);
        dest.writeInt(seatCount);
        dest.writeString(paymentMethod);
        dest.writeString(productImage);
        dest.writeString(priceFmt);
        dest.writeFloat(price);
        dest.writeString(headerTitle);
        dest.writeInt(maxCapacity);
        dest.writeFloat(surgeMultiplier);
        dest.writeString(surgeConfirmationHref);
        dest.writeString(cancellationFee);
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

    public String getProductDisplayName() {
        return productDisplayName;
    }

    public void setProductDisplayName(String productDisplayName) {
        this.productDisplayName = productDisplayName;
    }

    public float getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public void setSurgeMultiplier(float surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    public String getSurgeConfirmationHref() {
        return surgeConfirmationHref;
    }

    public void setSurgeConfirmationHref(String surgeConfirmationHref) {
        this.surgeConfirmationHref = surgeConfirmationHref;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPromoDescription() {
        return promoDescription;
    }

    public void setPromoDescription(String promoDescription) {
        this.promoDescription = promoDescription;
    }

    public String getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(String cancellationFee) {
        this.cancellationFee = cancellationFee;
    }
}
