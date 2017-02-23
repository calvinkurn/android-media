package com.tokopedia.seller.topads.domain.model.data;

/**
 * Created by Hendry on 2/23/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Etalase {

    @SerializedName("etalase_total_product")
    @Expose
    private Integer etalaseTotalProduct;
    @SerializedName("etalase_name")
    @Expose
    private String etalaseName;
    @SerializedName("etalase_num_product")
    @Expose
    private Integer etalaseNumProduct;
    @SerializedName("etalase_id")
    @Expose
    private String etalaseId;

    public Integer getEtalaseTotalProduct() {
        return etalaseTotalProduct;
    }

    public void setEtalaseTotalProduct(Integer etalaseTotalProduct) {
        this.etalaseTotalProduct = etalaseTotalProduct;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public Integer getEtalaseNumProduct() {
        return etalaseNumProduct;
    }

    public void setEtalaseNumProduct(Integer etalaseNumProduct) {
        this.etalaseNumProduct = etalaseNumProduct;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

}

