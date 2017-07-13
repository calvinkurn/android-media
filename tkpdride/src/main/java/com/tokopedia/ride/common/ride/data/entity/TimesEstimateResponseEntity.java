package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 3/20/17.
 */

public class TimesEstimateResponseEntity {
    public List<TimesEstimateEntity> getTimes() {
        return times;
    }

    public void setTimes(List<TimesEstimateEntity> times) {
        this.times = times;
    }

    @SerializedName("times")
    @Expose
    List<TimesEstimateEntity> times;

    public TimesEstimateResponseEntity() {
    }
}
