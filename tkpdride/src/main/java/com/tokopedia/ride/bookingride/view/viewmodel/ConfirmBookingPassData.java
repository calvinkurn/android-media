package com.tokopedia.ride.bookingride.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 7/17/17.
 */

public class ConfirmBookingPassData implements Parcelable {
    private String productId;
    private String productDisplayName;
    private PlacePassViewModel source;
    private PlacePassViewModel destination;
    private int seatCount;
    private String productImage;
    private String priceFmt;
    private String headerTitle;
    private int maxCapacity;
    private String cancellationFee;

    protected ConfirmBookingPassData(Parcel in) {
        productId = in.readString();
        productDisplayName = in.readString();
        source = in.readParcelable(PlacePassViewModel.class.getClassLoader());
        destination = in.readParcelable(PlacePassViewModel.class.getClassLoader());
        seatCount = in.readInt();
        productImage = in.readString();
        priceFmt = in.readString();
        headerTitle = in.readString();
        maxCapacity = in.readInt();
        cancellationFee = in.readString();
    }

    public static final Creator<ConfirmBookingPassData> CREATOR = new Creator<ConfirmBookingPassData>() {
        @Override
        public ConfirmBookingPassData createFromParcel(Parcel in) {
            return new ConfirmBookingPassData(in);
        }

        @Override
        public ConfirmBookingPassData[] newArray(int size) {
            return new ConfirmBookingPassData[size];
        }
    };

    public ConfirmBookingPassData() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductDisplayName() {
        return productDisplayName;
    }

    public void setProductDisplayName(String productDisplayName) {
        this.productDisplayName = productDisplayName;
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

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
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

    public String getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(String cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(productDisplayName);
        parcel.writeParcelable(source, i);
        parcel.writeParcelable(destination, i);
        parcel.writeInt(seatCount);
        parcel.writeString(productImage);
        parcel.writeString(priceFmt);
        parcel.writeString(headerTitle);
        parcel.writeInt(maxCapacity);
        parcel.writeString(cancellationFee);
    }
}
