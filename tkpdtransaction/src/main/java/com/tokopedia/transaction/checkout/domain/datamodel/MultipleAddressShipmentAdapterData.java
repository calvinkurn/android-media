package com.tokopedia.transaction.checkout.domain.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates.ShipmentCartData;
import com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressShipmentAdapterData implements Parcelable {

    private boolean isError;
    private String errorMessage;
    private boolean isWarning;
    private String warningMessage;

    private int invoicePosition;

    private int shopId;

    private String senderName;

    private String productImageUrl;

    private String productPrice;

    private String productName;

    private MultipleAddressItemData itemData;

    private String courier;

    private String subTotalAmount;

    private String destinationDistrictId;

    private String destinationDistrictName;

    private String tokenPickup;

    private String unixTime;

    private Store store;

    private ShipmentCartData shipmentCartData;

    private ShipmentDetailData selectedShipmentDetailData;

    private long subTotal;

    private long productPriceNumber;

    private String freeReturnLogo;

    private boolean productIsPreorder;

    private boolean productIsFreeReturns;

    private boolean productReturnable;

    private boolean productFinsurance;

    private boolean productFcancelPartial;

    public int getInvoicePosition() {
        return invoicePosition;
    }

    public void setInvoicePosition(int invoicePosition) {
        this.invoicePosition = invoicePosition;
    }


    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public MultipleAddressItemData getItemData() {
        return itemData;
    }

    public void setItemData(MultipleAddressItemData itemData) {
        this.itemData = itemData;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(String subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
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

    public ShipmentCartData getShipmentCartData() {
        return shipmentCartData;
    }

    public void setShipmentCartData(ShipmentCartData shipmentCartData) {
        this.shipmentCartData = shipmentCartData;
    }

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

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public long getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(long subTotal) {
        this.subTotal = subTotal;
    }

    public long getProductPriceNumber() {
        return productPriceNumber;
    }

    public void setProductPriceNumber(long productPriceNumber) {
        this.productPriceNumber = productPriceNumber;
    }

    public String getFreeReturnLogo() {
        return freeReturnLogo;
    }

    public void setFreeReturnLogo(String freeReturnLogo) {
        this.freeReturnLogo = freeReturnLogo;
    }

    public boolean isProductIsPreorder() {
        return productIsPreorder;
    }

    public void setProductIsPreorder(boolean productIsPreorder) {
        this.productIsPreorder = productIsPreorder;
    }

    public boolean isProductIsFreeReturns() {
        return productIsFreeReturns;
    }

    public void setProductIsFreeReturns(boolean productIsFreeReturns) {
        this.productIsFreeReturns = productIsFreeReturns;
    }

    public boolean isProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(boolean productReturnable) {
        this.productReturnable = productReturnable;
    }

    public boolean isProductFinsurance() {
        return productFinsurance;
    }

    public void setProductFinsurance(boolean productFinsurance) {
        this.productFinsurance = productFinsurance;
    }

    public boolean isProductFcancelPartial() {
        return productFcancelPartial;
    }

    public void setProductFcancelPartial(boolean productFcancelPartial) {
        this.productFcancelPartial = productFcancelPartial;
    }

    public ShipmentDetailData getSelectedShipmentDetailData() {
        return selectedShipmentDetailData;
    }

    public void setSelectedShipmentDetailData(ShipmentDetailData selectedShipmentDetailData) {
        this.selectedShipmentDetailData = selectedShipmentDetailData;
    }

    public MultipleAddressShipmentAdapterData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
        dest.writeByte(this.isWarning ? (byte) 1 : (byte) 0);
        dest.writeString(this.warningMessage);
        dest.writeInt(this.invoicePosition);
        dest.writeInt(this.shopId);
        dest.writeString(this.senderName);
        dest.writeString(this.productImageUrl);
        dest.writeString(this.productPrice);
        dest.writeString(this.productName);
        dest.writeParcelable(this.itemData, flags);
        dest.writeString(this.courier);
        dest.writeString(this.subTotalAmount);
        dest.writeString(this.destinationDistrictId);
        dest.writeString(this.destinationDistrictName);
        dest.writeString(this.tokenPickup);
        dest.writeString(this.unixTime);
        dest.writeParcelable(this.store, flags);
        dest.writeParcelable(this.shipmentCartData, flags);
        dest.writeParcelable(this.selectedShipmentDetailData, flags);
        dest.writeLong(this.subTotal);
        dest.writeLong(this.productPriceNumber);
        dest.writeString(this.freeReturnLogo);
        dest.writeByte(this.productIsPreorder ? (byte) 1 : (byte) 0);
        dest.writeByte(this.productIsFreeReturns ? (byte) 1 : (byte) 0);
        dest.writeByte(this.productReturnable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.productFinsurance ? (byte) 1 : (byte) 0);
        dest.writeByte(this.productFcancelPartial ? (byte) 1 : (byte) 0);
    }

    protected MultipleAddressShipmentAdapterData(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.isWarning = in.readByte() != 0;
        this.warningMessage = in.readString();
        this.invoicePosition = in.readInt();
        this.shopId = in.readInt();
        this.senderName = in.readString();
        this.productImageUrl = in.readString();
        this.productPrice = in.readString();
        this.productName = in.readString();
        this.itemData = in.readParcelable(MultipleAddressItemData.class.getClassLoader());
        this.courier = in.readString();
        this.subTotalAmount = in.readString();
        this.destinationDistrictId = in.readString();
        this.destinationDistrictName = in.readString();
        this.tokenPickup = in.readString();
        this.unixTime = in.readString();
        this.store = in.readParcelable(Store.class.getClassLoader());
        this.shipmentCartData = in.readParcelable(ShipmentCartData.class.getClassLoader());
        this.selectedShipmentDetailData = in.readParcelable(ShipmentDetailData.class.getClassLoader());
        this.subTotal = in.readLong();
        this.productPriceNumber = in.readLong();
        this.freeReturnLogo = in.readString();
        this.productIsPreorder = in.readByte() != 0;
        this.productIsFreeReturns = in.readByte() != 0;
        this.productReturnable = in.readByte() != 0;
        this.productFinsurance = in.readByte() != 0;
        this.productFcancelPartial = in.readByte() != 0;
    }

    public static final Creator<MultipleAddressShipmentAdapterData> CREATOR = new Creator<MultipleAddressShipmentAdapterData>() {
        @Override
        public MultipleAddressShipmentAdapterData createFromParcel(Parcel source) {
            return new MultipleAddressShipmentAdapterData(source);
        }

        @Override
        public MultipleAddressShipmentAdapterData[] newArray(int size) {
            return new MultipleAddressShipmentAdapterData[size];
        }
    };
}
