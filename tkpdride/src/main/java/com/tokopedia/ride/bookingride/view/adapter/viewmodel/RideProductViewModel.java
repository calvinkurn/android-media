package com.tokopedia.ride.bookingride.view.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.bookingride.view.adapter.factory.RideProductTypeFactory;

/**
 * Created by alvarisi on 3/16/17.
 */

public class RideProductViewModel implements Visitable<RideProductTypeFactory>, Parcelable {
    private String productId;
    private String productImage;
    private String productName;
    private String timeEstimate;
    private boolean surgePrice;
    private String productPriceFmt;
    private String baseFare;
    private int capacity;
    private boolean enabled;
    private String cancellationFee;
    private boolean isDestinationSelected;

    public RideProductViewModel() {
    }

    protected RideProductViewModel(Parcel in) {
        productId = in.readString();
        productImage = in.readString();
        productName = in.readString();
        timeEstimate = in.readString();
        surgePrice = in.readByte() != 0;
        productPriceFmt = in.readString();
        baseFare = in.readString();
        capacity = in.readInt();
        enabled = in.readByte() != 0;
        cancellationFee = in.readString();
        isDestinationSelected = in.readByte() != 0;
    }

    public static final Creator<RideProductViewModel> CREATOR = new Creator<RideProductViewModel>() {
        @Override
        public RideProductViewModel createFromParcel(Parcel in) {
            return new RideProductViewModel(in);
        }

        @Override
        public RideProductViewModel[] newArray(int size) {
            return new RideProductViewModel[size];
        }
    };

    @Override
    public int type(RideProductTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(String timeEstimate) {
        this.timeEstimate = timeEstimate;
    }

    public boolean isSurgePrice() {
        return surgePrice;
    }

    public void setSurgePrice(boolean surgePrice) {
        this.surgePrice = surgePrice;
    }

    public String getProductPriceFmt() {
        return productPriceFmt;
    }

    public void setProductPriceFmt(String productPriceFmt) {
        this.productPriceFmt = productPriceFmt;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(String cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public boolean isDestinationSelected() {
        return isDestinationSelected;
    }

    public void setDestinationSelected(boolean destinationSelected) {
        isDestinationSelected = destinationSelected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RideProductViewModel that = (RideProductViewModel) o;

        return productId != null ? productId.equals(that.productId) : that.productId == null;
    }

    @Override
    public int hashCode() {
        return productId != null ? productId.hashCode() : 0;
    }

    public static RideProductViewModel copy(RideProductViewModel orig) {
        Parcel p = Parcel.obtain();
        orig.writeToParcel(p, 0);
        p.setDataPosition(0);
        RideProductViewModel copy = null;
        try {
            copy = orig.getClass().getDeclaredConstructor(new Class[]{Parcel.class}).newInstance(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return copy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(productImage);
        parcel.writeString(productName);
        parcel.writeString(timeEstimate);
        parcel.writeByte((byte) (surgePrice ? 1 : 0));
        parcel.writeString(productPriceFmt);
        parcel.writeString(baseFare);
        parcel.writeInt(capacity);
        parcel.writeByte((byte) (enabled ? 1 : 0));
        parcel.writeString(cancellationFee);
        parcel.writeByte((byte) (isDestinationSelected ? 1 : 0));
    }
}
