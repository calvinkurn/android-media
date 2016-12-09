package com.tokopedia.inbox.rescenter.edit.model.passdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/28/16.
 */
public class ProductData implements Parcelable {


    @SerializedName("pt_is_free_return")
    private int isFreeReturn;
    @SerializedName("pt_primary_dtl_photo")
    private String primaryDtlPhoto;
    @SerializedName("pt_product_name")
    private String productName;
    @SerializedName("pt_primary_photo")
    private String primaryPhoto;
    @SerializedName("pt_show_input_quantity")
    private int showInputQuantity;
    @SerializedName("pt_product_id")
    private String productId;
    @SerializedName("pt_order_dtl_id")
    private String orderDetailId;
    @SerializedName("pt_quantity")
    private int quantity;

    public int getIsFreeReturn() {
        return isFreeReturn;
    }

    public void setIsFreeReturn(int isFreeReturn) {
        this.isFreeReturn = isFreeReturn;
    }

    public String getPrimaryDtlPhoto() {
        return primaryDtlPhoto;
    }

    public void setPrimaryDtlPhoto(String primaryDtlPhoto) {
        this.primaryDtlPhoto = primaryDtlPhoto;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrimaryPhoto() {
        return primaryPhoto;
    }

    public void setPrimaryPhoto(String primaryPhoto) {
        this.primaryPhoto = primaryPhoto;
    }

    public int getShowInputQuantity() {
        return showInputQuantity;
    }

    public void setShowInputQuantity(int showInputQuantity) {
        this.showInputQuantity = showInputQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.isFreeReturn);
        dest.writeString(this.primaryDtlPhoto);
        dest.writeString(this.productName);
        dest.writeString(this.primaryPhoto);
        dest.writeInt(this.showInputQuantity);
        dest.writeString(this.productId);
        dest.writeString(this.orderDetailId);
        dest.writeInt(this.quantity);
    }

    public ProductData() {
    }

    protected ProductData(Parcel in) {
        this.isFreeReturn = in.readInt();
        this.primaryDtlPhoto = in.readString();
        this.productName = in.readString();
        this.primaryPhoto = in.readString();
        this.showInputQuantity = in.readInt();
        this.productId = in.readString();
        this.orderDetailId = in.readString();
        this.quantity = in.readInt();
    }

    public static final Creator<ProductData> CREATOR = new Creator<ProductData>() {
        @Override
        public ProductData createFromParcel(Parcel source) {
            return new ProductData(source);
        }

        @Override
        public ProductData[] newArray(int size) {
            return new ProductData[size];
        }
    };
}
