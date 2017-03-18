package com.tokopedia.ride.common.place.data;

import com.tokopedia.ride.common.place.data.entity.DirectionEntity;
import com.tokopedia.ride.common.place.data.entity.Route;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 3/18/17.
 */

public class DirectionEntityMapper {
    public DirectionEntityMapper() {
    }

    public OverviewPolyline transformToOverviewPolyline(DirectionEntity entity) {
        OverviewPolyline direction;
        if (entity != null) {
            direction = new OverviewPolyline();
            direction.setOverviewPolyline(transformRoutes(entity.getRoutes()));
            return direction;
        }
        return null;
    }

    private List<String> transformRoutes(List<Route> routes) {
        List<String> overviews = new ArrayList<>();
        for (Route route : routes) {
            overviews.add(route.getOverviewPolyline().getPoints());
        }
        return overviews;
    }
}
