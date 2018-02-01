
package com.tokopedia.seller.product.edit.view.model.edit;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariantCombinationViewModel {

    @SerializedName("st")
    @Expose
    private long st;
    @SerializedName("opt")
    @Expose
    private List<Long> opt = null;

    public long getSt() {
        return st;
    }

    public void setSt(long st) {
        this.st = st;
    }

    public List<Long> getOpt() {
        return opt;
    }

    public void setOpt(List<Long> opt) {
        this.opt = opt;
    }

}
