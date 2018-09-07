package com.tokopedia.ride.bookingride.data;

import com.tokopedia.ride.bookingride.data.entity.NearbyRidesEntity;
import com.tokopedia.ride.bookingride.data.entity.RideLocationEntity;
import com.tokopedia.ride.bookingride.domain.model.NearbyRides;
import com.tokopedia.ride.common.ride.domain.model.Location;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by Vishal
 */

public class NearbyRidesDestinationMapper implements Func1<NearbyRidesEntity, NearbyRides> {

    @Override
    public NearbyRides call(NearbyRidesEntity nearbyRidesEntity) {
        NearbyRides rides = new NearbyRides();

        if (nearbyRidesEntity != null) {
            rides.setBikes(transform(nearbyRidesEntity.getBike()));
            rides.setCars(transform(nearbyRidesEntity.getCar()));
        }
        return rides;
    }

    private ArrayList<Location> transform(List<RideLocationEntity> bike) {
        ArrayList<Location> locations = new ArrayList<>();
        if (bike != null) {
            for (RideLocationEntity locationEntity : bike) {
                Location location = new Location();
                location.setLatitude(locationEntity.getLatitude());
                location.setLongitude(locationEntity.getLongitude());
                locations.add(location);
            }
        }

        return locations;
    }
}
