package com.tokopedia.seller.selling.orderReject.model;

import org.parceler.Parcel;

/**
 * Created by Toped10 on 6/3/2016.
 */
@Parcel(parcelsIndex = false)
public class ModelRejectOrder {
    public static final String MODEL_REJECT_ORDER_KEY = "model_reject_order_key";

    public static final String ACTION_TYPE = "action_type";
    public static final String EST_SHIPPING = "est_shipping";
    public static final String LIST_PRODUCT_ID = "list_product_id";
    public static final String ORDER_ID = "order_id";
    public static final String QTY_ACCEPT = "qty_accept";
    public static final String REASON = "reason";
    public static final String CLOSED_NOTE = "closed_note";
    public static final String CLOSE_END = "close_end";
    public static final String REASON_CODE = "reason_code";

    String action_type;
    String est_shipping;
    String list_product_id;
    String order_id;
    String qty_accept;
    String reason;
    String user_id;
    String closed_note;
    String close_end;
    String reason_code;
    int position;

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getEst_shipping() {
        return est_shipping;
    }

    public void setEst_shipping(String est_shipping) {
        this.est_shipping = est_shipping;
    }

    public String getList_product_id() {
        return list_product_id;
    }

    public void setList_product_id(String list_product_id) {
        this.list_product_id = list_product_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getQty_accept() {
        return qty_accept;
    }

    public void setQty_accept(String qty_accept) {
        this.qty_accept = qty_accept;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getClosed_note() {
        return closed_note;
    }

    public void setClosed_note(String closed_note) {
        this.closed_note = closed_note;
    }

    public String getClose_end() {
        return close_end;
    }

    public void setClose_end(String close_end) {
        this.close_end = close_end;
    }

    public String getReason_code() {
        return reason_code;
    }

    public void setReason_code(String reason_code) {
        this.reason_code = reason_code;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
