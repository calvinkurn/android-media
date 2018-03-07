package com.tokopedia.tkpdtrain.station.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 3/5/18.
 */

public class TrainCityEntity {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("stations")
    @Expose
    private List<TrainStationEntity> stations;
    @SerializedName("name")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public List<TrainStationEntity> getStations() {
        return stations;
    }

    public String getName() {
        return name;
    }
}
