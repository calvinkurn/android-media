package com.tokopedia.transaction.cart.interactor.data;

import com.tokopedia.transaction.cart.interactor.data.source.CloudShipmentCartDataStore;

/**
 * @author  by alvarisi on 1/4/17.
 */

class ShipmentCartDataStoreFactory {
    ShipmentCartDataStoreFactory() {
    }

    IShipmentCartDataStore createCloudShipmentCartDataStore() {
        return new CloudShipmentCartDataStore();
    }

}