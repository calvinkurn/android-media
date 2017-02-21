package com.tokopedia.seller.myproduct.model.temp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by m.normansyah on 12/9/15.
 */
@Deprecated
public class MergePojo {
    @SerializedName("d_id")
    String d_id;
    @SerializedName("name")
    String name;
    @SerializedName("childs")
    List<MergeChilds> childs;

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MergeChilds> getChilds() {
        return childs;
    }

    public void setChilds(List<MergeChilds> childs) {
        this.childs = childs;
    }
}
