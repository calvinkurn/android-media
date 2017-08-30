package com.tokopedia.ride.common.place.data;

import com.tokopedia.ride.common.place.data.entity.Bounds;
import com.tokopedia.ride.common.place.data.entity.DirectionEntity;
import com.tokopedia.ride.common.place.data.entity.Location;
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

    /*public OverviewPolyline transformToOverviewPolyline(DirectionEntity entity) {
        OverviewPolyline direction;
        if (entity != null) {
            direction = new OverviewPolyline();
            direction.setOverviewPolyline(transformRoutes(entity.getRoutes()));
            return direction;
        }
        return null;
    }*/

    private List<String> transformRoutes(List<Route> routes) {
        List<String> overviews = new ArrayList<>();
        for (Route route : routes) {
            overviews.add(route.getOverviewPolyline().getPoints());
        }
        return overviews;
    }

    public List<OverviewPolyline> transformOverViews(DirectionEntity directionEntity) {
        List<OverviewPolyline> polylines = new ArrayList<>();
        OverviewPolyline polyline;
        for (Route route : directionEntity.getRoutes()) {
            polyline = transformPolyline(route);
            polylines.add(polyline);
        }
        return polylines;
    }

    private OverviewPolyline transformPolyline(Route route) {
        OverviewPolyline overviewPolyline = new OverviewPolyline();
        overviewPolyline.setOverviewPolyline(route.getOverviewPolyline().getPoints());
        overviewPolyline.setBounds(transformBounds(route.getBounds()));
        return overviewPolyline;
    }

    private com.tokopedia.ride.common.place.domain.model.Bounds transformBounds(Bounds entity) {
        com.tokopedia.ride.common.place.domain.model.Bounds bounds = new com.tokopedia.ride.common.place.domain.model.Bounds();
        bounds.setNortheast(transformLocation(entity.getNortheast()));
        bounds.setSouthwest(transformLocation(entity.getSouthwest()));
        return bounds;
    }

    private com.tokopedia.ride.common.ride.domain.model.Location transformLocation(Location location) {
        com.tokopedia.ride.common.ride.domain.model.Location location1 = new com.tokopedia.ride.common.ride.domain.model.Location();
        location1.setLatitude(location.getLat());
        location1.setLongitude(location.getLng());
        return location1;
    }
}
