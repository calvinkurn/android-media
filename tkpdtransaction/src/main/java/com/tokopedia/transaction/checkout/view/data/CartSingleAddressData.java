package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.view.holderitemdata.CartPromo;

import java.util.List;

/**
 * The type Single address product shipment data.
 *
 * @author Aghny A. Putra on 25/01/18
 */

public class CartSingleAddressData implements Parcelable {

    private CartPromo cartPromo;
    private CartPromoSuggestion cartPromoSuggestion;
    private RecipientAddressModel recipientAddressModel;
    private List<CartSellerItemModel> cartSellerItemModelList;
    private CartPayableDetailModel cartPayableDetailModel;
    private ShipmentCartData shipmentCartData;

    public CartPromo getCartPromo() {
        return cartPromo;
    }

    public void setCartPromo(CartPromo cartPromo) {
        this.cartPromo = cartPromo;
    }

    public CartPromoSuggestion getCartPromoSuggestion() {
        return cartPromoSuggestion;
    }

    public void setCartPromoSuggestion(CartPromoSuggestion cartPromoSuggestion) {
        this.cartPromoSuggestion = cartPromoSuggestion;
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
        dest.writeParcelable(this.cartPromo, flags);
        dest.writeParcelable(this.cartPromoSuggestion, flags);
        dest.writeParcelable(this.recipientAddressModel, flags);
        dest.writeTypedList(this.cartSellerItemModelList);
        dest.writeParcelable(this.cartPayableDetailModel, flags);
    }

    public CartSingleAddressData() {
    }

    protected CartSingleAddressData(Parcel in) {
        this.cartPromo = in.readParcelable(CartPromo.class.getClassLoader());
        this.cartPromoSuggestion = in.readParcelable(CartPromoSuggestion.class.getClassLoader());
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
