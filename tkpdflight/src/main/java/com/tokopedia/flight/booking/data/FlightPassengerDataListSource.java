package com.tokopedia.flight.booking.data;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.flight.booking.data.cache.FlightPassengerDataListCacheSource;
import com.tokopedia.flight.booking.data.cloud.FlightSavedPassengerDataListCloudSource;
import com.tokopedia.flight.booking.data.cloud.entity.SavedPassengerEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.booking.data.db.FlightPassengerDataListDBSource;
import com.tokopedia.flight.booking.data.db.model.FlightPassengerDB;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 28/02/18.
 */

public class FlightPassengerDataListSource extends DataListSource<SavedPassengerEntity, FlightPassengerDB> {

    FlightPassengerDataListDBSource flightPassengerDataListDBSource;
    FlightSavedPassengerDataListCloudSource flightSavedPassengerDataListCloudSource;

    @Inject
    public FlightPassengerDataListSource(FlightPassengerDataListCacheSource flightPassengerDataListCacheSource,
                                         FlightPassengerDataListDBSource flightPassengerDataListDBSource,
                                         FlightSavedPassengerDataListCloudSource flightSavedPassengerDataListCloudSource) {
        super(flightPassengerDataListCacheSource, flightPassengerDataListDBSource, flightSavedPassengerDataListCloudSource);
        this.flightPassengerDataListDBSource = flightPassengerDataListDBSource;
        this.flightSavedPassengerDataListCloudSource = flightSavedPassengerDataListCloudSource;
    }

    public Observable<List<FlightPassengerDB>> getPassengerList(String passengerId) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put(FlightPassengerDataListDBSource.PASSENGER_ID, passengerId);

        return flightPassengerDataListDBSource.isDataAvailable()
                .flatMap(new Func1<Boolean, Observable<List<FlightPassengerDB>>>() {
                    @Override
                    public Observable<List<FlightPassengerDB>> call(Boolean aBoolean) {
                        if (aBoolean) {
                            return flightPassengerDataListDBSource.getData(params);
                        } else {
                            return getPassengerListFromCloud();
                        }
                    }
                });
    }

    public Observable<Boolean> updateIsSelected(String passengerId, int isSelected) {
        return flightPassengerDataListDBSource.updateIsSelected(passengerId, isSelected);
    }

    public Observable<Boolean> deleteAllListPassenger() {
        return flightPassengerDataListDBSource.deleteAll();
    }

    public Observable<Response<String>> deletePassenger(DeletePassengerRequest deletePassengerRequest) {
        return flightSavedPassengerDataListCloudSource.deletePassenger(deletePassengerRequest);
    }

    private Observable<List<FlightPassengerDB>> getPassengerListFromCloud() {
        return flightSavedPassengerDataListCloudSource.getData(null)
                .flatMap(new Func1<List<SavedPassengerEntity>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<SavedPassengerEntity> savedPassengerEntities) {
                        return flightPassengerDataListDBSource.insertAll(savedPassengerEntities);
                    }
                })
                .flatMap(new Func1<Boolean, Observable<List<FlightPassengerDB>>>() {
                    @Override
                    public Observable<List<FlightPassengerDB>> call(Boolean aBoolean) {
                        return flightPassengerDataListDBSource.getData(null);
                    }
                });
    }
}
