package com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 14/08/17.
 */

public class OrderDetailResponse {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("returnable")
    @Expose
    private int returnable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReturnable() {
        return returnable;
    }

    public void setReturnable(int returnable) {
        this.returnable = returnable;
    }

    @Override
    public String toString() {
        return "OrderDetailResponse{" +
                "id='" + id + '\'' +
                ", returnable='" + returnable + '\'' +
                '}';
    }
}
