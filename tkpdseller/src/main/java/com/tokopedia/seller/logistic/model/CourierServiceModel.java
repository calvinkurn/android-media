package com.tokopedia.seller.logistic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourierServiceModel {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private int id; // 0,1
    @SerializedName("active")
    @Expose
    private int active; // 0,1
    @SerializedName("name")
    @Expose
    private String name; // Reguler/OK/SameDay/etc

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getActive() {
        return active;
    }

    public String getName() {
        return name;
    }

}
