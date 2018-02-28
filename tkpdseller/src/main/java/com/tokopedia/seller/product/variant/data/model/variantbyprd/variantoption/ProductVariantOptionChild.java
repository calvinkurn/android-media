package com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;

import java.util.List;


public class ProductVariantOptionChild implements Parcelable{
    @SerializedName(value="pvo", alternate={"id"})
    @Expose
    private int pvo; // id for this variant option

    @SerializedName("vuv")
    @Expose
    private int vuv; //variant option id, ex: 19: Ungu

    @SerializedName("t_id")
    @Expose
    private int tId; // temporary ID for submit

    @SerializedName("cstm")
    @Expose
    private String value; // custom name for variant Option. ex; merah delima, also for original value, if vuv is 0

    @SerializedName("image")
    @Expose
    private List<ProductPictureViewModel> productPictureViewModelList;

    @SerializedName("hex")
    @Expose
    private String hex; // ex; "#bf00ff"

    public int getPvo() {
        return pvo;
    }

    public void setPvo(int id) {
        this.pvo = id;
    }

    public int getVuv() {
        return vuv;
    }

    public void setVuv(int vuv) {
        this.vuv = vuv;
    }

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<ProductPictureViewModel> getProductPictureViewModelList() {
        return productPictureViewModelList;
    }

    public void setProductPictureViewModelList(List<ProductPictureViewModel> productPictureViewModelList) {
        this.productPictureViewModelList = productPictureViewModelList;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pvo);
        dest.writeInt(this.vuv);
        dest.writeInt(this.tId);
        dest.writeString(this.value);
        dest.writeTypedList(this.productPictureViewModelList);
        dest.writeString(this.hex);
    }

    public ProductVariantOptionChild() {
    }

    protected ProductVariantOptionChild(Parcel in) {
        this.pvo = in.readInt();
        this.vuv = in.readInt();
        this.tId = in.readInt();
        this.value = in.readString();
        this.productPictureViewModelList = in.createTypedArrayList(ProductPictureViewModel.CREATOR);
        this.hex = in.readString();
    }

    public static final Creator<ProductVariantOptionChild> CREATOR = new Creator<ProductVariantOptionChild>() {
        @Override
        public ProductVariantOptionChild createFromParcel(Parcel source) {
            return new ProductVariantOptionChild(source);
        }

        @Override
        public ProductVariantOptionChild[] newArray(int size) {
            return new ProductVariantOptionChild[size];
        }
    };
}
