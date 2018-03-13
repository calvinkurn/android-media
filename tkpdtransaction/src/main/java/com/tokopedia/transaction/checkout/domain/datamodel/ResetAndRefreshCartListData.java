package com.tokopedia.transaction.checkout.domain.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.ResetCartData;

/**
 * @author anggaprasetiyo on 07/03/18.
 */

public class ResetAndRefreshCartListData implements Parcelable {
    private ResetCartData resetCartData;
    private CartListData cartListData;


    public ResetCartData getResetCartData() {
        return resetCartData;
    }

    public void setResetCartData(ResetCartData resetCartData) {
        this.resetCartData = resetCartData;
    }

    public CartListData getCartListData() {
        return cartListData;
    }

    public void setCartListData(CartListData cartListData) {
        this.cartListData = cartListData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.resetCartData, flags);
        dest.writeParcelable(this.cartListData, flags);
    }

    public ResetAndRefreshCartListData() {
    }

    protected ResetAndRefreshCartListData(Parcel in) {
        this.resetCartData = in.readParcelable(ResetCartData.class.getClassLoader());
        this.cartListData = in.readParcelable(CartListData.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResetAndRefreshCartListData> CREATOR = new Parcelable.Creator<ResetAndRefreshCartListData>() {
        @Override
        public ResetAndRefreshCartListData createFromParcel(Parcel source) {
            return new ResetAndRefreshCartListData(source);
        }

        @Override
        public ResetAndRefreshCartListData[] newArray(int size) {
            return new ResetAndRefreshCartListData[size];
        }
    };
}
