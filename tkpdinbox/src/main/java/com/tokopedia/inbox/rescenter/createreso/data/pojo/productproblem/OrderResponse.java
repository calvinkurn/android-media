package com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 14/08/17.
 */

public class OrderResponse {
    @SerializedName("detail")
    @Expose
    private OrderDetailResponse detail;
    @SerializedName("product")
    @Expose
    private OrderProductResponse product;
    @SerializedName("shipping")
    @Expose
    private ShippingResponse shipping;

    public OrderDetailResponse getDetail() {
        return detail;
    }

    public void setDetail(OrderDetailResponse detail) {
        this.detail = detail;
    }

    public OrderProductResponse getProduct() {
        return product;
    }

    public void setProduct(OrderProductResponse product) {
        this.product = product;
    }

    public ShippingResponse getShipping() {
        return shipping;
    }

    public void setShipping(ShippingResponse shipping) {
        this.shipping = shipping;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "detail='" + detail.toString() + '\'' +
                ", product='" + product.toString() + '\'' +
                ", shipping='" + shipping.toString() + '\'' +
                '}';
    }
}

