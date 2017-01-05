package com.tokopedia.transaction.cart.interactor.data;

import com.tokopedia.transaction.cart.interactor.data.source.CloudShipmentCartDataStore;

/**
 * @author  by alvarisi on 1/4/17.
 */

public class ShipmentCartDataStoreFactory {
    public ShipmentCartDataStoreFactory() {
    }

    public IShipmentCartDataStore createCloudShipmentCartDataStore(){
        return new CloudShipmentCartDataStore();
    }

}