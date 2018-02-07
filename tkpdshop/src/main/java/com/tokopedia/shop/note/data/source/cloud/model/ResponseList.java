
package com.tokopedia.shop.note.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseList<T> {

    @SerializedName("list")
    @Expose
    private List<T> list = new ArrayList<>();

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
