package com.tokopedia.shop.etalase.domain.model;

import android.text.TextUtils;

import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;

import java.util.HashMap;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseRequestModel {

    private static final String SHOW_ALL = "1";

    private String shopId;

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public HashMap<String, String> getHashMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!TextUtils.isEmpty(shopId)) {
            hashMap.put(ShopCommonParamApiConstant.SHOP_ID, shopId);
        }
        hashMap.put(ShopCommonParamApiConstant.SHOW_ALL, SHOW_ALL);
        return hashMap;
    }
}