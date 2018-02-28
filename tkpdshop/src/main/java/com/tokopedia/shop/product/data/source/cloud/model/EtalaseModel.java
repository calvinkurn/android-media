package com.tokopedia.shop.product.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class EtalaseModel {
    @SerializedName("use_ace")
    @Expose
    private long useAce;
    @SerializedName("etalase_id")
    @Expose
    private String etalaseId;
    @SerializedName("etalase_name")
    @Expose
    private String etalaseName;
    @SerializedName("etalase_num_product")
    @Expose
    private long etalaseNumProduct;
    @SerializedName("etalase_total_product")
    @Expose
    private long etalaseTotalProduct;
    @SerializedName("etalase_badge")
    @Expose
    private String etalaseBadge;

    public long getUseAce() {
        return useAce;
    }

    public void setUseAce(long useAce) {
        this.useAce = useAce;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public long getEtalaseNumProduct() {
        return etalaseNumProduct;
    }

    public void setEtalaseNumProduct(long etalaseNumProduct) {
        this.etalaseNumProduct = etalaseNumProduct;
    }

    public long getEtalaseTotalProduct() {
        return etalaseTotalProduct;
    }

    public void setEtalaseTotalProduct(long etalaseTotalProduct) {
        this.etalaseTotalProduct = etalaseTotalProduct;
    }

    public String getEtalaseBadge() {
        return etalaseBadge;
    }

    public void setEtalaseBadge(String etalaseBadge) {
        this.etalaseBadge = etalaseBadge;
    }

}
