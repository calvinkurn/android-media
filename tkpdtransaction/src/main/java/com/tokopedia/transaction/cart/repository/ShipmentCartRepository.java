package com.tokopedia.transaction.cart.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.shipmentcart.EditShipmentCart;
import com.tokopedia.transaction.cart.repository.entity.EditShipmentEntity;
import com.tokopedia.transaction.cart.repository.entity.ShipmentEntity;
import com.tokopedia.transaction.cart.repository.entity.mapper.ShipmentEntityDataMapper;
import com.tokopedia.transaction.cart.repository.source.CloudShipmentCartSource;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 11/30/16.
 */

public class ShipmentCartRepository implements IShipmentCartRepository {
    private CloudShipmentCartSource cloudSource;
    private ShipmentEntityDataMapper mapper;

    public ShipmentCartRepository() {
        cloudSource = new CloudShipmentCartSource();
        mapper = new ShipmentEntityDataMapper();
    }

    @Override
    public Observable<List<Shipment>> shipments(TKPDMapParam<String, String> param) {
        return cloudSource.shipments(param).map(new Func1<List<ShipmentEntity>, List<Shipment>>() {
            @Override
            public List<Shipment> call(List<ShipmentEntity> shipmentEntities) {
                return mapper.transform(shipmentEntities);
            }
        });
    }

    @Override
    public Observable<EditShipmentCart> editShipment(TKPDMapParam<String, String> param) {
        return cloudSource.editShipment(param).map(new Func1<EditShipmentEntity, EditShipmentCart>() {
            @Override
            public EditShipmentCart call(EditShipmentEntity editShipmentEntity) {
                return mapper.transform(editShipmentEntity);
            }
        });
    }
}
