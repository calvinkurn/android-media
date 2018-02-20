package com.tokopedia.seller.product.edit.view.model.edit.variantbyprd.variantoption;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductVariantOptionParent implements Parcelable{
    @SerializedName("pv")
    @Expose
    private int pv; // id for this variant

    @SerializedName("v")
    @Expose
    private int v; //variant id, ex: 1: color

    @SerializedName("vu")
    @Expose
    private int vu; //variant unit, ex: 0 for no unit; 7 for ukuran

    @SerializedName(value="pos", alternate={"position"})
    @Expose
    private int position;

    @SerializedName(value="opt", alternate={"option"})
    @Expose
    private List<ProductVariantOptionChild> productVariantOptionChild;

    //TODO from catalog
    //@SerializedName("name")
    //@Expose
    //private String name; // ex; warna

    //@SerializedName("identifier")
    //@Expose
    //private String identifier; // ex: color

    //@SerializedName("unit_name")
    //@Expose
    //private String unitName; // ex: "" (for no unit),  "International"

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getVu() {
        return vu;
    }

    public void setVu(int vu) {
        this.vu = vu;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<ProductVariantOptionChild> getProductVariantOptionChild() {
        return productVariantOptionChild;
    }

    public void setProductVariantOptionChild(List<ProductVariantOptionChild> productVariantOptionChild) {
        this.productVariantOptionChild = productVariantOptionChild;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pv);
        dest.writeInt(this.v);
        dest.writeInt(this.vu);
        dest.writeInt(this.position);
        dest.writeList(this.productVariantOptionChild);
    }

    public ProductVariantOptionParent() {
    }

    protected ProductVariantOptionParent(Parcel in) {
        this.pv = in.readInt();
        this.v = in.readInt();
        this.vu = in.readInt();
        this.position = in.readInt();
        this.productVariantOptionChild = new ArrayList<ProductVariantOptionChild>();
        in.readList(this.productVariantOptionChild, ProductVariantOptionChild.class.getClassLoader());
    }

    public static final Creator<ProductVariantOptionParent> CREATOR = new Creator<ProductVariantOptionParent>() {
        @Override
        public ProductVariantOptionParent createFromParcel(Parcel source) {
            return new ProductVariantOptionParent(source);
        }

        @Override
        public ProductVariantOptionParent[] newArray(int size) {
            return new ProductVariantOptionParent[size];
        }
    };
}
