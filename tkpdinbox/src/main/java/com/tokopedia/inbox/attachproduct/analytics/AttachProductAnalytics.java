package com.tokopedia.inbox.attachproduct.analytics;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;

/**
 * Created by Hendri on 02/03/18.
 */

public class AttachProductAnalytics {

    public static class Event {
        public static final String CLICK_CHAT_DETAIL = "ClickChatDetail";
    }

    public static class Category{
        public static final String CHAT_DETAIL = "chat detail";
    }

    public static class Action{
        public static final String CLICK_PRODUCT_IMAGE = "click on product image";
        public static final String CHECK_PRODUCT = "click one of the product";
    }

    public static class Label{

    }

    public static EventTracking getEventCheckProduct() {
        return new EventTracking(
                Event.CLICK_CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CHECK_PRODUCT,
                ""
        );
    }

    public static EventTracking getEventClickChatAttachedProductImage() {
        return new EventTracking(
                Event.CLICK_CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_PRODUCT_IMAGE,
                ""
        );
    }
}
