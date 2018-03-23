package com.tokopedia.transaction.checkout.data.entity.response.ratesV2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Irfan Khoirul on 22/03/18.
 */

public class Attribute {

    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("service_id")
    @Expose
    private int serviceId;
    @SerializedName("service_etd")
    @Expose
    private String serviceEtd;
    @SerializedName("service_order")
    @Expose
    private int serviceOrder;
    @SerializedName("service_range_price")
    @Expose
    private String serviceRangePrice;

    @SerializedName("products")
    @Expose
    private List<Product> products;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceEtd() {
        return serviceEtd;
    }

    public void setServiceEtd(String serviceEtd) {
        this.serviceEtd = serviceEtd;
    }

    public int getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(int serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public String getServiceRangePrice() {
        return serviceRangePrice;
    }

    public void setServiceRangePrice(String serviceRangePrice) {
        this.serviceRangePrice = serviceRangePrice;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
