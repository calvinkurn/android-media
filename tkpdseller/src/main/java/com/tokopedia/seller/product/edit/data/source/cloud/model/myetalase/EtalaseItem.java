
package com.tokopedia.seller.product.edit.data.source.cloud.model.myetalase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EtalaseItem {

    @SerializedName("etalase_id")
    @Expose
    private Integer etalaseId;
    @SerializedName("etalase_name")
    @Expose
    private String etalaseName;
    @SerializedName("etalase_num_product")
    @Expose
    private Integer etalaseNumProduct;
    @SerializedName("etalase_total_product")
    @Expose
    private String etalaseTotalProduct;
    @SerializedName("et alase_num_product")
    @Expose
    private Integer etAlaseNumProduct;

    public Integer getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(Integer etalaseId) {
        this.etalaseId = etalaseId;
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

    public String getEtalaseTotalProduct() {
        return etalaseTotalProduct;
    }

    public void setEtalaseTotalProduct(String etalaseTotalProduct) {
        this.etalaseTotalProduct = etalaseTotalProduct;
    }

    public Integer getEtAlaseNumProduct() {
        return etAlaseNumProduct;
    }

    public void setEtAlaseNumProduct(Integer etAlaseNumProduct) {
        this.etAlaseNumProduct = etAlaseNumProduct;
    }

}
