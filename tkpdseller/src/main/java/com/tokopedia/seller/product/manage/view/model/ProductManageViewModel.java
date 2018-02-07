package com.tokopedia.seller.product.manage.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.adapter.ItemIdType;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ProductManageViewModel implements ItemIdType, Parcelable {
    public static final int TYPE = 1934;
    public static final String STOCK_READY_VALUE = "1";

    private String productName;
    private String imageUrl;
    private String productId;
    private String productPrice;
    private String productPricePlain;
    private String productStatus;
    private int productCurrencyId;
    private String productUrl;
    private String productCurrencySymbol;
    private int productReturnable;
    private int productPreorder;
    private int productCashback;
    private int productCashbackAmount;
    private int productWholesale;
    private int productUsingStock;
    private int productStock;
    private int productVariant;

    @Override
    public int getType() {
        return TYPE;
    }

    public int getProductWholesale() {
        return productWholesale;
    }

    public void setProductWholesale(int productWholesale) {
        this.productWholesale = productWholesale;
    }

    public int getProductUsingStock() {
        return productUsingStock;
    }

    public void setProductUsingStock(int productUsingStock) {
        this.productUsingStock = productUsingStock;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public String getProductPricePlain() {
        return productPricePlain;
    }

    public void setProductPricePlain(String productPricePlain) {
        this.productPricePlain = productPricePlain;
    }

    public String getProductCurrencySymbol() {
        return productCurrencySymbol;
    }

    public void setProductCurrencySymbol(String productCurrencySymbol) {
        this.productCurrencySymbol = productCurrencySymbol;
    }

    public int getProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(int productReturnable) {
        this.productReturnable = productReturnable;
    }

    public int getProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(int productPreorder) {
        this.productPreorder = productPreorder;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    @Override
    public String getId() {
        return productId;
    }

    public void setTitle(String productName) {
        this.productName = productName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(String productId) {
        this.productId = productId;
    }

    public boolean isStockOrImageEmpty(){
        return productStatus != null && !productStatus.equals(STOCK_READY_VALUE);
    }

    public ProductManageViewModel() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductCurrencyId(int productCurrencyId) {
        this.productCurrencyId = productCurrencyId;
    }

    public int getProductCurrencyId() {
        return productCurrencyId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setProductCashback(int productCashback) {
        this.productCashback = productCashback;
    }

    public int getProductCashback() {
        return productCashback;
    }

    public void setProductCashbackAmount(int productCashbackAmount) {
        this.productCashbackAmount = productCashbackAmount;
    }

    public int getProductCashbackAmount() {
        return productCashbackAmount;
    }

    public int getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(int productVariant) {
        this.productVariant = productVariant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.productId);
        dest.writeString(this.productPrice);
        dest.writeString(this.productPricePlain);
        dest.writeString(this.productStatus);
        dest.writeInt(this.productCurrencyId);
        dest.writeString(this.productUrl);
        dest.writeString(this.productCurrencySymbol);
        dest.writeInt(this.productReturnable);
        dest.writeInt(this.productPreorder);
        dest.writeInt(this.productCashback);
        dest.writeInt(this.productCashbackAmount);
        dest.writeInt(this.productWholesale);
        dest.writeInt(this.productUsingStock);
        dest.writeInt(this.productStock);
        dest.writeInt(this.productVariant);
    }

    protected ProductManageViewModel(Parcel in) {
        this.productName = in.readString();
        this.imageUrl = in.readString();
        this.productId = in.readString();
        this.productPrice = in.readString();
        this.productPricePlain = in.readString();
        this.productStatus = in.readString();
        this.productCurrencyId = in.readInt();
        this.productUrl = in.readString();
        this.productCurrencySymbol = in.readString();
        this.productReturnable = in.readInt();
        this.productPreorder = in.readInt();
        this.productCashback = in.readInt();
        this.productCashbackAmount = in.readInt();
        this.productWholesale = in.readInt();
        this.productUsingStock = in.readInt();
        this.productStock = in.readInt();
        this.productVariant = in.readInt();
    }

    public static final Creator<ProductManageViewModel> CREATOR = new Creator<ProductManageViewModel>() {
        @Override
        public ProductManageViewModel createFromParcel(Parcel source) {
            return new ProductManageViewModel(source);
        }

        @Override
        public ProductManageViewModel[] newArray(int size) {
            return new ProductManageViewModel[size];
        }
    };
}
