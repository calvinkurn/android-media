package com.tokopedia.transaction.addtocart.utils;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.addtocart.model.OrderData;
import com.tokopedia.transaction.addtocart.model.responseatcform.Destination;
import com.tokopedia.transaction.addtocart.model.responseatcform.ProductDetail;
import com.tokopedia.transaction.addtocart.model.responseatcform.Shop;

/**
 * @author anggaprasetiyo on 11/18/16.
 */

public class KeroppiParam {
    public static final String TAG = KeroppiParam.class.getSimpleName();

    private static final String NAMES = "names";
    private static final String ORIGIN = "origin";
    private static final String DESTINATION = "destination";
    private static final String WEIGHT = "weight";
    private static final String TYPE = "type";
    private static final String FROM = "from";
    private static final String TOKEN = "token";
    private static final String UT = "ut";

    private static final String SEPARATOR = "|";
    private static final String CO_SEPARATOR = ",";
    private static final String TYPE_ANDROID = "android";
    private static final String FROM_CLIENT = "client";

    public static TKPDMapParam<String, String> paramsKero(Shop shop, Destination destination,
                                                          ProductDetail productDetail) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(NAMES, shop.getAvailShippingCode());
        params.put(ORIGIN, generatePath(shop.getOriginId()
                + "", shop.getOriginPostal(), shop.getLatitude(), shop.getLongitude()));
        params.put(DESTINATION, generatePath(destination.getDistrictId()
                        + "", destination.getPostalCode(), destination.getLatitude(),
                destination.getLongitude()));
        params.put(WEIGHT, productDetail.getProductWeight());
        params.put(TYPE, TYPE_ANDROID);
        params.put(FROM, FROM_CLIENT);
        params.put(TOKEN, shop.getToken());
        params.put(UT, shop.getUt() + "");

        return params;
    }

    public static TKPDMapParam<String, String> paramsKeroOrderData(OrderData orderData) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(NAMES, orderData.getShop().getAvailShippingCode());
        params.put(ORIGIN, generatePath(orderData.getShop().getOriginId()
                        + "", orderData.getShop().getOriginPostal(),
                orderData.getShop().getLatitude(),
                orderData.getShop().getLongitude()));
        params.put(DESTINATION, generatePath(orderData.getAddress().getDistrictId()
                        + "", orderData.getAddress().getPostalCode(),
                orderData.getAddress().getLatitude(),
                orderData.getAddress().getLongitude()));
        params.put(WEIGHT, orderData.getWeight());
        params.put(TYPE, TYPE_ANDROID);
        params.put(FROM, FROM_CLIENT);
        params.put(TOKEN, orderData.getShop().getToken());
        params.put(UT, orderData.getShop().getUt() + "");
        return params;
    }

    private static String generatePath(String districtID, String postalCode, String lat,
                                       String lng) {
        return districtID + SEPARATOR + postalCode + SEPARATOR + lat + CO_SEPARATOR + lng;
    }
}
