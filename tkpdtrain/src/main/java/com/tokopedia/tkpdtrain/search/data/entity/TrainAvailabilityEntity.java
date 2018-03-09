package com.tokopedia.tkpdtrain.search.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainAvailabilityEntity {

    @SerializedName("id")
    @Expose
    private String idTrain;
    @SerializedName("available")
    @Expose
    private int availableSeat;

    public String getIdTrain() {
        return idTrain;
    }

    public int getAvailableSeat() {
        return availableSeat;
    }
}
