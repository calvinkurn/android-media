package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class CartItemModel implements Parcelable {

    private String shopId;
    private String shopName;
    private String productId;
    private String productName;
    private String productPriceFormatted;
    private double productPricePlan;
    private int productPriceCurrency;

    private int productWeightUnit;
    private double productWeightPlan;
    private String productWeightFormatted;

    private int totalProductItem;
    private String noteToSeller;

    private String productImageUrl;

    private String cashback;
    private boolean isCashback;
    private boolean poAvailable;
    private boolean freeReturn;

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPriceFormatted() {
        return productPriceFormatted;
    }

    public void setProductPriceFormatted(String productPriceFormatted) {
        this.productPriceFormatted = productPriceFormatted;
    }

    public double getProductPricePlan() {
        return productPricePlan;
    }

    public void setProductPricePlan(double productPricePlan) {
        this.productPricePlan = productPricePlan;
    }

    public int getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(int productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public int getProductWeightUnit() {
        return productWeightUnit;
    }

    public void setProductWeightUnit(int productWeightUnit) {
        this.productWeightUnit = productWeightUnit;
    }

    public double getProductWeightPlan() {
        return productWeightPlan;
    }

    public void setProductWeightPlan(double productWeightPlan) {
        this.productWeightPlan = productWeightPlan;
    }

    public String getProductWeightFormatted() {
        return productWeightFormatted;
    }

    public void setProductWeightFormatted(String productWeightFormatted) {
        this.productWeightFormatted = productWeightFormatted;
    }

    public int getTotalProductItem() {
        return totalProductItem;
    }

    public void setTotalProductItem(int totalProductItem) {
        this.totalProductItem = totalProductItem;
    }

    public String getNoteToSeller() {
        return noteToSeller;
    }

    public void setNoteToSeller(String noteToSeller) {
        this.noteToSeller = noteToSeller;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public boolean isCashback() {
        return isCashback;
    }

    public void setCashback(boolean cashback) {
        isCashback = cashback;
    }

    public boolean isPoAvailable() {
        return poAvailable;
    }

    public void setPoAvailable(boolean poAvailable) {
        this.poAvailable = poAvailable;
    }

    public boolean isFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        this.freeReturn = freeReturn;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopId);
        dest.writeString(this.shopName);
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.productPriceFormatted);
        dest.writeDouble(this.productPricePlan);
        dest.writeInt(this.productPriceCurrency);
        dest.writeInt(this.productWeightUnit);
        dest.writeDouble(this.productWeightPlan);
        dest.writeString(this.productWeightFormatted);
        dest.writeInt(this.totalProductItem);
        dest.writeString(this.noteToSeller);
        dest.writeString(this.productImageUrl);
        dest.writeString(this.cashback);
        dest.writeByte(this.isCashback ? (byte) 1 : (byte) 0);
        dest.writeByte(this.poAvailable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.freeReturn ? (byte) 1 : (byte) 0);
    }

    public CartItemModel() {
    }

    protected CartItemModel(Parcel in) {
        this.shopId = in.readString();
        this.shopName = in.readString();
        this.productId = in.readString();
        this.productName = in.readString();
        this.productPriceFormatted = in.readString();
        this.productPricePlan = in.readDouble();
        this.productPriceCurrency = in.readInt();
        this.productWeightUnit = in.readInt();
        this.productWeightPlan = in.readDouble();
        this.productWeightFormatted = in.readString();
        this.totalProductItem = in.readInt();
        this.noteToSeller = in.readString();
        this.productImageUrl = in.readString();
        this.cashback = in.readString();
        this.isCashback = in.readByte() != 0;
        this.poAvailable = in.readByte() != 0;
        this.freeReturn = in.readByte() != 0;
    }

    public static final Creator<CartItemModel> CREATOR = new Creator<CartItemModel>() {
        @Override
        public CartItemModel createFromParcel(Parcel source) {
            return new CartItemModel(source);
        }

        @Override
        public CartItemModel[] newArray(int size) {
            return new CartItemModel[size];
        }
    };
}
