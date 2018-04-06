
package com.tokopedia.seller.product.edit.view.model.edit;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariantOptionViewModel {

    @SerializedName("pvo")
    @Expose
    private long pvo;
    @SerializedName("vuv")
    @Expose
    private long vuv;
    @SerializedName("t_id")
    @Expose
    private long tId;
    @SerializedName("cstm")
    @Expose
    private String cstm;
    @SerializedName("image")
    @Expose
    private List<ProductVariantImageViewModel> image = null;

    public long getPvo() {
        return pvo;
    }

    public void setPvo(long pvo) {
        this.pvo = pvo;
    }

    public long getVuv() {
        return vuv;
    }

    public void setVuv(long vuv) {
        this.vuv = vuv;
    }

    public long getTId() {
        return tId;
    }

    public void setTId(long tId) {
        this.tId = tId;
    }

    public String getCstm() {
        return cstm;
    }

    public void setCstm(String cstm) {
        this.cstm = cstm;
    }

    public List<ProductVariantImageViewModel> getImage() {
        return image;
    }

    public void setImage(List<ProductVariantImageViewModel> image) {
        this.image = image;
    }

}
