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
    private int id; // id for this variant option

    @SerializedName("vuv")
    @Expose
    private int vuv; //variant option id, ex: 19: Ungu

    @SerializedName("value")
    @Expose
    private int value; //ex: "Ungu"

    @SerializedName("t_id")
    @Expose
    private int tId; // temporary ID for submit

    @SerializedName("cstm")
    @Expose
    private String customName; // for submit only. custom name for variant Option. ex; merah delima

    @SerializedName("image")
    @Expose
    private List<ProductPictureViewModel> productPictureViewModelList;

    // TODO FROM CATALOG
    @SerializedName("hex")
    @Expose
    private String hex; // ex; "#bf00ff"

    @SerializedName("picture")
    @Expose
    private ProductVariantOptionChildOriPicture productVariantOptionChildOriPicture;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.vuv);
        dest.writeInt(this.value);
        dest.writeInt(this.tId);
        dest.writeString(this.customName);
        dest.writeTypedList(this.productPictureViewModelList);
        dest.writeString(this.hex);
        dest.writeParcelable(this.productVariantOptionChildOriPicture, flags);
    }

    public ProductVariantOptionChild() {
    }

    protected ProductVariantOptionChild(Parcel in) {
        this.id = in.readInt();
        this.vuv = in.readInt();
        this.value = in.readInt();
        this.tId = in.readInt();
        this.customName = in.readString();
        this.productPictureViewModelList = in.createTypedArrayList(ProductPictureViewModel.CREATOR);
        this.hex = in.readString();
        this.productVariantOptionChildOriPicture = in.readParcelable(ProductVariantOptionChildOriPicture.class.getClassLoader());
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
