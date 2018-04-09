package com.tokopedia.loyalty.view.analytics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventTracking {
    private static final String KEY_PARAM_EVENT = "event";
    private static final String KEY_PARAM_CATEGORY = "eventCategory";
    private static final String KEY_PARAM_ACTION = "eventAction";
    private static final String KEY_PARAM_LABEL = "actionLabel";
    private static final String KEY_PARAM_ECOMMERCE = "ecommerce";

    private Map<String, Object> eventTracking;

    public EventTracking() {
        this.eventTracking = new HashMap<>();
    }

    public EventTracking(String event, String category, String action, String label) {
        this();
        this.eventTracking.put(KEY_PARAM_EVENT, event);
        this.eventTracking.put(KEY_PARAM_CATEGORY, category);
        this.eventTracking.put(KEY_PARAM_ACTION, action);
        this.eventTracking.put(KEY_PARAM_LABEL, label);
    }

    public EventTracking(String event, String category, String action,
                         String label, Object ecommerce) {
        this(event, category, action, label);
        this.eventTracking.put(KEY_PARAM_ECOMMERCE, ecommerce);
    }

    public void addCustomTracking(String key, Object tracking) {
        this.eventTracking.put(key, tracking);
    }

    public Map<String, Object> getEventMap() {
        return this.eventTracking;
    }

}

class Ecommerce {
    private static final String KEY_PARAM_PROMO_VIEW = "promoView";
    private static final String KEY_PARAM_PROMO_CLICK = "promoClick";

    private Map<String, Object> ecommerce;

    public Ecommerce() {
        this.ecommerce = new HashMap<>();
    }

    public Ecommerce(PromoView promoView) {
        this();
        this.ecommerce.put(KEY_PARAM_PROMO_VIEW, promoView);
    }

    public Ecommerce(PromoClick promoClick) {
        this();
        this.ecommerce.put(KEY_PARAM_PROMO_CLICK, promoClick);
    }

    public void addCustomTracking(String key, Object tracking) {
        this.ecommerce.put(key, tracking);
    }

    public Map<String, Object> getEventMap() {
        return this.ecommerce;
    }

}

class PromoView {
    private static final String KEY_PARAM_NAME = "name";
    private static final String KEY_PARAM_CREATIVE = "creative";
    private static final String KEY_PARAM_CREATIVE_URL = "creative_url";
    private static final String KEY_PARAM_POSITION = "position";
    private static final String KEY_PARAM_CATEGORY = "category";
    private static final String KEY_PARAM_PROMOTIONS = "promotions";

    private Map<String, Object> promoView;

    public PromoView() {
        this.promoView = new HashMap<>();
    }

    public PromoView(String name, String creative, String creativeUrl, String position,
                     String category, Map<String, Object> promotions) {
        this();
        this.promoView.put(KEY_PARAM_NAME, name);
        this.promoView.put(KEY_PARAM_CREATIVE, creative);
        this.promoView.put(KEY_PARAM_CREATIVE_URL, creativeUrl);
        this.promoView.put(KEY_PARAM_POSITION, position);
        this.promoView.put(KEY_PARAM_CATEGORY, category);
        this.promoView.put(KEY_PARAM_PROMOTIONS, promotions);
    }

    public void addCustomTracking(String key, Object tracking) {
        this.promoView.put(key, tracking);
    }

    public Map<String, Object> getEventMap() {
        return this.promoView;
    }

}

class PromoClick {
    private static final String KEY_PARAM_PROMOTIONS = "promotions";

    private Map<String, Object> promotions;

    public PromoClick() {
        this.promotions = new HashMap<>();
    }

    public PromoClick(List<Map<String, Object>> promotions) {
        this.promotions.put(KEY_PARAM_PROMOTIONS, promotions);
    }

    public void addCustomTracking(String key, Object tracking) {
        this.promotions.put(key, tracking);
    }

    public Map<String, Object> getEventMap() {
        return this.promotions;
    }

}

class Promotion {
    private static final String KEY_PARAM_ID = "id";
    private static final String KEY_PARAM_NAME = "name";
    private static final String KEY_PARAM_POSITION = "position";
    private static final String KEY_PARAM_CREATIVE = "creative";
    private static final String KEY_PARAM_CREATIVE_URL = "creative_url";
    private static final String KEY_PARAM_CATEGORY = "category";
    private static final String KEY_PARAM_PROMO_ID = "promo_id";
    private static final String KEY_PARAM_PROMO_CODE = "promo_code";

    private Map<String, Object> promotion;

    public Promotion() {
        this.promotion = new HashMap<>();
    }

    public Promotion(String id, String promoId, String promoCode) {
        this();
        this.promotion.put(KEY_PARAM_ID, id);
        this.promotion.put(KEY_PARAM_PROMO_ID, promoId);
        this.promotion.put(KEY_PARAM_PROMO_CODE, promoCode);
    }

    public Promotion(String id, String name, String position, String creative, String creativeUrl,
                     String category, String promoId, String promoCode) {
        this();
        this.promotion.put(KEY_PARAM_ID, id);
        this.promotion.put(KEY_PARAM_NAME, name);
        this.promotion.put(KEY_PARAM_POSITION, position);
        this.promotion.put(KEY_PARAM_CREATIVE, creative);
        this.promotion.put(KEY_PARAM_CREATIVE_URL, creativeUrl);
        this.promotion.put(KEY_PARAM_CATEGORY, category);
        this.promotion.put(KEY_PARAM_PROMO_ID, promoId);
        this.promotion.put(KEY_PARAM_PROMO_CODE, promoCode);
    }

    public void addCustomTracking(String key, Object tracking) {
        this.promotion.put(key, tracking);
    }

    public Map<String, Object> getEventMap() {
        return this.promotion;
    }

}
