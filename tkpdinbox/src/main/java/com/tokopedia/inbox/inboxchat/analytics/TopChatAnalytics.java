package com.tokopedia.inbox.inboxchat.analytics;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;

/**
 * Created by stevenfredian on 11/6/17.
 */

public class TopChatAnalytics {


    public interface Category {
        public static final String PRODUCT_PAGE = "product page";
        public static final String SEND_MESSAGE_PAGE = "send message page";
        public static final String SHOP_PAGE = "shop page";
        public static final String INBOX_CHAT = "inbox-chat";
        public static final String CHAT_DETAIL = "chat detail";
        public static final String UPDATE_TEMPLATE = "update template";
        public static final String ADD_TEMPLATE = "add template";

        static String EVENT_CATEGORY_INBOX_CHAT = "inbox-chat";

    }

    public interface Name {
        public static final String PRODUCT_PAGE = "ClickProductDetailPage";
        public static final String SEND_MESSAGE_PAGE = "ClickMessageRoom";
        public static final String SHOP_PAGE = "ClickShopPage";
        public static final String INBOX_CHAT = "ClickInboxChat";
        public static final String CHAT_DETAIL = "ClickChatDetail";

        String EVENT_NAME_CLICK_INBOXCHAT = "clickInboxChat";

    }

    public interface Action {
        public static final String PRODUCT_PAGE = "click";
        public static final String SEND_MESSAGE_PAGE = "click on kirim";
        public static final String SHOP_PAGE = "click on kirim pesan";
        public static final String INBOX_CHAT_CLICK = "click on chatlist";
        public static final String INBOX_CHAT_SEARCH = "search on chatlist";
        public static final String CHAT_DETAIL_SEND = "click on send button";
        public static final String CHAT_DETAIL_ATTACH = "click on attach image button";
        public static final String CHAT_DETAIL_INSERT = "click on insert button";
        public static final String CHAT_DETAIL_ATTACHMENT = "click on send product attachment";
        public static final String TEMPLATE_CHAT_CLICK = "click on template chat";
        public static final String UPDATE_TEMPLATE = "click on tambah template";
        public static final String CLICK_THUMBNAIL = "click on thumbnail";

        static final String EVENT_ACTION_CLICK_COMMUNITY_TAB = "click on community tab";

    }

    public interface Label {
        public static final String PRODUCT_PAGE = "message shop";
    }

    public static EventTracking eventClickInboxChannel() {
        return new EventTracking(Name.EVENT_NAME_CLICK_INBOXCHAT,
                Category.EVENT_CATEGORY_INBOX_CHAT,
                Action.EVENT_ACTION_CLICK_COMMUNITY_TAB,
                "");
    }

}
