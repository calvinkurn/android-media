package com.tokopedia.transaction.cart.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;

import java.util.List;

import rx.Observable;

/**
 * @author  by alvarisi on 11/30/16.
 */

public interface IShipmentCartRepository {
    Observable<List<Shipment>> shipments(TKPDMapParam<String, String> param);
}
