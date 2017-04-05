
package com.tokopedia.seller.product.data.source.cloud.model.myetalase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("is_allow")
    @Expose
    private Integer isAllow;
    @SerializedName("list")
    @Expose
    private List<EtalaseItem> list = null;

    public Integer getIsAllow() {
        return isAllow;
    }

    public void setIsAllow(Integer isAllow) {
        this.isAllow = isAllow;
    }

    public List<EtalaseItem> getList() {
        return list;
    }

    public void setList(List<EtalaseItem> list) {
        this.list = list;
    }

}
