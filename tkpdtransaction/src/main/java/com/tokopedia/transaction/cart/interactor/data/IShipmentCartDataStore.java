package com.tokopedia.transaction.cart.interactor.data;

import com.tokopedia.transaction.cart.interactor.data.entity.EditShipmentEntity;
import com.tokopedia.transaction.cart.interactor.data.entity.ShipmentEntity;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationData;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author by alvarisi on 1/4/17.
 */

public interface IShipmentCartDataStore {
    Observable<List<ShipmentEntity>> shipments(Map<String, String> param);

    Observable<EditShipmentEntity> editShipment(Map<String, String> param);

    Observable<SaveLocationData> editLocation(Map<String, String> param);
}
