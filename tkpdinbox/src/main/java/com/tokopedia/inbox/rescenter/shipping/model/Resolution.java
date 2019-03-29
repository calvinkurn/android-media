package com.tokopedia.inbox.rescenter.shipping.model;

import com.google.gson.annotations.SerializedName;

public class Resolution {

    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
