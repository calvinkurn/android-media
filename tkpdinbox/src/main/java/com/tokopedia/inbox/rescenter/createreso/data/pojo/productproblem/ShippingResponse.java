package com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem;


import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ShippingResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("detail")
    private ShippingDetailResponse detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShippingDetailResponse getDetail() {
        return detail;
    }

    public void setDetail(ShippingDetailResponse detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ShippingResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", detail='" + detail.toString() + '\'' +
                '}';
    }
}
