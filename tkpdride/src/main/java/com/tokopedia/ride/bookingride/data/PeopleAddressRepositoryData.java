package com.tokopedia.ride.bookingride.data;

import com.tokopedia.ride.bookingride.data.entity.PeopleAddressResponse;
import com.tokopedia.ride.bookingride.domain.PeopleAddressRepository;
import com.tokopedia.ride.bookingride.domain.model.PeopleAddressWrapper;

import java.util.Map;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by alvarisi on 4/6/17.
 */

public class PeopleAddressRepositoryData implements PeopleAddressRepository {
    private PeopleAddressDataStoreFactory mPeopleAddressDataStoreFactory;

    public PeopleAddressRepositoryData(PeopleAddressDataStoreFactory mPeopleAddressDataStoreFactory) {
        this.mPeopleAddressDataStoreFactory = mPeopleAddressDataStoreFactory;
    }

    @Override
    public Observable<PeopleAddressWrapper> getAddresses(final boolean isFirst, Map<String, Object> params) {
        PeopleAddressDataStore dataStore = mPeopleAddressDataStoreFactory.create(isFirst);
        return dataStore.getPeopleAddress(params)
                .doOnNext(new Action1<PeopleAddressResponse>() {
                    @Override
                    public void call(PeopleAddressResponse peopleAddressResponse) {
                        if (isFirst){
                            PeopleAddressCache peopleAddressCache = new PeopleAddressCacheImpl();
                            peopleAddressCache.put(peopleAddressResponse);
                        }
                    }
                })
                .map(new PeopleAddressResponseMapper());
    }
}
