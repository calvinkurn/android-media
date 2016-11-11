package com.tokopedia.seller.selling.orderReject.model;

import org.parceler.Parcel;

/**
 * Created by Toped10 on 6/3/2016.
 */
@Parcel(parcelsIndex = false)
public class ModelEditDescription {
    public static final String MODEL_EDIT_DESCRIPTION_KEY = "model_edit_description_key";
    public static final String USER_ID = "user_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_DESCRIPTION = "product_description";

    String product_description;
    String product_id;
    String user_id;

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
