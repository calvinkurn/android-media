package com.tokopedia.gm.subscribe.data.source.cart.cloud.inputmodel.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class Item {
    public static final String AUTOEXTEND = "extend";
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("configuration")
    @Expose
    private Map<String, String> configuration;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }
}
