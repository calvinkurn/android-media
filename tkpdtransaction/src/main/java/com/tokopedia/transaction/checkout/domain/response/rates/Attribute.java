package com.tokopedia.transaction.checkout.domain.response.rates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class Attribute {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("shipper_name")
    @Expose
    private String shipperName;
    @SerializedName("service_id")
    @Expose
    private int serviceId;
    @SerializedName("origin_id")
    @Expose
    private int originId;
    @SerializedName("destination_id")
    @Expose
    private int destinationId;
    @SerializedName("weight")
    @Expose
    private int weight;
    @SerializedName("service_etd")
    @Expose
    private String serviceEtd;
    @SerializedName("weight_service")
    @Expose
    private int weightService;
    @SerializedName("products")
    @Expose
    private List<Product> products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getOriginId() {
        return originId;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getServiceEtd() {
        return serviceEtd;
    }

    public void setServiceEtd(String serviceEtd) {
        this.serviceEtd = serviceEtd;
    }

    public int getWeightService() {
        return weightService;
    }

    public void setWeightService(int weightService) {
        this.weightService = weightService;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
