package com.tokopedia.ride.common.place.domain.model;

/**
 * Created by alvarisi on 3/18/17.
 */

public class OverviewPolyline {
    private String overviewPolyline;
    private Bounds bounds;

    public OverviewPolyline() {
    }

    public String getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(String overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
}
