package com.tokopedia.seller.product.edit.view.model.edit.variantbyprd.variantoption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @SerializedName("name")
    @Expose
    private String name; // ex; warna

    @SerializedName("identifier")
    @Expose
    private String identifier; // ex: color

    @SerializedName("unit_name")
    @Expose
    private String unitName; // ex: "" (for no unit),  "International"

    @SerializedName(value="pos", alternate={"position"})
    @Expose
    private int position;

    @SerializedName(value="opt", alternate={"option"})
    @Expose
    private ProductVariantOptionChild productVariantOptionChild;
}
