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
    private String productPrice;
    private String baseFare;

    public RideProductViewModel() {
    }

    protected RideProductViewModel(Parcel in) {
        productId = in.readString();
        productImage = in.readString();
        productName = in.readString();
        timeEstimate = in.readString();
        surgePrice = in.readByte() != 0;
        productPrice = in.readString();
        baseFare = in.readString();
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

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productImage);
        dest.writeString(productName);
        dest.writeString(timeEstimate);
        dest.writeByte((byte) (surgePrice ? 1 : 0));
        dest.writeString(productPrice);
        dest.writeString(baseFare);
    }
}
