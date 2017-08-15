package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 8/15/2017.
 */

public class VariantDatum {

    // currently not used
    /*@SerializedName("pvd_id")
    @Expose
    private int pvdId;*/

    // 0: not active, 1: active
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("stock")
    @Expose
    private int stock;

    // combinastion of variant code
    @SerializedName("v_code")
    @Expose
    private String vCode;

    /*public int getPvdId() {
        return pvdId;
    }

    public void setPvdId(int pvdId) {
        this.pvdId = pvdId;
    }*/

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getVCode() {
        return vCode;
    }

    public void setVCode(String vCode) {
        this.vCode = vCode;
    }

}
