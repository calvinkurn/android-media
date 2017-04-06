
package com.tokopedia.seller.product.data.source.cloud.model.myetalase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EtalaseItem {

    @SerializedName("etalase_total_product")
    @Expose
    private String etalaseTotalProduct;
    @SerializedName("etalase_name")
    @Expose
    private String etalaseName;
    @SerializedName("etalase_id")
    @Expose
    private Integer etalaseId;
    @SerializedName("etalase_num_product")
    @Expose
    private Integer etalaseNumProduct;

    public String getEtalaseTotalProduct() {
        return etalaseTotalProduct;
    }

    public void setEtalaseTotalProduct(String etalaseTotalProduct) {
        this.etalaseTotalProduct = etalaseTotalProduct;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public Integer getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(Integer etalaseId) {
        this.etalaseId = etalaseId;
    }

    public Integer getEtalaseNumProduct() {
        return etalaseNumProduct;
    }

    public void setEtalaseNumProduct(Integer etalaseNumProduct) {
        this.etalaseNumProduct = etalaseNumProduct;
    }

}
