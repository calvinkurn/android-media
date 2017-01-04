package com.tokopedia.transaction.cart.interactor.domain;

import com.tokopedia.transaction.cart.interactor.data.entity.EditShipmentEntity;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationData;
import com.tokopedia.transaction.cart.model.shipmentcart.EditShipmentCart;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author by alvarisi on 1/4/17.
 */

public interface IShipmentCartRepository {
    Observable<List<Shipment>> shipments(Map<String, String> param);

    Observable<EditShipmentCart> editShipment(Map<String, String> param);

    Observable<SaveLocationData> editLocation(Map<String, String> param);
}
