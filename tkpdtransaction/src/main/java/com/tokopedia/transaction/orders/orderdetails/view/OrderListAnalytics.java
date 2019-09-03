package com.tokopedia.transaction.orders.orderdetails.view;

import javax.inject.Inject;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.Gson;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.transaction.common.sharedata.buyagain.Datum;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.data.MetaDataInfo;
import com.tokopedia.transaction.orders.orderdetails.data.ShopInfo;
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListAnalytics {

    private static final String PRODUCT_EVENT_NAME = "clickPurchaseList";
    private static final String PRODUCT_EVENT_CATEGORY = "my purchase list - mp";
    private static final String PRODUCT_EVENT_DETAIL = "my purchase list detail - mp";
    private static final String PRODUCT_EVENT_ACTION = "click product";
    private static final String FILTER_EVENT_ACTION = "click quick filter";
    private static final String DATE_EVENT_ACTION = "submit date filter";
    private static final String SEARCH_EVENT_ACTION = "submit search";
    private static final String INVOICE_EVENT_ACTION = "click view invoice";
    private static final String LOAD_MORE_EVENT_ACTION = "scroll load more";
    private static final String EVENT_ADD_TO_CART = "addToCart";

    private static final String ID = "id";
    private static final String CATEGORY = "deals";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String EVENT_TRANSACTION = "transaction";
    private static final String EVENT_CARTEGORY = "digital-deals";
    private static final String EVENT_CATEGORY_BUY_AGAIN = "my purchase list detail - mp";
    private static final String EVENT_ACTION_BUY_AGAIN = "click beli lagi order";
    private static final String EVENT_LABEL_BUY_AGAIN_SUCCESS = "success";
    private static final String EVENT_LABEL_BUY_AGAIN_FAILURE = "failure";
    private static final String KEY_ADD = "add";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_SHOP_ID = "shop_id";
    private static final String KEY_SHOP_TYPE= "shop_type";
    private static final String KEY_SHOP_NAME= "shop_name";
    private static final String KEY_DIMENSION_45 = "dimension45";
    private static final String KEY_DIMENSION_40 = "dimension40";
    private static final String KEY_DIMENSION_38 = "dimension38";
    private static final String ACTION = "view purchase attempt";
    private static final String LABEL = "purchase attempt status";
    private static final String CURRENCY_CODE = "currencyCode";
    private static final String IDR = "IDR";
    private static final String QUANTITY = "quantity";
    private static final String REVENUE = "revenue";
    private static final String AFFILIATION = "affiliation";
    private static final String BRAND = "brand";
    private static final String VARIANT = "variant";
    private static final String SHIPPING = "shipping";
    private static final String TAX = "tax";
    private static final String COUPON_CODE = "coupon";
    private static final String KEY_PRODUCTS = "products";
    private static final String KEY_PURCHASE = "purchase";
    private static final String KEY_ACTION_FIELD = "actionField";
    private static final String SCREEN_NAME = "/digital/deals/thanks";
    private static final String ACTION_CLICK_SEE_BUTTON_ON_ATC_SUCCESS_TOASTER = "click lihat button on atc success toaster";
    private static final String NONE = "none/other";
    private List<Object> dataLayerList = new ArrayList<>();
    private String recomTitle;


    @Inject
    public OrderListAnalytics() {
    }

    public void sendProductClickEvent(String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, PRODUCT_EVENT_ACTION, eventLabel));
    }

    public void sendQuickFilterClickEvent(String filterLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, FILTER_EVENT_ACTION, filterLabel));
    }

    public void sendDateFilterClickEvent() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, DATE_EVENT_ACTION, ""));
    }

    public void sendSearchFilterClickEvent() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, SEARCH_EVENT_ACTION, ""));
    }

    public void sendViewInvoiceClickEvent() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, INVOICE_EVENT_ACTION, ""));
    }

    public void sendActionButtonClickEvent(String eventAction) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, eventAction, ""));
    }

    public void sendLoadMoreEvent(String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, LOAD_MORE_EVENT_ACTION, eventLabel));
    }

    public void sendThankYouEvent(int entityProductId, String entityProductName, int totalTicketPrice, int quantity, String brandName, String orderId) {
        Map<String, Object> products = new HashMap<>();
        Map<String, Object> purchase = new HashMap<>();
        Map<String, Object> ecommerce = new HashMap<>();
        Map<String, Object> actionField = new HashMap<>();

        products.put(ID, String.valueOf(entityProductId));
        products.put(NAME, entityProductName);
        products.put(PRICE, totalTicketPrice);
        products.put("Category", CATEGORY);
        products.put(QUANTITY, quantity);
        products.put(BRAND, brandName);
        products.put(COUPON_CODE, "none");
        products.put(VARIANT, "none");

        actionField.put(ID, String.valueOf(orderId));
        actionField.put(REVENUE, totalTicketPrice);
        actionField.put(AFFILIATION, brandName);
        actionField.put(SHIPPING, "0");
        actionField.put(TAX, "none");
        actionField.put(COUPON_CODE, "none");

        purchase.put(KEY_ACTION_FIELD, actionField);
        ecommerce.put(CURRENCY_CODE, IDR);
        ecommerce.put(KEY_PRODUCTS, Collections.singletonList(products));
        ecommerce.put(KEY_PURCHASE, purchase);

        Map<String, Object> map = new HashMap<>();
        map.put("event", EVENT_TRANSACTION);
        map.put("eventCategory", EVENT_CARTEGORY);
        map.put("eventAction", ACTION);
        map.put("eventLabel", brandName);
        map.put("currentSite", "tokopediadigital");
        map.put("ecommerce", ecommerce);
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(map);
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(SCREEN_NAME);
    }

    public void sendBuyAgainEvent(List<Items> items, ShopInfo shopInfo, List<Datum> responseBuyAgainList, boolean isSuccess) {
        ArrayList<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> add = new HashMap<>();
        Map<String, Object> ecommerce = new HashMap<>();
        Gson gson = new Gson();

        for (Items item : items) {
            MetaDataInfo metaDataInfo = gson.fromJson(item.getMetaData(), MetaDataInfo.class);
            HashMap<String, Object> product = new HashMap<>();
            product.put(ID, String.valueOf(item.getId()));
            product.put(NAME, metaDataInfo != null && metaDataInfo.getEntityProductName() != null
                    ? metaDataInfo.getEntityProductName() : item.getTitle());
            product.put(PRICE, item.getTotalPrice());
            product.put(KEY_CATEGORY, item.getCategory());
            product.put(QUANTITY, item.getQuantity());
            product.put(BRAND, metaDataInfo != null && metaDataInfo.getEntityBrandName() != null ? metaDataInfo.getEntityBrandName() : NONE);
            product.put(VARIANT, NONE);
            product.put(KEY_CATEGORY_ID, String.valueOf(item.getCategoryID()));
            product.put(KEY_SHOP_ID, String.valueOf(shopInfo.getShopId()));
            product.put(KEY_SHOP_NAME, shopInfo.getShopName());
            product.put(KEY_SHOP_TYPE, NONE);
            String cartId = NONE;
            for(Datum datum : responseBuyAgainList)
                if (datum.getProductId() == item.getId()) {
                    cartId = String.valueOf(datum.getCartId());
                    break;
                }
            product.put(KEY_DIMENSION_45, cartId);
            product.put(KEY_DIMENSION_40, NONE);
            product.put(KEY_DIMENSION_38, NONE);
            products.add(product);
        }

        add.put(KEY_PRODUCTS, products);

        ecommerce.put(CURRENCY_CODE, IDR);
        ecommerce.put(KEY_ADD, add);

        Map<String, Object> map = new HashMap<>();
        map.put("event", EVENT_ADD_TO_CART);
        map.put("eventCategory", EVENT_CATEGORY_BUY_AGAIN);
        map.put("eventAction", EVENT_ACTION_BUY_AGAIN);
        map.put("eventLabel", isSuccess ? EVENT_LABEL_BUY_AGAIN_SUCCESS : EVENT_LABEL_BUY_AGAIN_FAILURE);
        map.put("ecommerce", ecommerce);
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(map);

    }

    public void eventEmptyBOMRecommendationProductClick(RecommendationItem item, int position, String recomTitle) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "my purchase list - mp - bom_empty",
                "eventAction", "click - product recommendation",
                "eventLabel", recomTitle,
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf("actionField",
                                DataLayer.mapOf("list", "/my_purchase_list_bom_empty - rekomendasi untuk anda - " + item.getRecommendationType() + (item.isTopAds() ? " - product topads" : ""),
                                        "products", DataLayer.listOf(DataLayer.mapOf(
                                                "name", item.getName(),
                                                "id", item.getProductId(),
                                                "price", item.getPrice().replaceAll("[^0-9]", ""),
                                                "brand", "none/other",
                                                "category", item.getCategoryBreadcrumbs(),
                                                "variant", "none/other",
                                                "list", "/my_purchase_list_bom_empty - rekomendasi untuk anda - " + item.getRecommendationType() + (item.isTopAds() ? " - product topads" : ""),
                                                "position", String.valueOf(position)))))
                )));
    }

    public void clearDataLayerList() {
        dataLayerList.clear();
    }

    public void sendEmptyWishlistProductImpression(TrackingQueue trackingQueue) {
        if (!dataLayerList.isEmpty()) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "my purchase list - mp - bom_empty",
                    "eventAction", "impression - product recommendation",
                    "eventLabel", recomTitle,
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "impressions", DataLayer.listOf(
                                    dataLayerList.toArray(new Object[dataLayerList.size()])
                            )
                    ));
            trackingQueue.putEETracking((HashMap<String, Object>) map);
            clearDataLayerList();
        }
    }

    public void eventRecommendationProductImpression(RecommendationItem item, int position, String recomTitle) {
        this.recomTitle = recomTitle;
        this.dataLayerList.add(DataLayer.mapOf(
                "name", item.getName(),
                "id", item.getProductId(),
                "price", item.getPrice().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "category", item.getCategoryBreadcrumbs(),
                "variant", "none/other",
                "list", "/my_purchase_list_bom_empty - rekomendasi untuk anda - " + item.getRecommendationType() + (item.isTopAds() ? " - product topads" : ""),
                "position", String.valueOf(position)));
    }

    public void eventRecommendationAddToCart(OrderListRecomViewModel orderListRecomViewModel, AddToCartDataModel addToCartDataModel){
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent((HashMap<String, Object>) DataLayer.mapOf(
                "event", "addToCart",
                "eventCategory", "my purchase list - mp - bom_empty",
                "eventAction", "click add to cart on my purchase list page",
                "eventLabel", orderListRecomViewModel.getRecomTitle(),
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "add", DataLayer.mapOf("actionField",
                                DataLayer.mapOf("list", "/my_purchase_list_bom_empty - rekomendasi untuk anda - " + orderListRecomViewModel.getRecommendationItem().getRecommendationType() + (orderListRecomViewModel.getRecommendationItem().isTopAds() ? " - product topads" : ""),
                                        "products", DataLayer.listOf(DataLayer.mapOf(
                                                "name", orderListRecomViewModel.getRecommendationItem().getName(),
                                                "id", orderListRecomViewModel.getRecommendationItem().getProductId(),
                                                "price", orderListRecomViewModel.getRecommendationItem().getPrice().replaceAll("[^0-9]", ""),
                                                "brand", "none/other",
                                                "category", orderListRecomViewModel.getRecommendationItem().getCategoryBreadcrumbs(),
                                                "variant", "none/other",
                                                "list", "/my_purchase_list_bom_empty - rekomendasi untuk anda - " + orderListRecomViewModel.getRecommendationItem().getRecommendationType() + (orderListRecomViewModel.getRecommendationItem().isTopAds() ? " - product topads" : ""),
                                                "dimension45", addToCartDataModel.getData().getCartId(),
                                                "quantity",orderListRecomViewModel.getRecommendationItem().getMinOrder(),
                                                "shop_id",String.valueOf(orderListRecomViewModel.getRecommendationItem().getShopId()),
                                                "shop_type",orderListRecomViewModel.getRecommendationItem().getShopType(),
                                                "shop_name",orderListRecomViewModel.getRecommendationItem().getShopName(),
                                                "category_id",NONE
                                                ))))
                )));
    }
}
