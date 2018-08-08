package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by alvarisi on 6/5/17.
 */

public class TipListEntity {
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("list")
    @Expose
    private ArrayList<Integer> list = null;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ArrayList<Integer> getList() {
        return list;
    }

    public void setList(ArrayList<Integer> list) {
        this.list = list;
    }
}
