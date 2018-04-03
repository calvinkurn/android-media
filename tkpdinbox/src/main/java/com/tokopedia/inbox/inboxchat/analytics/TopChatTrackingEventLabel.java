package com.tokopedia.inbox.inboxchat.analytics;

/**
 * Created by stevenfredian on 11/6/17.
 */

public class TopChatTrackingEventLabel {

    public interface Category {
        public static final String PRODUCT_PAGE = "product page";
        public static final String SEND_MESSAGE_PAGE = "send message page";
        public static final String SHOP_PAGE = "shop page";
        public static final String INBOX_CHAT = "inbox-chat";
        public static final String CHAT_DETAIL = "chat detail";
        public static final String UPDATE_TEMPLATE = "update template";
        public static final String ADD_TEMPLATE = "add template";
    }

    public interface Name {
        public static final String PRODUCT_PAGE = "ClickProductDetailPage";
        public static final String SEND_MESSAGE_PAGE = "ClickMessageRoom";
        public static final String SHOP_PAGE = "ClickShopPage";
        public static final String INBOX_CHAT ="ClickInboxChat";
        public static final String CHAT_DETAIL ="ClickChatDetail";
    }

    public interface Action {
        public static final String PRODUCT_PAGE = "click";
        public static final String SEND_MESSAGE_PAGE = "click on kirim";
        public static final String SHOP_PAGE = "click on kirim pesan";
        public static final String INBOX_CHAT_CLICK ="click on chatlist";
        public static final String INBOX_CHAT_SEARCH ="search on chatlist";
        public static final String CHAT_DETAIL_SEND ="click on send button";
        public static final String CHAT_DETAIL_ATTACH ="click on attach image button";
        public static final String CHAT_DETAIL_INSERT ="click on insert button";
        public static final String CHAT_DETAIL_ATTACHMENT ="click on send product attachment";
        public static final String TEMPLATE_CHAT_CLICK ="click on template chat";
        public static final String UPDATE_TEMPLATE = "click on tambah template";
        public static final String CLICK_THUMBNAIL = "click on thumbnail";
    }

    public interface Label {
        public static final String PRODUCT_PAGE =  "message shop";
    }


}
