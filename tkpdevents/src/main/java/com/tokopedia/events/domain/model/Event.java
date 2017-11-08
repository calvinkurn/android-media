
package com.tokopedia.events.domain.model;

import com.tokopedia.events.data.entity.FilterEntity;
import com.tokopedia.events.data.entity.HomeEntity;

import java.util.List;

public class Event {

    private List<FilterEntity> filters = null;
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
