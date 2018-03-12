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

    private boolean isError;
    private String errorMessage;

    private String shopId;
    private String shopName;
    private List<CartItemModel> cartItemModels;

    private double totalItemPrice;
    private double shippingFee;
    private double insuranceFee;
    private double totalPrice;
    private double totalWeight;
    private int weightUnit;
    private int totalQuantity;

    private int isPreOrder;
    private int isFinsurance;
    private int isFcancelPartial;

    private ShipmentCartData shipmentCartData;
    private ShipmentDetailData selectedShipmentDetailData;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

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

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
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

    public int getIsPreOrder() {
        return isPreOrder;
    }

    public void setIsPreOrder(int isPreOrder) {
        this.isPreOrder = isPreOrder;
    }

    public int getIsFinsurance() {
        return isFinsurance;
    }

    public void setIsFinsurance(int isFinsurance) {
        this.isFinsurance = isFinsurance;
    }

    public int getIsFcancelPartial() {
        return isFcancelPartial;
    }

    public void setIsFcancelPartial(int isFcancelPartial) {
        this.isFcancelPartial = isFcancelPartial;
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

    public CartSellerItemModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
        dest.writeString(this.shopId);
        dest.writeString(this.shopName);
        dest.writeTypedList(this.cartItemModels);
        dest.writeDouble(this.totalItemPrice);
        dest.writeDouble(this.shippingFee);
        dest.writeDouble(this.insuranceFee);
        dest.writeDouble(this.totalPrice);
        dest.writeDouble(this.totalWeight);
        dest.writeInt(this.weightUnit);
        dest.writeInt(this.totalQuantity);
        dest.writeInt(this.isPreOrder);
        dest.writeInt(this.isFinsurance);
        dest.writeInt(this.isFcancelPartial);
        dest.writeParcelable(this.shipmentCartData, flags);
        dest.writeParcelable(this.selectedShipmentDetailData, flags);
    }

    protected CartSellerItemModel(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.shopId = in.readString();
        this.shopName = in.readString();
        this.cartItemModels = in.createTypedArrayList(CartItemModel.CREATOR);
        this.totalItemPrice = in.readDouble();
        this.shippingFee = in.readDouble();
        this.insuranceFee = in.readDouble();
        this.totalPrice = in.readDouble();
        this.totalWeight = in.readDouble();
        this.weightUnit = in.readInt();
        this.totalQuantity = in.readInt();
        this.isPreOrder = in.readInt();
        this.isFinsurance = in.readInt();
        this.isFcancelPartial = in.readInt();
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