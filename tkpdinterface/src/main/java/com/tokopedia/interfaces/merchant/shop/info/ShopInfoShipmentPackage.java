
package com.tokopedia.interfaces.merchant.shop.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoShipmentPackage {

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("shipping_id")
    @Expose
    private String shippingId;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

}
