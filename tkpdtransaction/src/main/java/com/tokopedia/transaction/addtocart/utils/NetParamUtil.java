package com.tokopedia.transaction.addtocart.utils;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.addtocart.model.OrderData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angga.Prasetiyo on 22/03/2016.
 */
public class NetParamUtil {
    private static final String TAG = NetParamUtil.class.getSimpleName();

    public static TKPDMapParam<String, String> paramCalculateCart(String doAction,
                                                                  OrderData orderData) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("address_id", orderData.getAddress().getAddressId() + "");
        params.put("district_id", orderData.getAddress().getDistrictId() + "");
        params.put("do", doAction + "");
        params.put("product_id", orderData.getProductId() + "");
        params.put("postal_code", orderData.getAddress().getPostalCode() + "");
        params.put("qty", orderData.getQuantity() + "");
        params.put("shop_id", orderData.getShopId() + "");
        params.put("weight", orderData.getWeight() + "");
        return params;
    }

    public static Map<String, String> paramAddToCart(OrderData orderData) {
        Map<String, String> params = new HashMap<>();
        params.put("address_city", orderData.getAddress().getCityId());
        params.put("address_district", orderData.getAddress().getDistrictId());
        params.put("address_id", orderData.getAddress().getAddressId());
        params.put("address_name", orderData.getAddress().getAddressName());
        params.put("address_postal_code", orderData.getAddress().getPostalCode());
        params.put("address_province", orderData.getAddress().getProvinceId());
        params.put("address_street", orderData.getAddress().getAddressStreet());
        params.put("insurance", orderData.getInsurance());
        params.put("notes", orderData.getNotes());
        params.put("product_id", orderData.getProductId());
        params.put("quantity", orderData.getQuantity() + "");
        params.put("receiver_name", orderData.getAddress().getReceiverName());
        params.put("receiver_phone", orderData.getAddress().getReceiverPhone());
        params.put("shipping_id", orderData.getShipment());
        params.put("shipping_product", orderData.getShipmentPackage());

        return params;
    }
}
