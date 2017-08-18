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
    private int v;
    @SerializedName("vu")
    @Expose
    private int vu;
    @SerializedName("pos")
    @Expose
    private int pos;
    @SerializedName("pv")
    @Expose
    private int pv;
    @SerializedName("optList")
    @Expose
    private List<VarianSubmitOption> varianSubmitOptionList = null;

    /**
     * Varian ID.
     * @return v example: "1" for warna". "6" for "ukuran pakaian"
     */
    public int getV() {
        return v;
    }

    /**
     * Varian ID.
     * @param v example: "1" for warna". "6" for "ukuran pakaian"
     */
    public void setV(int v) {
        this.v = v;
    }

    /**
     * get variant unit chosen for this variant
     * @return for example: example: "8" for "Ukuran pakaian US"
     */
    public int getVu() {
        return vu;
    }

    /**
     * Variant unit id got from server. Set to 0 if there is no variant unit
     * @param vu Variant unit. example: "8" for "Ukuran pakaian US"
     */
    public void setVu(int vu) {
        this.vu = vu;
    }

    /**
     * Position level of the variant
     * @return example: "1" or "2" at most
     */
    public int getPos() {
        return pos;
    }

    /**
     * Position level of the variant
     * @param pos position of variant. Set to 1 if the top level. 2 for the second level.
     */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * product variant id, set with the id got from server
     * @return pv product variant id. example: 659143 for "warna"
     */
    public int getPv() {
        return pv;
    }

    /**
     * product variant id, set with the id got from server
     * @param pv product variant id. example: 659143 for "warna"
     */
    public void setPv(int pv) {
        this.pv = pv;
    }

    /**
     * Option list for this variant. example "merah:1" "hijau:2"
     * @return Option List
     */
    public List<VarianSubmitOption> getVarianSubmitOptionList() {
        return varianSubmitOptionList;
    }

    /**
     * set Option list
     * @param varianSubmitOptionList  example "merah:1" "hijau:2"
     */
    public void setVarianSubmitOptionList(List<VarianSubmitOption> varianSubmitOptionList) {
        this.varianSubmitOptionList = varianSubmitOptionList;
    }

}