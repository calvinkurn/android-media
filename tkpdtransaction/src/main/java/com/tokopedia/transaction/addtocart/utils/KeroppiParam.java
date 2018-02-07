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
        params.put(PRODUCT_INSURANCE, productDetail.getProductMustInsurance() == 1 ? "1" : "0");
        params.put(INSURANCE, "1");
        params.put(ORDER_VALUE, getRawPrice(productDetail.getProductPrice()));
        params.put(CAT_ID, productDetail.getProductCatId());

        return params;
    }

    private static String getRawPrice(String formattedPrice) {
        return formattedPrice.replace("Rp ", "").replace(".", "");
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

        params.put(CAT_ID, orderData.getCatId());
        params.put(INSURANCE, "1");
        params.put(PRODUCT_INSURANCE, isProductMustInsurance(orderData));
        String rawPrice = getRawPrice(orderData.getPriceTotal());
        params.put(ORDER_VALUE, rawPrice);

        return params;
    }

    private static String isProductMustInsurance(OrderData orderData) {
        if (orderData.getMustInsurance() == null) {
            return "0";
        } else {
            return String.valueOf(orderData.getMustInsurance());
        }
    }

    private static String generatePath(String districtID, String postalCode, String lat,
                                       String lng) {
        return districtID + SEPARATOR + postalCode + SEPARATOR + lat + CO_SEPARATOR + lng;
    }

    public static TKPDMapParam<String, String> paramsKeroCart(String token, String ut, CartItem cartItem) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        if (cartItem.getCartShipments() != null)
            params.put(NAMES, cartItem.getCartShipments().getShipmentCode());
        if (cartItem.getCartShop() != null) {
            params.put(ORIGIN, generatePath(cartItem.getCartShop().getAddressId()
                            + "", cartItem.getCartShop().getPostalCode(),
                    cartItem.getCartShop().getLatitude(),
                    cartItem.getCartShop().getLongitude()));
        }
        if (cartItem.getCartDestination() != null) {
            params.put(DESTINATION,
                    generatePath(cartItem.getCartDestination().getAddressDistrictId()
                                    + "", cartItem.getCartDestination().getAddressPostal(),
                            cartItem.getCartDestination().getLatitude(),
                            cartItem.getCartDestination().getLongitude()));
        }

        params.put(WEIGHT, cartItem.getCartTotalWeight());
        params.put(TYPE, TYPE_ANDROID);
        params.put(FROM, FROM_CLIENT);
        params.put(TOKEN, token);
        params.put(ORDER_VALUE, cartItem.getCartTotalProductPrice());
        params.put(CAT_ID, cartItem.getCartCatId());
        params.put(PRODUCT_INSURANCE, getProductInsurance(cartItem));
        params.put(UT, ut);
        params.put(INSURANCE, "1");

        return params;
    }

    private static String getProductInsurance(CartItem cartItem) {
        for (CartProduct cartProduct : cartItem.getCartProducts()) {
            if (cartProduct.getProductMustInsurance().equals("1")) {
                return "1";
            }
        }
        return "0";
    }

}
