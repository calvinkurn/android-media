package com.tokopedia.transaction.cart.interactor.data;

import com.tokopedia.transaction.cart.interactor.data.entity.EditShipmentEntity;
import com.tokopedia.transaction.cart.interactor.data.entity.ShipmentEntity;
import com.tokopedia.transaction.cart.interactor.data.mapper.ShipmentEntityDataMapper;
import com.tokopedia.transaction.cart.interactor.domain.IShipmentCartRepository;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationData;
import com.tokopedia.transaction.cart.model.shipmentcart.EditShipmentCart;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 1/4/17.
 */

public class ShipmentCartDataRepository implements IShipmentCartRepository {
    private final ShipmentEntityDataMapper mShipmentEntityDataMapper;
    private final ShipmentCartDataStoreFactory mShipmentCartDataStoreFactory;

    public ShipmentCartDataRepository() {
        mShipmentEntityDataMapper = new ShipmentEntityDataMapper();
        mShipmentCartDataStoreFactory = new ShipmentCartDataStoreFactory();
    }

    @Override
    public Observable<List<Shipment>> shipments(Map<String, String> param) {
        return mShipmentCartDataStoreFactory.createCloudShipmentCartDataStore().shipments(param)
                .map(new Func1<List<ShipmentEntity>, List<Shipment>>() {
                    @Override
                    public List<Shipment> call(List<ShipmentEntity> shipmentEntities) {
                        return mShipmentEntityDataMapper.transform(shipmentEntities);
                    }
                });
    }

    @Override
    public Observable<EditShipmentCart> editShipment(Map<String, String> param) {
        return mShipmentCartDataStoreFactory.createCloudShipmentCartDataStore().editShipment(param)
                .map(new Func1<EditShipmentEntity, EditShipmentCart>() {
                    @Override
                    public EditShipmentCart call(EditShipmentEntity editShipmentEntity) {
                        return mShipmentEntityDataMapper.transform(editShipmentEntity);
                    }
                });
    }

    @Override
    public Observable<SaveLocationData> editLocation(Map<String, String> param) {
        return mShipmentCartDataStoreFactory.createCloudShipmentCartDataStore().editLocation(param);
    }
}
