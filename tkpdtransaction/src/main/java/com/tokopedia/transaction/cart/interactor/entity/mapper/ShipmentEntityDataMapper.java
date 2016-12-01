package com.tokopedia.transaction.cart.interactor.entity.mapper;

import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.calculateshipment.ShipmentPackage;
import com.tokopedia.transaction.cart.model.shipmentcart.EditShipmentCart;
import com.tokopedia.transaction.cart.interactor.entity.EditShipmentEntity;
import com.tokopedia.transaction.cart.interactor.entity.ShipmentEntity;
import com.tokopedia.transaction.cart.interactor.entity.ShipmentPackageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  by alvarisi on 11/30/16.
 */

public class ShipmentEntityDataMapper {
    public List<Shipment> transform(List<ShipmentEntity> shipmentEntities) {
        List<Shipment> shipments = new ArrayList<>();
        if (shipmentEntities != null){
            Shipment shipment;
            for (ShipmentEntity shipmentEntity : shipmentEntities){
                shipment = transform(shipmentEntity);
                if (shipment != null){
                    shipments.add(shipment);
                }
            }
        };
        return shipments;
    }

    public Shipment transform(ShipmentEntity shipmentEntity) {
        Shipment shipment = null;
        if (shipmentEntity != null) {
            shipment = new Shipment();
            shipment.setShipmentId(shipmentEntity.getShipmentId());
            shipment.setShipmentName(shipmentEntity.getShipmentName());
            shipment.setShipmentAvailable(shipmentEntity.getShipmentAvailable());
            List<ShipmentPackage> shipmentPackages = new ArrayList<>();
            ShipmentPackage shipmentPackage;
            for (ShipmentPackageEntity shipmentPackageEntity : shipmentEntity.getShipmentPackageEntity()) {
                shipmentPackage = transform(shipmentPackageEntity);
                if (shipmentPackage != null) {
                    shipmentPackages.add(shipmentPackage);
                }
            }
            shipment.setShipmentPackage(shipmentPackages);
        }
        return shipment;
    }

    private ShipmentPackage transform(ShipmentPackageEntity shipmentPackageEntity) {
        ShipmentPackage shipmentPackage = null;
        if (shipmentPackageEntity != null) {
            shipmentPackage = new ShipmentPackage();
            shipmentPackage.setShipmentId(shipmentPackageEntity.getShipmentId());
            shipmentPackage.setName(shipmentPackageEntity.getName());
            shipmentPackage.setIsShowMap(shipmentPackageEntity.getIsShowMap());
            shipmentPackage.setPackageAvailable(shipmentPackageEntity.getPackageAvailable());
            shipmentPackage.setPrice(shipmentPackageEntity.getPrice());
            shipmentPackage.setShipmentPackageId(shipmentPackageEntity.getShipmentPackageId());
        }
        return shipmentPackage;
    }

    public EditShipmentCart transform(EditShipmentEntity editShipmentEntity) {
        EditShipmentCart editShipmentCart = new EditShipmentCart();
        if (editShipmentEntity != null){
            editShipmentCart.setMessage(editShipmentEntity.getMessage());
            editShipmentCart.setStatus(editShipmentEntity.getStatus());
        }
        return null;
    }
}
