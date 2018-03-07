package com.tokopedia.tkpdtrain.station.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 3/5/18.
 */

public class TrainStationIslandEntity {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("cities")
    @Expose
    private List<TrainCityEntity> cities;
    @SerializedName("name")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public List<TrainCityEntity> getCities() {
        return cities;
    }

    public String getName() {
        return name;
    }
}
