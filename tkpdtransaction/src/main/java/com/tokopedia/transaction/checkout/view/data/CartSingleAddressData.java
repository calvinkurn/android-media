package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * The type Single address product shipment data.
 *
 * @author Aghny A. Putra on 25/01/18
 */
public class CartSingleAddressData implements Parcelable {

    private ShippingFeeBannerModel shippingFeeBannerModel;
    private ShippingRecipientModel shippingRecipientModel;
    private DropshipperShippingOptionModel dropshipperShippingOptionModel;
    private List<CartItemModel> cartItemModelList;
    private CartPayableDetailModel cartPayableDetailModel;

    public ShippingFeeBannerModel getShippingFeeBannerModel() {
        return shippingFeeBannerModel;
    }

    public void setShippingFeeBannerModel(ShippingFeeBannerModel shippingFeeBannerModel) {
        this.shippingFeeBannerModel = shippingFeeBannerModel;
    }

    public ShippingRecipientModel getShippingRecipientModel() {
        return shippingRecipientModel;
    }

    public void setShippingRecipientModel(ShippingRecipientModel shippingRecipientModel) {
        this.shippingRecipientModel = shippingRecipientModel;
    }

    public DropshipperShippingOptionModel getDropshipperShippingOptionModel() {
        return dropshipperShippingOptionModel;
    }

    public void setDropshipperShippingOptionModel(DropshipperShippingOptionModel dropshipperShippingOptionModel) {
        this.dropshipperShippingOptionModel = dropshipperShippingOptionModel;
    }

    public List<CartItemModel> getCartItemModelList() {
        return cartItemModelList;
    }

    public void setCartItemModelList(List<CartItemModel> cartItemModelList) {
        this.cartItemModelList = cartItemModelList;
    }

    public CartPayableDetailModel getCartPayableDetailModel() {
        return cartPayableDetailModel;
    }

    public void setCartPayableDetailModel(CartPayableDetailModel cartPayableDetailModel) {
        this.cartPayableDetailModel = cartPayableDetailModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.shippingFeeBannerModel, flags);
        dest.writeParcelable(this.shippingRecipientModel, flags);
        dest.writeParcelable(this.dropshipperShippingOptionModel, flags);
        dest.writeTypedList(this.cartItemModelList);
        dest.writeParcelable(this.cartPayableDetailModel, flags);
    }

    public CartSingleAddressData() {
    }

    protected CartSingleAddressData(Parcel in) {
        this.shippingFeeBannerModel = in.readParcelable(ShippingFeeBannerModel.class.getClassLoader());
        this.shippingRecipientModel = in.readParcelable(ShippingRecipientModel.class.getClassLoader());
        this.dropshipperShippingOptionModel = in.readParcelable(DropshipperShippingOptionModel.class.getClassLoader());
        this.cartItemModelList = in.createTypedArrayList(CartItemModel.CREATOR);
        this.cartPayableDetailModel = in.readParcelable(CartPayableDetailModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<CartSingleAddressData> CREATOR = new Parcelable.Creator<CartSingleAddressData>() {
        @Override
        public CartSingleAddressData createFromParcel(Parcel source) {
            return new CartSingleAddressData(source);
        }

        @Override
        public CartSingleAddressData[] newArray(int size) {
            return new CartSingleAddressData[size];
        }
    };
}
