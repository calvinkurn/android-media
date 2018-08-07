package com.tokopedia.tkpdpdp.estimasiongkir;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ShippingServiceModel {
    @SerializedName("service_name")
    @Expose
    private String name;

    @SerializedName("service_etd")
    @Expose
    private String etd;

    @SerializedName("service_range_price")
    @Expose
    private String rangePrice;

    @SerializedName("service_notes")
    @Expose
    private String notes;

    @SerializedName("products")
    List<Product> products;

    public ShippingServiceModel() {
        products = new ArrayList<>();
    }

    class Product {
        @SerializedName("shipper_name")
        @Expose
        private String name;

        @SerializedName("shipper_product_name")
        @Expose
        private String productName;

        @SerializedName("shipper_formatted_price")
        @Expose
        private String fmtPrice;

        @SerializedName("shipper_etd")
        @Expose
        private String etd;
    }
}
