
package com.tokopedia.seller.gmsubscribe.data.source.product.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("Product")
    @Expose
    private List<GMProductServiceModel> product = null;
    @SerializedName("Extend")
    @Expose
    private List<GMProductServiceModel> extend = null;
    @SerializedName("PayMethod")
    @Expose
    private String payMethod;

    public List<GMProductServiceModel> getProduct() {
        return product;
    }

    public void setProduct(List<GMProductServiceModel> product) {
        this.product = product;
    }

    public List<GMProductServiceModel> getExtend() {
        return extend;
    }

    public void setExtend(List<GMProductServiceModel> extend) {
        this.extend = extend;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

}
