package com.tokopedia.transaction.addtocart.utils;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.transaction.addtocart.model.OrderData;
import com.tokopedia.transaction.addtocart.model.responseatcform.Destination;
import com.tokopedia.transaction.addtocart.model.responseatcform.ProductDetail;
import com.tokopedia.transaction.addtocart.model.responseatcform.Shop;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;

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
    private static final String CAT_ID = "cat_id";
    private static final String ORDER_VALUE = "order_value";
    private static final String TOKEN = "token";
    private static final String UT = "ut";
    private static final String PRODUCT_INSURANCE = "product_insurance";
    private static final String INSURANCE = "insurance";
    private static final String APP_VERSION = "app_version";

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
        params.put(APP_VERSION, GlobalConfig.VERSION_NAME);
        return params;
    }

    private static String generatePath(String districtID, String postalCode, String lat,
                                       String lng) {
        return districtID + SEPARATOR + postalCode + SEPARATOR + lat + CO_SEPARATOR + lng;
    }

    public static TKPDMapParam<String, String> paramsKeroCart(String token, CartItem cartItem) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(NAMES, cartItem.getCartShipments().getShipmentCode());
        params.put(ORIGIN, generatePath(cartItem.getCartShop().getAddressId()
                + "", cartItem.getCartShop().getPostalCode(),
                cartItem.getCartShop().getLatitude(),
                cartItem.getCartShop().getLongitude()));
        params.put(DESTINATION, generatePath(cartItem.getCartDestination().getAddressDistrictId()
                        + "", cartItem.getCartDestination().getAddressPostal(),
                cartItem.getCartDestination().getLatitude(),
                cartItem.getCartDestination().getLongitude()));
        params.put(WEIGHT, cartItem.getCartTotalWeight());
        params.put(TYPE, TYPE_ANDROID);
        params.put(FROM, FROM_CLIENT);
        params.put(TOKEN, token);
        params.put(ORDER_VALUE, cartItem.getCartTotalProductPrice());
        params.put(CAT_ID, cartItem.getCartCatId());
        params.put(PRODUCT_INSURANCE, setInsurance(cartItem));
        params.put(INSURANCE, "1");

        return params;
    }

    public static TKPDMapParam<String, String> keroCartMock() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        //params.put(NAMES, cartItem.getCartShipments().getShipmentName());
        params.put(NAMES, "gojek");
        params.put(ORIGIN, generatePath("5573"
                        + "", "11410",
                "-6.1761317",
                "106.8206753"));
        params.put(DESTINATION, generatePath("5573"
                        + "", "11410",
                "2.1941499",
                "98.9739881"));
        params.put(WEIGHT, "0.4");
        params.put(TYPE, TYPE_ANDROID);
        params.put(FROM, FROM_CLIENT);
        params.put(TOKEN, "Tokopedia+Kero:GSOwDwy3479CJdi8AzE4ed6soww=");
        params.put(UT, "1464591486");
        params.put("cat_id", "180,10,50");
        params.put("order_value", "1500000");
        params.put("insurance", "1");
        params.put(PRODUCT_INSURANCE, "1");
        params.put(INSURANCE, "1");
        return params;
    }

    private static String temporaryCourierNameManipulator(CartItem cartItem) {
        String courierCode = cartItem.getCartShipments().getShipmentName().toLowerCase();
        if(cartItem.getCartShipments().getShipmentName().contains("pos"))
            return "pos";
        else return courierCode;
    }

    private static String setInsurance (CartItem cartItem) {
        if (cartItem.getCartForceInsurance() == 1) {
            return "1";
        } else return "0";
    }
}
