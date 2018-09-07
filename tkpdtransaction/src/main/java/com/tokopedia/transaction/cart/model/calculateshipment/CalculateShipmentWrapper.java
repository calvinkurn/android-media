package com.tokopedia.transaction.cart.model.calculateshipment;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author by alvarisi on 11/3/16.
 */

public class CalculateShipmentWrapper {
    private static final String PARAM_SP_ID = "sp_id";
    private static final String PARAM_ADDRESS_ID = "address_id";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_WEIGHT = "weight";
    private static final String PARAM_DO = "do";
    private static final String PARAM_RECALCULATE = "recalculate";

    private String mAddressId;
    private String mShopId;
    private String mWeight;
    private String mDo = "calculate_address_shipping";
    private String mRecalculate = "1";
    private String mShippingId;

    public TKPDMapParam<String, String> getParams() {
        TKPDMapParam<String, String> map = new TKPDMapParam<>();
        map.put(PARAM_SP_ID, this.mShippingId);
        map.put(PARAM_ADDRESS_ID, this.mAddressId);
        map.put(PARAM_SHOP_ID, this.mShopId);
        map.put(PARAM_WEIGHT, this.mWeight);
        map.put(PARAM_DO, this.mDo);
        map.put(PARAM_RECALCULATE, this.mRecalculate);
        return map;
    }

    public void setAddressId(String addressId) {
        this.mAddressId = addressId;
    }

    public void setShopId(String shopId) {
        this.mShopId = shopId;
    }

    public void setWeight(String weight) {
        this.mWeight = weight;
    }

    public void setShippingId(String mShippingId) {
        this.mShippingId = mShippingId;
    }
}
