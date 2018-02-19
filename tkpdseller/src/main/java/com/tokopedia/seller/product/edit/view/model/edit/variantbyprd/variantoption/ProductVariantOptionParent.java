package com.tokopedia.seller.product.edit.view.model.edit.variantbyprd.variantoption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductVariantOptionParent {
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
}
