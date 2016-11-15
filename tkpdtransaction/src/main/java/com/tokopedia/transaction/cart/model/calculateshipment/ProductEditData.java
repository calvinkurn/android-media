package com.tokopedia.transaction.cart.model.calculateshipment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;

/**
 * @author anggaprasetiyo on 11/15/16.
 */

public class ProductEditData implements Parcelable {
    @SerializedName("product_cart_id")
    @Expose
    private String productCartId;
    @SerializedName("product_notes")
    @Expose
    private String productNotes;
    @SerializedName("product_quantity")
    @Expose
    private Integer productQuantity;


    public String getProductCartId() {
        return productCartId;
    }

    public void setProductCartId(String productCartId) {
        this.productCartId = productCartId;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productCartId);
        dest.writeString(this.productNotes);
        dest.writeValue(this.productQuantity);
    }

    public ProductEditData() {
    }

    protected ProductEditData(Parcel in) {
        this.productCartId = in.readString();
        this.productNotes = in.readString();
        this.productQuantity = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProductEditData> CREATOR
            = new Parcelable.Creator<ProductEditData>() {
        @Override
        public ProductEditData createFromParcel(Parcel source) {
            return new ProductEditData(source);
        }

        @Override
        public ProductEditData[] newArray(int size) {
            return new ProductEditData[size];
        }
    };

    public static ProductEditData initInstance(CartProduct cartProduct) {
        ProductEditData result = new ProductEditData();
        result.setProductCartId(cartProduct.getProductCartId());
        result.setProductNotes(cartProduct.getProductNotes());
        result.setProductQuantity(cartProduct.getProductQuantity());
        return result;
    }
}
