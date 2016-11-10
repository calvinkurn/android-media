package com.tokopedia.core.selling.model;

import org.parceler.Parcel;

/**
 * Created by Toped10 on 5/11/2016.
 */
@Parcel
public class ModelParamSelling {
    public static final String ORDER_ID = "order_id";
    public static final String ACTION_TYPE = "action_type";
    public static final String SHIPMENT_ID = "shipment_id";
    public static final String SHIPMENT_NAME = "shipment_name";
    public static final String SHIPPING_REF = "shipping_ref";
    public static final String SP_ID = "sp_id";
    public static final String REASON = "reason";
    public static final String QTY_ACCEPT = "qty_accept";
    public static final String LIST_PRODUCT_ID = "list_product_id";

    // GENERIC PARAMETER
    String orderId;
    String reason;
    int position;
    String actionType;

    // SPECIFICS
    // order
    String qtyAccept;
    String estShipping;
    String listProductId;

    // shipping
    String refNum;
    String shipmentId;
    String shipmentName;
    String spId;



    public String getEstShipping() {
        return estShipping;
    }

    public void setEstShipping(String estShipping) {
        this.estShipping = estShipping;
    }

    public String getListProductId() {
        return listProductId;
    }

    public void setListProductId(String listProductId) {
        this.listProductId = listProductId;
    }

    public String getQtyAccept() {
        return qtyAccept;
    }

    public void setQtyAccept(String qtyAccept) {
        this.qtyAccept = qtyAccept;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public static String getShippingRef() {
        return SHIPPING_REF;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }
}
