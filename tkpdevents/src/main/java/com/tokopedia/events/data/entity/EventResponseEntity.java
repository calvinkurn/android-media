
package com.tokopedia.events.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventResponseEntity {

    @SerializedName("filters")
    @Expose
    private List<FilterEntity> filters = null;
    @SerializedName("home")
    @Expose
    private HomeEntity home;

    public List<FilterEntity> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterEntity> filters) {
        this.filters = filters;
    }

    public HomeEntity getHome() {
        return home;
    }

    public void setHome(HomeEntity home) {
        this.home = home;
    }

}
