package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.FareEntity;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.TripEntity;
import com.tokopedia.ride.common.ride.domain.model.Fare;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.Trip;

/**
 * Created by alvarisi on 3/21/17.
 */

public class FareEstimateMapper {
    public FareEstimateMapper() {
    }

    public FareEstimate transform(FareEstimateEntity estimateEntity){
        FareEstimate fareEstimate = null;
        if (estimateEntity != null){
            fareEstimate = new FareEstimate();
            fareEstimate.setFare(transformFareEntity(estimateEntity.getFare()));
            fareEstimate.setTrip(transformTripEntity(estimateEntity.getTrip()));
            fareEstimate.setPickupEstimate(estimateEntity.getPickupEstimate());
        }
        return fareEstimate;
    }

    private Trip transformTripEntity(TripEntity entity) {
        Trip trip = null;
        if (entity != null){
            trip = new Trip();
            trip.setDistanceEstimate(entity.getDistanceEstimate());
            trip.setDistanceUnit(entity.getDistanceUnit());
            trip.setDurationEstimate(entity.getDurationEstimate());
        }
        return trip;
    }

    private Fare transformFareEntity(FareEntity entity) {
        Fare fare1 = null;
        if (entity != null){
            fare1 = new Fare();
            fare1.setCurrencyCode(entity.getCurrencyCode());
            fare1.setDisplay(entity.getDisplay());
            fare1.setExpiresAt(entity.getExpiresAt());
            fare1.setFareId(entity.getFareId());
            fare1.setValue(entity.getValue());
        }
        return fare1;
    }
}
