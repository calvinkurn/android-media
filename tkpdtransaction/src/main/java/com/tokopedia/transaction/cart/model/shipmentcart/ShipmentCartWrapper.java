package com.tokopedia.transaction.cart.model.shipmentcart;


import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author by alvarisi on 11/3/16.
 */

public class ShipmentCartWrapper {
    private String shopId;
    private String addressId;
    private String shipmentId;
    private String shipmentPackageId;
    private String oldAddressId;
    private String oldShipmentId;
    private String oldShipmentPackageId;
    private boolean isValidShipment = true;
    private boolean isValidShipmentPackage = true;
    private ShipmentCartData shipmentCartData;

    public ShipmentCartWrapper() {
    }

    public TKPDMapParam<String, String> getParams() {
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        maps.put("address_id", this.addressId);
        maps.put("old_address_id", this.oldAddressId);
        maps.put("old_shipment_id", this.oldShipmentId);
        maps.put("old_shipment_package_id", this.oldShipmentPackageId);
        maps.put("shipment_id", this.shipmentId);
        maps.put("shipment_package_id", this.shipmentPackageId);
        maps.put("shop_id", this.shopId);
        return maps;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentPackageId() {
        return shipmentPackageId;
    }

    public void setShipmentPackageId(String shipmentPackageId) {
        this.shipmentPackageId = shipmentPackageId;
    }

    public String getOldAddressId() {
        return oldAddressId;
    }

    public void setOldAddressId(String oldAddressId) {
        this.oldAddressId = oldAddressId;
    }

    public String getOldShipmentId() {
        return oldShipmentId;
    }

    public void setOldShipmentId(String oldShipmentId) {
        this.oldShipmentId = oldShipmentId;
    }

    public String getOldShipmentPackageId() {
        return oldShipmentPackageId;
    }

    public void setOldShipmentPackageId(String oldShipmentPackageId) {
        this.oldShipmentPackageId = oldShipmentPackageId;
    }

    public ShipmentCartData getShipmentCartData() {
        return shipmentCartData;
    }

    public void setShipmentCartData(ShipmentCartData shipmentCartData) {
        this.shipmentCartData = shipmentCartData;
    }
}
