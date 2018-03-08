package com.tokopedia.transaction.checkout.domain.datamodel;

import com.tokopedia.transaction.pickuppoint.domain.model.Store;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressShipmentAdapterData {

    private int invoicePosition;

    private int productId;

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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
}
