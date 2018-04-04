package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ashwanityagi on 17/11/17.
 */

public class EventLocationEntity {
    @SerializedName("location")
    @Expose
    private List<EventLocationResponseEntity> locationResponseEntity = null;

    public List<EventLocationResponseEntity> getLocationResponseEntity() {
        return locationResponseEntity;
    }

    public void setLocationResponseEntity(List<EventLocationResponseEntity> locationResponseEntity) {
        this.locationResponseEntity = locationResponseEntity;
    }
}
