package com.tokopedia.discovery.similarsearch.analytics;

import com.tokopedia.core.analytics.AppEventTracking;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class SimilarSearchAppEventTracking implements AppEventTracking {
    interface Event {
        String GenericProductView = "productView";
        String GenericProductClick = "productClick";
        String GenericViewSearchResult = "viewSearchResult";
        String GenericClickSearchResult = "clickSearchResult";

    }

    interface Category {
        String EventSearchResult =
                "search result";
        String EventSimilarProduct = "similar product";

    }

    interface Action {
        String EventLongPressProduct = "click - long press product";
        String EventImpressionProduct = "impression product";
        String EventClickSimilarProduct = "click - similar product";
        String EventNoSimilarProduct = "no similar product";


    }

    interface Label {
        String LabelKeywordProduct = "Keyword: %s - product id: %s}";
        String LabelProductIDTitle = "product id: %s";
        String LabelScreeName = "%s";
        String LabelProductID = "%s";

    }
}
