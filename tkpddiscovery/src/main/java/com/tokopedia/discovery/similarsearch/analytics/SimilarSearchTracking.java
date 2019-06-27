package com.tokopedia.discovery.similarsearch.analytics;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.discovery.intermediary.domain.model.ProductModel;
import com.tokopedia.discovery.similarsearch.model.ProductsItem;

import java.util.List;
import java.util.Map;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class SimilarSearchTracking extends UnifyTracking {

    public static void eventProductLongPress(Context context, String keyword, String productId) {
        sendGTMEvent(context, new EventTracking(
                SimilarSearchAppEventTracking.Event.GenericClickSearchResult,
                SimilarSearchAppEventTracking.Category.EventSearchResult,
                SimilarSearchAppEventTracking.Action.EventLongPressProduct,
                String.format(SimilarSearchAppEventTracking.Label.LabelKeywordProduct, keyword,productId)
        ).getEvent());
    }

    public static void eventUserSeeSimilarProduct(Context context, String productId, List<Object> productsItem) {
        TrackingUtils.eventTrackingEnhancedEcommerce(context, new SimilarSearchEventTracking(
                SimilarSearchAppEventTracking.Event.GenericProductView,
                SimilarSearchAppEventTracking.Category.EventSimilarProduct,
                SimilarSearchAppEventTracking.Action.EventImpressionProduct,
                String.format(SimilarSearchAppEventTracking.Label.LabelProductIDTitle,productId),
                DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                                productsItem.toArray(new Object[productsItem.size()])
                        ))
        ).getEvent());
    }

    public static void eventClickSimilarProduct(Context context, String screenName,String productsId,Object productsItem) {
        try {
            TrackingUtils.eventTrackingEnhancedEcommerce(context, new SimilarSearchEventTracking(
                    SimilarSearchAppEventTracking.Event.GenericProductClick,
                    SimilarSearchAppEventTracking.Category.EventSimilarProduct,
                    SimilarSearchAppEventTracking.Action.EventClickSimilarProduct,
                    String.format(SimilarSearchAppEventTracking.Label.LableOriginProductId, productsId, screenName),
                    DataLayer.mapOf(
                            "click",
                            DataLayer.mapOf("actionField",
                                    DataLayer.mapOf("list", "'/similarproduct'"),
                                    "products", DataLayer.listOf(productsItem)
                            ))
            ).getEvent());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void eventUserSeeNoSimilarProduct(Context context, String productId,String screenName) {
        if (context != null) {
            sendGTMEvent(context, new EventTracking(
                    SimilarSearchAppEventTracking.Event.GenericViewSearchResult,
                    SimilarSearchAppEventTracking.Category.EventSimilarProduct,
                    SimilarSearchAppEventTracking.Action.EventNoSimilarProduct,
                    String.format(SimilarSearchAppEventTracking.Label.LableOriginProductId, productId,screenName)
            ).getEvent());
        }
    }

    public static void eventAddWishList(Context context, String productId) {
        if (context != null) {
            sendGTMEvent(context, new EventTracking(
                    SimilarSearchAppEventTracking.Event.GenericViewSearchResult,
                    SimilarSearchAppEventTracking.Category.EventSimilarProduct,
                    SimilarSearchAppEventTracking.Action.EventAddWishList,
                    String.format(SimilarSearchAppEventTracking.Label.LabelProductID, productId)
            ).getEvent());
        }
    }

    public static void eventRemoveWishList(Context context, String productId) {
        if (context != null) {
            sendGTMEvent(context, new EventTracking(
                    SimilarSearchAppEventTracking.Event.GenericViewSearchResult,
                    SimilarSearchAppEventTracking.Category.EventSimilarProduct,
                    SimilarSearchAppEventTracking.Action.EventRemoveWishList,
                    String.format(SimilarSearchAppEventTracking.Label.LabelProductID, productId)
            ).getEvent());
        }
    }
}
