package com.tokopedia.transaction.purchase.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.transaction.common.data.order.OrderDetailData;
import com.tokopedia.transaction.common.data.order.OrderDetailItemData;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCartMapData;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceProductCartMapData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.transaction.purchase.utils.OrderDetailConstant.EVENT;
import static com.tokopedia.transaction.purchase.utils.OrderDetailConstant.EVENT_ACTION;
import static com.tokopedia.transaction.purchase.utils.OrderDetailConstant.EVENT_CATEGORY;
import static com.tokopedia.transaction.purchase.utils.OrderDetailConstant.EVENT_LABEL;
import static com.tokopedia.transaction.purchase.utils.OrderDetailConstant.VALUE_CLICK_ORDER;
import static com.tokopedia.transaction.purchase.utils.OrderDetailConstant.VALUE_SALES_SHIPPING;
import com.tokopedia.track.TrackApp;

/**
 * Temporary class to provide analytics in tkpdtransaction
 * Will be moved soon when refactoring
 * Will be merged with analytics module
 */
public class OrderDetailAnalytics {

    private static final String ID = "id";
    private static final String CATEGORY = "deals";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String EVENT_TRANSACTION = "transaction";
    private static final String EVENT_CARTEGORY = "digital-deals";
    private static final String ACTION = "view purchase attempt";
    private static final String LABEL = "purchase attempt status";
    private static final String CURRENCY_CODE = "currencyCode";
    private static final String IDR = "IDR";
    private static final String QUANTITY = "quantity";
    private static final String REVENUE = "revenue";
    private static final String KEY_PRODUCTS = "products";
    private static final String KEY_PURCHASE = "purchase";

    private Context context;

    public OrderDetailAnalytics() {
    }

    private HashMap<String, Object> createEventMap(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(EVENT, event);
        eventMap.put(EVENT_CATEGORY, category);
        eventMap.put(EVENT_ACTION, action);
        eventMap.put(EVENT_LABEL, label);
        return eventMap;
    }

    public void sendAnalytics(String event, String category, String action, String label) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(createEventMap(event, category, action, label));
    }

    public void sendAnalyticsClickShipping(String action, String label) {
        sendAnalytics(VALUE_CLICK_ORDER,
                VALUE_SALES_SHIPPING,
                action, label);
    }

    public void sendAnalyticBuyAgain(OrderDetailData data) {
        EnhancedECommerceCartMapData enhancedECommerceCartMapData = new EnhancedECommerceCartMapData();
        for (OrderDetailItemData orderDetailItemData : data.getItemList()) {
            EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                    new EnhancedECommerceProductCartMapData();
            enhancedECommerceProductCartMapData.setProductName(orderDetailItemData.getItemName());
            enhancedECommerceProductCartMapData.setProductID(String.valueOf(orderDetailItemData.getProductId()));
            enhancedECommerceProductCartMapData.setPrice(String.valueOf(orderDetailItemData.getPriceUnformatted()));
            enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
            String categoryLevelStr = "";
            enhancedECommerceProductCartMapData.setCartId(data.getOrderId());
            enhancedECommerceProductCartMapData.setDimension45(data.getOrderId());
            enhancedECommerceProductCartMapData.setCategory(TextUtils.isEmpty(categoryLevelStr)
                    ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                    : categoryLevelStr);
            enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
            int qty = 0;
            try {
                qty = Integer.parseInt(orderDetailItemData.getItemQuantity());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            enhancedECommerceProductCartMapData.setQty(qty);
            enhancedECommerceProductCartMapData.setShopId(data.getShopId());
            enhancedECommerceProductCartMapData.setShopType(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceProductCartMapData.setShopName(data.getShopName());
            enhancedECommerceProductCartMapData.setCategoryId(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceProductCartMapData.setAttribution(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceProductCartMapData.setDimension38(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceProductCartMapData.setListName(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceProductCartMapData.setDimension40(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);

            enhancedECommerceCartMapData.addProduct(enhancedECommerceProductCartMapData.getProduct());
            enhancedECommerceCartMapData.setCurrencyCode("IDR");
            enhancedECommerceCartMapData.setAction(EnhancedECommerceCartMapData.ADD_ACTION);
        }

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        ConstantTransactionAnalytics.Key.EVENT, ConstantTransactionAnalytics.EventName.ADD_TO_CART,
                        ConstantTransactionAnalytics.Key.EVENT_CATEGORY, "my purchase list detail - mp",
                        ConstantTransactionAnalytics.Key.EVENT_ACTION, "click beli lagi order",
                        ConstantTransactionAnalytics.Key.EVENT_LABEL, "success",
                        ConstantTransactionAnalytics.Key.E_COMMERCE, enhancedECommerceCartMapData.getCartMap()
                )
        );
    }

    public void sendThankYouEvent(int entityProductId, String entityProductName, int totalTicketPrice, int quantity, String orderId) {
        Map<String, Object> products = new HashMap<>();
        Map<String, Object> purchase = new HashMap<>();
        Map<String, Object> ecommerce = new HashMap<>();

        products.put(ID, entityProductId);
        products.put(NAME, entityProductName);
        products.put(PRICE, totalTicketPrice);
        products.put("Category", CATEGORY);
        products.put(QUANTITY, quantity);

        purchase.put(ID, orderId);
        purchase.put(REVENUE, totalTicketPrice);

        ecommerce.put(CURRENCY_CODE, IDR);
        ecommerce.put(KEY_PRODUCTS, Collections.singletonList(products));
        ecommerce.put(KEY_PURCHASE, purchase);

        Map<String, Object> map = new HashMap<>();
        map.put("event", EVENT_TRANSACTION);
        map.put("eventCategory", EVENT_CARTEGORY);
        map.put("eventAction", ACTION);
        map.put("eventLabel", LABEL);
        map.put("ecommerce", ecommerce);
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(map);
    }
}
