package com.tokopedia.seller.logistic.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Courier {

    public static final String EXPRESS_COURIER_ID = "1000"; // from API
    public static final String CARGO_COURIER_ID = "1001"; // from API

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
    private List<CourierServiceModel> services = null;
    @SerializedName("weight")
    @Expose
    private int weight;
    @SerializedName("url_additional_option")
    @Expose
    private String urlAdditionalOption; // might "0"

    public String getName() {
        return name;
    }

    public String getWeightPolicy() {
        return weightPolicy;
    }

    public String getLogo() {
        return logo;
    }

    public Integer getByZipCode() {
        return byZipCode;
    }

    public int getAvailable() {
        return available;
    }

    public boolean isAvailable() {
        return available == 1;
    }

    public String getId() {
        return id;
    }

    public boolean isExpressCourierId(){
        return EXPRESS_COURIER_ID.equals(id);
    }

    public boolean isCargoCourierId(){
        return CARGO_COURIER_ID.equals(id);
    }

    public List<CourierServiceModel> getServices() {
        return services;
    }

    public int getWeight() {
        return weight;
    }

    public String getUrlAdditionalOption() {
        return urlAdditionalOption;
    }

    public boolean hasUrlAdditionalOption() {
        return !TextUtils.isEmpty(urlAdditionalOption) &&
                "0".equals(urlAdditionalOption);
    }

}
