package com.tokopedia.transaction.checkout.domain.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.pickuppoint.domain.model.Store;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressItemData implements Parcelable {

    private int cartPosition;

    private int addressPosition;

    private String cartId = "";

    private int addressStatus;

    private String productId = "";

    private String productWeight = "";

    private int productRawWeight;

    private String productQty = "";

    private String productNotes = "";

    private String addressId = "";

    private String addressTitle = "";

    private String addressReceiverName = "";

    private String addressProvinceName = "";

    private String addressPostalCode = "";

    private String addressCityName = "";

    private String addressStreet = "";

    private String addressCountryName = "";

    private String recipientPhoneNumber = "";

    private String destinationDistrictId = "";

    private String destinationDistrictName = "";

    private String tokenPickup = "";

    private String unixTime = "";

    private Store store;

    private int maxQuantity;

    private int minQuantity;

    private String errorCheckoutPriceLimit;

    private String errorFieldBetween;

    private String errorFieldMaxChar;

    private String errorFieldRequired;

    private String errorProductAvailableStock;

    private String errorProductAvailableStockDetail;

    private String errorProductMaxQuantity;

    private String errorProductMinQuantity;

    private int maxRemark;

    public MultipleAddressItemData() {
    }

    public void setCartPosition(int cartPosition) {
        this.cartPosition = cartPosition;
    }

    public int getCartPosition() {
        return cartPosition;
    }

    public int getAddressPosition() {
        return addressPosition;
    }

    public void setAddressPosition(int addressPosition) {
        this.addressPosition = addressPosition;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public int getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(int addressStatus) {
        this.addressStatus = addressStatus;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddressReceiverName() {
        return addressReceiverName;
    }

    public void setAddressReceiverName(String addressReceiverName) {
        this.addressReceiverName = addressReceiverName;
    }

    public String getAddressProvinceName() {
        return addressProvinceName;
    }

    public void setAddressProvinceName(String addressProvinceName) {
        this.addressProvinceName = addressProvinceName;
    }

    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    public String getAddressCityName() {
        return addressCityName;
    }

    public void setAddressCityName(String addressCityName) {
        this.addressCityName = addressCityName;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressCountryName() {
        return addressCountryName;
    }

    public void setAddressCountryName(String addressCountryName) {
        this.addressCountryName = addressCountryName;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    public String getDestinationDistrictId() {
        return destinationDistrictId;
    }

    public void setDestinationDistrictId(String destinationDistrictId) {
        this.destinationDistrictId = destinationDistrictId;
    }

    public String getDestinationDistrictName() {
        return destinationDistrictName;
    }

    public void setDestinationDistrictName(String destinationDistrictName) {
        this.destinationDistrictName = destinationDistrictName;
    }

    public String getTokenPickup() {
        return tokenPickup;
    }

    public void setTokenPickup(String tokenPickup) {
        this.tokenPickup = tokenPickup;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getProductRawWeight() {
        return productRawWeight;
    }

    public void setProductRawWeight(int productRawWeight) {
        this.productRawWeight = productRawWeight;
    }

    public String getErrorCheckoutPriceLimit() {
        return errorCheckoutPriceLimit;
    }

    public void setErrorCheckoutPriceLimit(String errorCheckoutPriceLimit) {
        this.errorCheckoutPriceLimit = errorCheckoutPriceLimit;
    }

    public String getErrorFieldBetween() {
        return errorFieldBetween;
    }

    public void setErrorFieldBetween(String errorFieldBetween) {
        this.errorFieldBetween = errorFieldBetween;
    }

    public String getErrorFieldMaxChar() {
        return errorFieldMaxChar;
    }

    public void setErrorFieldMaxChar(String errorFieldMaxChar) {
        this.errorFieldMaxChar = errorFieldMaxChar;
    }

    public String getErrorFieldRequired() {
        return errorFieldRequired;
    }

    public void setErrorFieldRequired(String errorFieldRequired) {
        this.errorFieldRequired = errorFieldRequired;
    }

    public String getErrorProductAvailableStock() {
        return errorProductAvailableStock;
    }

    public void setErrorProductAvailableStock(String errorProductAvailableStock) {
        this.errorProductAvailableStock = errorProductAvailableStock;
    }

    public String getErrorProductAvailableStockDetail() {
        return errorProductAvailableStockDetail;
    }

    public void setErrorProductAvailableStockDetail(String errorProductAvailableStockDetail) {
        this.errorProductAvailableStockDetail = errorProductAvailableStockDetail;
    }

    public String getErrorProductMaxQuantity() {
        return errorProductMaxQuantity;
    }

    public void setErrorProductMaxQuantity(String errorProductMaxQuantity) {
        this.errorProductMaxQuantity = errorProductMaxQuantity;
    }

    public String getErrorProductMinQuantity() {
        return errorProductMinQuantity;
    }

    public void setErrorProductMinQuantity(String errorProductMinQuantity) {
        this.errorProductMinQuantity = errorProductMinQuantity;
    }

    public int getMaxRemark() {
        return maxRemark;
    }

    public void setMaxRemark(int maxRemark) {
        this.maxRemark = maxRemark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cartPosition);
        dest.writeInt(this.addressPosition);
        dest.writeString(this.cartId);
        dest.writeInt(this.addressStatus);
        dest.writeString(this.productId);
        dest.writeString(this.productWeight);
        dest.writeInt(this.productRawWeight);
        dest.writeString(this.productQty);
        dest.writeString(this.productNotes);
        dest.writeString(this.addressId);
        dest.writeString(this.addressTitle);
        dest.writeString(this.addressReceiverName);
        dest.writeString(this.addressProvinceName);
        dest.writeString(this.addressPostalCode);
        dest.writeString(this.addressCityName);
        dest.writeString(this.addressStreet);
        dest.writeString(this.addressCountryName);
        dest.writeString(this.recipientPhoneNumber);
        dest.writeString(this.destinationDistrictId);
        dest.writeString(this.destinationDistrictName);
        dest.writeString(this.tokenPickup);
        dest.writeString(this.unixTime);
        dest.writeParcelable(this.store, flags);
        dest.writeInt(this.maxQuantity);
        dest.writeInt(this.minQuantity);
        dest.writeString(this.errorCheckoutPriceLimit);
        dest.writeString(this.errorFieldBetween);
        dest.writeString(this.errorFieldMaxChar);
        dest.writeString(this.errorFieldRequired);
        dest.writeString(this.errorProductAvailableStock);
        dest.writeString(this.errorProductAvailableStockDetail);
        dest.writeString(this.errorProductMaxQuantity);
        dest.writeString(this.errorProductMinQuantity);
        dest.writeInt(this.maxRemark);
    }

    protected MultipleAddressItemData(Parcel in) {
        this.cartPosition = in.readInt();
        this.addressPosition = in.readInt();
        this.cartId = in.readString();
        this.addressStatus = in.readInt();
        this.productId = in.readString();
        this.productWeight = in.readString();
        this.productRawWeight = in.readInt();
        this.productQty = in.readString();
        this.productNotes = in.readString();
        this.addressId = in.readString();
        this.addressTitle = in.readString();
        this.addressReceiverName = in.readString();
        this.addressProvinceName = in.readString();
        this.addressPostalCode = in.readString();
        this.addressCityName = in.readString();
        this.addressStreet = in.readString();
        this.addressCountryName = in.readString();
        this.recipientPhoneNumber = in.readString();
        this.destinationDistrictId = in.readString();
        this.destinationDistrictName = in.readString();
        this.tokenPickup = in.readString();
        this.unixTime = in.readString();
        this.store = in.readParcelable(Store.class.getClassLoader());
        this.maxQuantity = in.readInt();
        this.minQuantity = in.readInt();
        this.errorCheckoutPriceLimit = in.readString();
        this.errorFieldBetween = in.readString();
        this.errorFieldMaxChar = in.readString();
        this.errorFieldRequired = in.readString();
        this.errorProductAvailableStock = in.readString();
        this.errorProductAvailableStockDetail = in.readString();
        this.errorProductMaxQuantity = in.readString();
        this.errorProductMinQuantity = in.readString();
        this.maxRemark = in.readInt();
    }

    public static final Creator<MultipleAddressItemData> CREATOR = new Creator<MultipleAddressItemData>() {
        @Override
        public MultipleAddressItemData createFromParcel(Parcel source) {
            return new MultipleAddressItemData(source);
        }

        @Override
        public MultipleAddressItemData[] newArray(int size) {
            return new MultipleAddressItemData[size];
        }
    };
}
