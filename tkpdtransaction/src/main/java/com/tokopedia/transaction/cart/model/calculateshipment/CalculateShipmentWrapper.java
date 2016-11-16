package com.tokopedia.transaction.cart.model.calculateshipment;


import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author by alvarisi on 11/3/16.
 */

public class CalculateShipmentWrapper {
    private String mAddressId;
    private String mShopId;
    private String mWeight;
    private String mDo = "calculate_address_shipping";
    private String mRecalculate = "1";
    private CalculateShipmentData mCalculateShipmentData;

    public TKPDMapParam<String, String> getParams() {
        TKPDMapParam<String, String> map = new TKPDMapParam<>();
        map.put("address_id", this.mAddressId);
        map.put("shop_id", this.mShopId);
        map.put("weight", this.mWeight);
        map.put("do", this.mDo);
        map.put("recalculate", this.mRecalculate);

        return (TKPDMapParam<String, String>) AuthUtil.generateParams(MainApplication.getAppContext(), map);
    }

    public CalculateShipmentData getData() {
        return mCalculateShipmentData;
    }

    public void setData(CalculateShipmentData mCalculateShipmentData) {
        this.mCalculateShipmentData = mCalculateShipmentData;
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
}
