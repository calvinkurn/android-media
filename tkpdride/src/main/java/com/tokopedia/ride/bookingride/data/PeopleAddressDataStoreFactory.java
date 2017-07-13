package com.tokopedia.ride.bookingride.data;

/**
 * Created by alvarisi on 4/6/17.
 */

public class PeopleAddressDataStoreFactory {
    private PeopleAddressApi mPeopleAddressApi;
    private PeopleAddressCache mPeopleAddressCache;

    public PeopleAddressDataStoreFactory(PeopleAddressApi mPeopleAddressApi) {
        this.mPeopleAddressApi = mPeopleAddressApi;
        this.mPeopleAddressCache = new PeopleAddressCacheImpl();
    }

    public PeopleAddressDataStore createCloudDataStore() {
        return new CloudPeopleAddressDataStore(mPeopleAddressApi);
    }

    public PeopleAddressDataStore create(boolean isFirst) {
        if (isFirst && mPeopleAddressCache.isCached()){
            return new DiskPeopleAddressDataStore();
        }else {
            return createCloudDataStore();
        }
    }
}
