package com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentCartData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;

import java.util.List;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class CartSellerItemModel implements Parcelable {

    private String shopId;
    private String shopName;
    private List<CartItemModel> cartItemModels;

    private double totalItemPrice;
    private double totalPrice;
    private double totalWeight;
    private int weightUnit;
    private int totalQuantity;

    private ShipmentCartData shipmentCartData;
    private ShipmentDetailData selectedShipmentDetailData;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<CartItemModel> getCartItemModels() {
        return cartItemModels;
    }

    public void setCartItemModels(List<CartItemModel> cartItemModels) {
        this.cartItemModels = cartItemModels;
    }

    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public int getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(int weightUnit) {
        this.weightUnit = weightUnit;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public ShipmentCartData getShipmentCartData() {
        return shipmentCartData;
    }

    public void setShipmentCartData(ShipmentCartData shipmentCartData) {
        this.shipmentCartData = shipmentCartData;
    }

    public ShipmentDetailData getSelectedShipmentDetailData() {
        return selectedShipmentDetailData;
    }

    public void setSelectedShipmentDetailData(ShipmentDetailData selectedShipmentDetailData) {
        this.selectedShipmentDetailData = selectedShipmentDetailData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopId);
        dest.writeString(this.shopName);
        dest.writeTypedList(this.cartItemModels);
        dest.writeDouble(this.totalItemPrice);
        dest.writeDouble(this.totalPrice);
        dest.writeDouble(this.totalWeight);
        dest.writeInt(this.weightUnit);
        dest.writeInt(this.totalQuantity);
        dest.writeParcelable(this.shipmentCartData, flags);
        dest.writeParcelable(this.selectedShipmentDetailData, flags);
    }

    public CartSellerItemModel() {
    }

    protected CartSellerItemModel(Parcel in) {
        this.shopId = in.readString();
        this.shopName = in.readString();
        this.cartItemModels = in.createTypedArrayList(CartItemModel.CREATOR);
        this.totalItemPrice = in.readDouble();
        this.totalPrice = in.readDouble();
        this.totalWeight = in.readDouble();
        this.weightUnit = in.readInt();
        this.totalQuantity = in.readInt();
        this.shipmentCartData = in.readParcelable(ShipmentCartData.class.getClassLoader());
        this.selectedShipmentDetailData = in.readParcelable(ShipmentDetailData.class.getClassLoader());
    }

    public static final Creator<CartSellerItemModel> CREATOR = new Creator<CartSellerItemModel>() {
        @Override
        public CartSellerItemModel createFromParcel(Parcel source) {
            return new CartSellerItemModel(source);
        }

        @Override
        public CartSellerItemModel[] newArray(int size) {
            return new CartSellerItemModel[size];
        }
    };

}
