package com.tokopedia.tkpdtrain.search.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainListSchedulesEntity {

    @SerializedName("schedules")
    @Expose
    private List<TrainScheduleEntity> trainSchedules;

    @SerializedName("availability_keys")
    @Expose
    private List<AvailabilityKeysEntity> availabilityKeys;

    public List<TrainScheduleEntity> getTrainSchedules() {
        return trainSchedules;
    }

    public List<AvailabilityKeysEntity> getAvailabilityKeys() {
        return availabilityKeys;
    }
}
