package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.RideAddressEntity;
import com.tokopedia.ride.common.ride.domain.model.RideAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 5/2/17.
 */

public class RideAddressEntityMapper {
    public RideAddressEntityMapper() {
    }

    public List<RideAddress> transform(List<RideAddressEntity> entities) {
        List<RideAddress> rideAddresses = new ArrayList<>();
        RideAddress rideAddress;
        for (RideAddressEntity entity : entities) {
            rideAddress = transform(entity);
            if (rideAddress != null) {
                rideAddresses.add(rideAddress);
            }
        }
        return rideAddresses;
    }

    private RideAddress transform(RideAddressEntity entity) {
        RideAddress rideAddress = null;
        if (entity != null) {
            rideAddress = new RideAddress();
            rideAddress.setAddressDescription(entity.getAddressDescription());
            rideAddress.setAddressName(entity.getAddressName());
            rideAddress.setLatitude(entity.getLatitude());
            rideAddress.setLongitude(entity.getLongitude());
            rideAddress.setPrefill(entity.getPrefill());
        }
        return rideAddress;
    }
}
