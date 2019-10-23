package com.tokopedia.seller.purchase.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData;
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData;
import com.tokopedia.transaction.common.data.order.OrderDetailData;
import com.tokopedia.transaction.common.data.order.OrderDetailItemData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.seller.purchase.utils.OrderDetailConstant.EVENT;
import static com.tokopedia.seller.purchase.utils.OrderDetailConstant.EVENT_ACTION;
import static com.tokopedia.seller.purchase.utils.OrderDetailConstant.EVENT_CATEGORY;
import static com.tokopedia.seller.purchase.utils.OrderDetailConstant.EVENT_LABEL;
import static com.tokopedia.seller.purchase.utils.OrderDetailConstant.VALUE_CLICK_ASK_BUYER;
import static com.tokopedia.seller.purchase.utils.OrderDetailConstant.VALUE_CLICK_ORDER;
import static com.tokopedia.seller.purchase.utils.OrderDetailConstant.VALUE_SALES_SHIPPING;
import static com.tokopedia.seller.purchase.utils.OrderDetailConstant.VALUE_SHOP_ORDER_PAGE;

import com.tokopedia.track.TrackApp;

/**
 * Temporary class to provide analytics in tkpdtransaction
 * Will be moved soon when refactoring
 * Will be merged with analytics module
 */
public class OrderDetailAnalytics {

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

    public void sendAnalyticsClickAskBuyer(String action, String label) {
        sendAnalytics(VALUE_CLICK_ASK_BUYER,
                VALUE_SHOP_ORDER_PAGE,
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
}
