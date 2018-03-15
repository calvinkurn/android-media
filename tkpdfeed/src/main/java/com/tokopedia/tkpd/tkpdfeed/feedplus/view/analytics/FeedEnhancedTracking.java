package com.tokopedia.tkpd.tkpdfeed.feedplus.view.analytics;

import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.tkpd.tkpdfeed.feedplus.view.analytics.FeedEnhancedTracking.Event.PROMO_CLICK;
import static com.tokopedia.tkpd.tkpdfeed.feedplus.view.analytics.FeedEnhancedTracking.Event.PROMO_VIEW;

/**
 * @author by nisie on 1/2/18.
 */

public class FeedEnhancedTracking {

    private static final String EVENT = "event";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_ID_MOD = "userIdmodulo";
    private static final String ECOMMERCE = "ecommerce";

    public static class Event {

        static final String PROMO_VIEW = "promoView";
        static final String PROMO_CLICK = "promoClick";
    }

    public static class Ecommerce {

        private static final String PROMOTIONS = "promotions";

        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "name";
        private static final String KEY_CREATIVE = "creative";
        private static final String KEY_POSITION = "position";
        private static final String KEY_CATEGORY = "category";
        private static final String KEY_PROMO_ID = "promo_id";
        private static final String KEY_PROMO_CODE = "promo_code";


        public static Map<String, Object> getEcommerceView(List<Promotion> listPromotion) {
            return DataLayer.mapOf(PROMO_VIEW, getListPromotions(listPromotion));
        }

        public static Map<String, Object> getEcommerceClick(List<Promotion> listPromotion) {
            return DataLayer.mapOf(PROMO_CLICK, getListPromotions(listPromotion));

        }

        private static Map<String, Object> getListPromotions(List<Promotion> list) {
            return DataLayer.mapOf(PROMOTIONS, createList(list));
        }

        private static List<Object> createList(List<Promotion> listPromotion) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Promotion promo : listPromotion) {
                Map<String, Object> map = createPromotionMap(promo);
                list.add(map);
            }
            return DataLayer.listOf(list.toArray(new Object[list.size()]));
        }

        private static Map<String, Object> createPromotionMap(Promotion promo) {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_ID, String.valueOf(promo.getId()));
            map.put(KEY_NAME, promo.getName());
            map.put(KEY_CREATIVE, promo.getCreative());
            map.put(KEY_POSITION, String.valueOf(promo.getPosition()));
            map.put(KEY_CATEGORY, promo.getCategory());
            map.put(KEY_PROMO_ID, String.valueOf(promo.getPromoId()));
            map.put(KEY_PROMO_CODE, promo.getPromoCode());

            if (!TextUtils.isEmpty(promo.getUserId())) {
                map.put(KEY_USER_ID, promo.getUserId());
            }

            if (!TextUtils.isEmpty(promo.getUserIdMod50())) {
                map.put(KEY_USER_ID_MOD, promo.getUserIdMod50());
            }

            return map;
        }

    }

    public static class Promotion {

        private static final String CONTENT_FEED = "content feed";
        private static final String FOLLOWED_KOL_POST = "followedkolpost";
        private static final String KOL_POST = "kolpost";
        private static final String KOL_RECOMMENDATION = "kolrecommendation";
        private static String PROFILE = "profile";
        int id;
        String name;
        String creative;
        int position;
        String category;
        int promoId;
        String promoCode;
        String userId;
        String userIdMod50;

        public Promotion(int id, String name, String creative, int position,
                         String category, int promoId, String promoCode) {
            this.id = id;
            this.name = name;
            this.creative = creative;
            this.position = position;
            this.category = category;
            this.promoId = promoId;
            this.promoCode = promoCode;
        }

        public Promotion(int id, String name, String creative, int position,
                         String category, int promoId, String promoCode, int userId) {
            this.id = id;
            this.name = name;
            this.creative = creative;
            this.position = position;
            this.category = category;
            this.promoId = promoId;
            this.promoCode = promoCode;
            this.userId = String.valueOf(userId);
            this.userIdMod50 = String.valueOf(userId % 50);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCreative() {
            return creative;
        }

        public int getPosition() {
            return position;
        }

        public String getCategory() {
            return category;
        }

        public int getPromoId() {
            return promoId;
        }

        public String getPromoCode() {
            return promoCode;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserIdMod50() {
            return userIdMod50;
        }

        public static String createContentNameRecommendation() {
            return CONTENT_FEED + " - " + KOL_RECOMMENDATION + " - " + PROFILE;
        }

        public static String createContentName(String tagsType, String cardType) {
            return CONTENT_FEED + " - "
                    + cardType + " - "
                    + tagsType;
        }
    }

    public static Map<String, Object> getKolImpressionTracking(List<Promotion> listPromotion, int
            userId) {
        return DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                KEY_USER_ID, String.valueOf(userId),
                KEY_USER_ID_MOD, String.valueOf(userId % 50),
                ECOMMERCE, Ecommerce.getEcommerceView(listPromotion));
    }

    public static Map<String, Object> getKolClickTracking(List<Promotion> listPromotion, int
            userId) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                KEY_USER_ID, String.valueOf(userId),
                KEY_USER_ID_MOD, String.valueOf(userId % 50),
                ECOMMERCE, Ecommerce.getEcommerceClick(listPromotion));
    }

    public static Map<String, Object> getImpressionTracking(List<Promotion> listPromotion) {
        return DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                ECOMMERCE, Ecommerce.getEcommerceView(listPromotion));
    }

    public static Map<String, Object> getClickTracking(List<Promotion> listPromotion) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                ECOMMERCE, Ecommerce.getEcommerceClick(listPromotion));
    }
}
