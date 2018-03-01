package com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class Product implements Parcelable {

    private boolean isError;
    private String errorMessage;

    private int productId;
    private String productName;
    private String productPriceFmt;
    private int productPrice;
    private int productWholesalePrice;
    private String productWholesalePriceFmt;
    private String productWeightFmt;
    private int productWeight;
    private int productCondition;
    private String productUrl;
    private boolean productReturnable;
    private boolean productIsFreeReturns;
    private boolean productIsPreorder;
    private String productCashback;
    private int productMinOrder;
    private int productInvenageValue;
    private int productSwitchInvenage;
    private int productPriceCurrency;
    private String productImageSrc200Square;
    private String productNotes;
    private int productQuantity;
    private int productMenuId;
    private boolean productFinsurance;
    private boolean productFcancelPartial;
    private List<ProductShipment> productShipment = new ArrayList<>();
    private List<ProductShipmentMapping> productShipmentMapping = new ArrayList<>();
    private int productCatId;
    private int productCatalogId;

    public void setError(boolean error) {
        isError = error;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPriceFmt(String productPriceFmt) {
        this.productPriceFmt = productPriceFmt;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductWholesalePrice(int productWholesalePrice) {
        this.productWholesalePrice = productWholesalePrice;
    }

    public void setProductWholesalePriceFmt(String productWholesalePriceFmt) {
        this.productWholesalePriceFmt = productWholesalePriceFmt;
    }

    public void setProductWeightFmt(String productWeightFmt) {
        this.productWeightFmt = productWeightFmt;
    }

    public void setProductWeight(int productWeight) {
        this.productWeight = productWeight;
    }

    public void setProductCondition(int productCondition) {
        this.productCondition = productCondition;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setProductReturnable(boolean productReturnable) {
        this.productReturnable = productReturnable;
    }

    public void setProductIsFreeReturns(boolean productIsFreeReturns) {
        this.productIsFreeReturns = productIsFreeReturns;
    }

    public void setProductIsPreorder(boolean productIsPreorder) {
        this.productIsPreorder = productIsPreorder;
    }

    public void setProductCashback(String productCashback) {
        this.productCashback = productCashback;
    }

    public void setProductMinOrder(int productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    public void setProductInvenageValue(int productInvenageValue) {
        this.productInvenageValue = productInvenageValue;
    }

    public void setProductSwitchInvenage(int productSwitchInvenage) {
        this.productSwitchInvenage = productSwitchInvenage;
    }

    public void setProductPriceCurrency(int productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public void setProductImageSrc200Square(String productImageSrc200Square) {
        this.productImageSrc200Square = productImageSrc200Square;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setProductMenuId(int productMenuId) {
        this.productMenuId = productMenuId;
    }

    public void setProductFinsurance(boolean productFinsurance) {
        this.productFinsurance = productFinsurance;
    }

    public void setProductFcancelPartial(boolean productFcancelPartial) {
        this.productFcancelPartial = productFcancelPartial;
    }

    public void setProductShipment(List<ProductShipment> productShipment) {
        this.productShipment = productShipment;
    }

    public void setProductShipmentMapping(List<ProductShipmentMapping> productShipmentMapping) {
        this.productShipmentMapping = productShipmentMapping;
    }

    public void setProductCatId(int productCatId) {
        this.productCatId = productCatId;
    }

    public void setProductCatalogId(int productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public boolean isError() {
        return isError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPriceFmt() {
        return productPriceFmt;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductWholesalePrice() {
        return productWholesalePrice;
    }

    public String getProductWholesalePriceFmt() {
        return productWholesalePriceFmt;
    }

    public String getProductWeightFmt() {
        return productWeightFmt;
    }

    public int getProductWeight() {
        return productWeight;
    }

    public int getProductCondition() {
        return productCondition;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public boolean isProductReturnable() {
        return productReturnable;
    }

    public boolean isProductIsFreeReturns() {
        return productIsFreeReturns;
    }

    public boolean isProductIsPreorder() {
        return productIsPreorder;
    }

    public String getProductCashback() {
        return productCashback;
    }

    public int getProductMinOrder() {
        return productMinOrder;
    }

    public int getProductInvenageValue() {
        return productInvenageValue;
    }

    public int getProductSwitchInvenage() {
        return productSwitchInvenage;
    }

    public int getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public String getProductImageSrc200Square() {
        return productImageSrc200Square;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public int getProductMenuId() {
        return productMenuId;
    }

    public boolean isProductFinsurance() {
        return productFinsurance;
    }

    public boolean isProductFcancelPartial() {
        return productFcancelPartial;
    }

    public List<ProductShipment> getProductShipment() {
        return productShipment;
    }

    public List<ProductShipmentMapping> getProductShipmentMapping() {
        return productShipmentMapping;
    }

    public int getProductCatId() {
        return productCatId;
    }

    public int getProductCatalogId() {
        return productCatalogId;
    }

    public Product() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
        dest.writeInt(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.productPriceFmt);
        dest.writeInt(this.productPrice);
        dest.writeInt(this.productWholesalePrice);
        dest.writeString(this.productWholesalePriceFmt);
        dest.writeString(this.productWeightFmt);
        dest.writeInt(this.productWeight);
        dest.writeInt(this.productCondition);
        dest.writeString(this.productUrl);
        dest.writeByte(this.productReturnable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.productIsFreeReturns ? (byte) 1 : (byte) 0);
        dest.writeByte(this.productIsPreorder ? (byte) 1 : (byte) 0);
        dest.writeString(this.productCashback);
        dest.writeInt(this.productMinOrder);
        dest.writeInt(this.productInvenageValue);
        dest.writeInt(this.productSwitchInvenage);
        dest.writeInt(this.productPriceCurrency);
        dest.writeString(this.productImageSrc200Square);
        dest.writeString(this.productNotes);
        dest.writeInt(this.productQuantity);
        dest.writeInt(this.productMenuId);
        dest.writeByte(this.productFinsurance ? (byte) 1 : (byte) 0);
        dest.writeByte(this.productFcancelPartial ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.productShipment);
        dest.writeTypedList(this.productShipmentMapping);
        dest.writeInt(this.productCatId);
        dest.writeInt(this.productCatalogId);
    }

    protected Product(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.productId = in.readInt();
        this.productName = in.readString();
        this.productPriceFmt = in.readString();
        this.productPrice = in.readInt();
        this.productWholesalePrice = in.readInt();
        this.productWholesalePriceFmt = in.readString();
        this.productWeightFmt = in.readString();
        this.productWeight = in.readInt();
        this.productCondition = in.readInt();
        this.productUrl = in.readString();
        this.productReturnable = in.readByte() != 0;
        this.productIsFreeReturns = in.readByte() != 0;
        this.productIsPreorder = in.readByte() != 0;
        this.productCashback = in.readString();
        this.productMinOrder = in.readInt();
        this.productInvenageValue = in.readInt();
        this.productSwitchInvenage = in.readInt();
        this.productPriceCurrency = in.readInt();
        this.productImageSrc200Square = in.readString();
        this.productNotes = in.readString();
        this.productQuantity = in.readInt();
        this.productMenuId = in.readInt();
        this.productFinsurance = in.readByte() != 0;
        this.productFcancelPartial = in.readByte() != 0;
        this.productShipment = in.createTypedArrayList(ProductShipment.CREATOR);
        this.productShipmentMapping = in.createTypedArrayList(ProductShipmentMapping.CREATOR);
        this.productCatId = in.readInt();
        this.productCatalogId = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
