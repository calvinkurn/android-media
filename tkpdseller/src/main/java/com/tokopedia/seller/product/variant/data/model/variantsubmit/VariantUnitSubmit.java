package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 8/15/2017.
 */

public class VariantUnitSubmit {

    @SerializedName("v")
    @Expose
    private int variantId;
    @SerializedName("vu")
    @Expose
    private int variantUnitId;
    @SerializedName("pos")
    @Expose
    private int position;
    @SerializedName("pv")
    @Expose
    private int productVariant;
    @SerializedName("opt")
    @Expose
    private List<VarianSubmitOption> variantSubmitOptionList;

    /**
     * Varian ID.
     * @return variantId example: "1" for warna". "6" for "ukuran pakaian"
     */
    public int getVariantId() {
        return variantId;
    }

    /**
     * Varian ID.
     * @param variantId example: "1" for warna". "6" for "ukuran pakaian"
     */
    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    /**
     * get variant unit chosen for this variant
     * @return for example: example: "8" for "Ukuran pakaian US"
     */
    public int getVariantUnitId() {
        return variantUnitId;
    }

    /**
     * Variant unit id got from server. Set to 0 if there is no variant unit
     * @param variantUnitId Variant unit. example: "8" for "Ukuran pakaian US"
     */
    public void setVariantUnitId(int variantUnitId) {
        this.variantUnitId = variantUnitId;
    }

    /**
     * Position level of the variant
     * @return example: "1" or "2" at most
     */
    public int getPosition() {
        return position;
    }

    /**
     * Position level of the variant
     * @param position position of variant. Set to 1 if the top level. 2 for the second level.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * product variant id, set with the id got from server
     * @return productVariant product variant id. example: 659143 for "warna"
     */
    public int getProductVariant() {
        return productVariant;
    }

    /**
     * product variant id, set with the id got from server
     * @param productVariant product variant id. example: 659143 for "warna"
     */
    public void setProductVariant(int productVariant) {
        this.productVariant = productVariant;
    }

    /**
     * Option list for this variant. example "merah:1" "hijau:2"
     * @return Option List
     */
    public List<VarianSubmitOption> getVariantSubmitOptionList() {
        return variantSubmitOptionList;
    }

    /**
     * set Option list
     * @param variantSubmitOptionList  example "merah:1" "hijau:2"
     */
    public void setVariantSubmitOptionList(List<VarianSubmitOption> variantSubmitOptionList) {
        this.variantSubmitOptionList = variantSubmitOptionList;
    }

}