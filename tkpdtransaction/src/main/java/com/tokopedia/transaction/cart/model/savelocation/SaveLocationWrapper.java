package com.tokopedia.transaction.cart.model.savelocation;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author by alvarisi on 11/3/2016.
 */

public class SaveLocationWrapper {
    private String addressId;
    private String latitude;
    private String longitude;
    private SaveLocationData data;

    public SaveLocationWrapper() {
    }

    public TKPDMapParam<String, String> getParams() {
        TKPDMapParam<String, String> map = new TKPDMapParam<>();
        map.put("address_id", this.addressId);
        map.put("latitude", this.latitude);
        map.put("longitude", this.longitude);
        map.put("act", "edit_address");
        map.put("is_from_cart", "1");
        return map;
    }

    public SaveLocationData getData() {
        return data;
    }

    public void setData(SaveLocationData data) {
        this.data = data;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
