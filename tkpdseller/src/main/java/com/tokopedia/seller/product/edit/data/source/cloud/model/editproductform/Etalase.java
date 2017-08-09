
package com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Etalase {

    @SerializedName("etalase_id")
    @Expose
    private int etalaseId;
    @SerializedName("etalase_name")
    @Expose
    private String etalaseName;
    @SerializedName("etalase_num_product")
    @Expose
    private int etalaseNumProduct;
    @SerializedName("etalase_total_product")
    @Expose
    private String etalaseTotalProduct;
    @SerializedName("etalas e_num_product")
    @Expose
    private int etalasENumProduct;

    public int getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(int etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public int getEtalaseNumProduct() {
        return etalaseNumProduct;
    }

    public void setEtalaseNumProduct(int etalaseNumProduct) {
        this.etalaseNumProduct = etalaseNumProduct;
    }

    public String getEtalaseTotalProduct() {
        return etalaseTotalProduct;
    }

    public void setEtalaseTotalProduct(String etalaseTotalProduct) {
        this.etalaseTotalProduct = etalaseTotalProduct;
    }

    public int getEtalasENumProduct() {
        return etalasENumProduct;
    }

    public void setEtalasENumProduct(int etalasENumProduct) {
        this.etalasENumProduct = etalasENumProduct;
    }

}
