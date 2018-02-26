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

    private ShipmentFeeBannerModel shipmentFeeBannerModel;
    private RecipientAddressModel recipientAddressModel;
    private List<CartSellerItemModel> cartSellerItemModelList;
    private CartPayableDetailModel cartPayableDetailModel;

    public ShipmentFeeBannerModel getShipmentFeeBannerModel() {
        return shipmentFeeBannerModel;
    }

    public void setShipmentFeeBannerModel(ShipmentFeeBannerModel shipmentFeeBannerModel) {
        this.shipmentFeeBannerModel = shipmentFeeBannerModel;
    }

    public RecipientAddressModel getRecipientAddressModel() {
        return recipientAddressModel;
    }

    public void setRecipientAddressModel(RecipientAddressModel recipientAddressModel) {
        this.recipientAddressModel = recipientAddressModel;
    }

    public List<CartSellerItemModel> getCartSellerItemModelList() {
        return cartSellerItemModelList;
    }

    public void setCartSellerItemModelList(List<CartSellerItemModel> cartSellerItemModelList) {
        this.cartSellerItemModelList = cartSellerItemModelList;
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
        dest.writeParcelable(this.shipmentFeeBannerModel, flags);
        dest.writeParcelable(this.recipientAddressModel, flags);
        dest.writeTypedList(this.cartSellerItemModelList);
        dest.writeParcelable(this.cartPayableDetailModel, flags);
    }

    public CartSingleAddressData() {
    }

    protected CartSingleAddressData(Parcel in) {
        this.shipmentFeeBannerModel = in.readParcelable(ShipmentFeeBannerModel.class.getClassLoader());
        this.recipientAddressModel = in.readParcelable(RecipientAddressModel.class.getClassLoader());
        this.cartSellerItemModelList = in.createTypedArrayList(CartSellerItemModel.CREATOR);
        this.cartPayableDetailModel = in.readParcelable(CartPayableDetailModel.class.getClassLoader());
    }

    public static final Creator<CartSingleAddressData> CREATOR = new Creator<CartSingleAddressData>() {
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
