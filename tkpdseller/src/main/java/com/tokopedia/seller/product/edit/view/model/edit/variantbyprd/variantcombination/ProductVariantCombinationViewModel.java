
package com.tokopedia.seller.product.edit.view.model.edit.variantbyprd.variantcombination;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariantCombinationViewModel {

    @SerializedName("st")
    @Expose
    private long st;

    @SerializedName("price_var")
    @Expose
    private long priceVar;

    @SerializedName("stock")
    @Expose
    private long stock;

    @SerializedName("sku")
    @Expose
    private String sku;

    @SerializedName("opt")
    @Expose
    private List<Integer> opt = null; // option combination of t_id of selected variants

    public long getSt() {
        return st;
    }

    public void setSt(long st) {
        this.st = st;
    }

    public List<Integer> getOpt() {
        return opt;
    }

    public void setOpt(List<Integer> opt) {
        this.opt = opt;
    }

    public long getPriceVar() {
        return priceVar;
    }

    public void setPriceVar(long priceVar) {
        this.priceVar = priceVar;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
