package com.tokopedia.flight.booking;

import com.tokopedia.flight.booking.data.cloud.entity.SavedPassengerEntity;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;

import java.util.Observable;

import javax.inject.Inject;

/**
 * @author by furqan on 22/02/18.
 */

public class FlightSavedPassengerDataSource {
    private FlightApi flightApi;

    @Inject
    public FlightSavedPassengerDataSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    public Observable<SavedPassengerEntity> getSavedPassenger() {

    }
}
