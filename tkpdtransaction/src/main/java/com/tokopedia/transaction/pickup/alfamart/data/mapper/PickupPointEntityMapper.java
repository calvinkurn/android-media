package com.tokopedia.transaction.pickup.alfamart.data.mapper;

import com.tokopedia.transaction.pickup.alfamart.data.entity.DataEntity;
import com.tokopedia.transaction.pickup.alfamart.data.entity.PickupPointResponseEntity;
import com.tokopedia.transaction.pickup.alfamart.data.entity.StoreEntity;
import com.tokopedia.transaction.pickup.alfamart.domain.model.Data;
import com.tokopedia.transaction.pickup.alfamart.domain.model.PickupPointResponse;
import com.tokopedia.transaction.pickup.alfamart.domain.model.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class PickupPointEntityMapper {

    public PickupPointResponse transform(PickupPointResponseEntity entity) {
        PickupPointResponse pickupPointResponse = null;
        if (entity != null) {
            pickupPointResponse = new PickupPointResponse();
            pickupPointResponse.setData(transform(entity.getData()));
        }
        return pickupPointResponse;
    }

    public Data transform(DataEntity entity) {
        Data data = null;
        if (entity != null) {
            data = new Data();
            data.setPage(entity.getPage());
            data.setNextPage(entity.isNextPage());
            data.setPrevPage(entity.isPrevPage());
            List<Store> stores = new ArrayList<>();
            for (StoreEntity storeEntity : entity.getStoreEntities()) {
                stores.add(transform(storeEntity));
            }
            data.setStores(stores);
        }
        return data;
    }

    public Store transform(StoreEntity entity) {
        Store store = null;
        if (entity != null) {
            store = new Store();
            store.setId(entity.getId());
            store.setAddress(entity.getAddress());
            store.setDistrictId(entity.getDistrictId());
            store.setGeolocation(entity.getGeolocation());
            store.setStoreCode(entity.getStoreCode());
            store.setStoreName(entity.getStoreName());
        }
        return store;
    }
}
