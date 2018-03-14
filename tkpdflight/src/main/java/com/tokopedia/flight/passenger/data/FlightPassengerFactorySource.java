package com.tokopedia.flight.passenger.data;

import com.tokopedia.flight.passenger.data.cloud.FlightSavedPassengerDataListCloudSource;
import com.tokopedia.flight.passenger.data.cloud.entity.SavedPassengerEntity;
import com.tokopedia.flight.passenger.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.passenger.data.db.FlightPassengerDataListDbSource;
import com.tokopedia.flight.passenger.data.db.model.FlightPassengerDb;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 28/02/18.
 */

public class FlightPassengerFactorySource {

    FlightPassengerDataListDbSource flightPassengerDataListDbSource;
    FlightSavedPassengerDataListCloudSource flightSavedPassengerDataListCloudSource;

    @Inject
    public FlightPassengerFactorySource(FlightPassengerDataListDbSource flightPassengerDataListDbSource,
                                        FlightSavedPassengerDataListCloudSource flightSavedPassengerDataListCloudSource) {
        this.flightPassengerDataListDbSource = flightPassengerDataListDbSource;
        this.flightSavedPassengerDataListCloudSource = flightSavedPassengerDataListCloudSource;
    }

    public Observable<List<FlightPassengerDb>> getPassengerList(String passengerId) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put(FlightPassengerDataListDbSource.PASSENGER_ID, passengerId);

        return flightPassengerDataListDbSource.isDataAvailable()
                .flatMap(new Func1<Boolean, Observable<List<FlightPassengerDb>>>() {
                    @Override
                    public Observable<List<FlightPassengerDb>> call(Boolean aBoolean) {
                        if (aBoolean) {
                            return flightPassengerDataListDbSource.getData(params);
                        } else {
                            return getPassengerListFromCloud();
                        }
                    }
                });
    }

    public Observable<FlightPassengerDb> getSinglePassenger(String passengerId) {
        return flightPassengerDataListDbSource.getSingleData(passengerId);
    }

    public Observable<Boolean> updateIsSelected(String passengerId, int isSelected) {
        return flightPassengerDataListDbSource.updateIsSelected(passengerId, isSelected);
    }

    public Observable<Boolean> deleteAllListPassenger() {
        return flightPassengerDataListDbSource.deleteAll();
    }

    public Observable<Response<Object>> deletePassenger(DeletePassengerRequest deletePassengerRequest, String idempotencyKey) {
        return flightSavedPassengerDataListCloudSource.deletePassenger(deletePassengerRequest, idempotencyKey);
    }

    private Observable<List<FlightPassengerDb>> getPassengerListFromCloud() {
        return flightPassengerDataListDbSource.deleteAll()
                .flatMap(new Func1<Boolean, Observable<List<SavedPassengerEntity>>>() {
                    @Override
                    public Observable<List<SavedPassengerEntity>> call(Boolean aBoolean) {
                        return flightSavedPassengerDataListCloudSource.getData(null);
                    }
                })
                .flatMap(new Func1<List<SavedPassengerEntity>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<SavedPassengerEntity> savedPassengerEntities) {
                        return flightPassengerDataListDbSource.insertAll(savedPassengerEntities);
                    }
                })
                .flatMap(new Func1<Boolean, Observable<List<FlightPassengerDb>>>() {
                    @Override
                    public Observable<List<FlightPassengerDb>> call(Boolean aBoolean) {
                        return flightPassengerDataListDbSource.getData(null);
                    }
                });

    }
}
