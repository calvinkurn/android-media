package com.tokopedia.tkpdtrain.station.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author  by alvarisi on 3/5/18.
 */

public class TrainStationEntity {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("popularity_order")
    @Expose
    private int popularityOrder;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private String code;

    public String getId() {
        return id;
    }

    public int getPopularityOrder() {
        return popularityOrder;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
