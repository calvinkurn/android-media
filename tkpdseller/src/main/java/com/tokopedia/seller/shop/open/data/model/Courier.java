package com.tokopedia.seller.shop.open.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nakama on 19/12/17.
 */

public class Courier {
    @Expose
    private String name;
    @SerializedName("weight_policy")
    @Expose
    private String weightPolicy;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("by_zip_code")
    @Expose
    private int byZipCode;
    @SerializedName("available")
    @Expose
    private int available;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("services")
    @Expose
    private List<CourierService> services = null;
    @SerializedName("weight")
    @Expose
    private int weight;
    @SerializedName("url_additional_option")
    @Expose
    private String urlAdditionalOption; // might "0"

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeightPolicy() {
        return weightPolicy;
    }

    public void setWeightPolicy(String weightPolicy) {
        this.weightPolicy = weightPolicy;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getByZipCode() {
        return byZipCode;
    }

    public void setByZipCode(Integer byZipCode) {
        this.byZipCode = byZipCode;
    }

    public int getAvailable() {
        return available;
    }

    public String getId() {
        return id;
    }

    public List<CourierService> getServices() {
        return services;
    }

    public int getWeight() {
        return weight;
    }

    public String getUrlAdditionalOption() {
        return urlAdditionalOption;
    }

}
