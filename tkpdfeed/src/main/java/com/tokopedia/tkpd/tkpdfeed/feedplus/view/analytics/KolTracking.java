package com.tokopedia.tkpd.tkpdfeed.feedplus.view.analytics;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by nisie on 1/2/18.
 */

public class KolTracking {

    private static final String EVENT = "event";
    private static final String ECOMMERCE = "ecommerce";

    public static class Event {

        static final String IMPRESSION = "promoView";
        static final String CLICK = "promoClick";
    }

    public static class Ecommerce {

        private static final String PROMO_VIEW = "promoView";
        private static final String PROMOTIONS = "promotions";

        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "name";
        private static final String KEY_CREATIVE = "creative";
        private static final String KEY_POSITION = "position";
        private static final String KEY_CATEGORY = "category";
        private static final String KEY_PROMO_ID = "promo_id";
        private static final String KEY_PROMO_CODE = "promo_code";
        private static final String KEY_USER_ID = "userId";
        private static final String KEY_USER_ID_MOD = "userIdmodulo";


        public static Map<String, Object> getKolContentEcommerce(List<Promotion> listPromotion) {
            return DataLayer.mapOf(PROMO_VIEW, getListPromotions(listPromotion));
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
            map.put(KEY_ID,  String.valueOf(promo.getId()));
            map.put(KEY_NAME, promo.getName());
            map.put(KEY_CREATIVE, promo.getCreative());
            map.put(KEY_POSITION, String.valueOf(promo.getPosition()));
            map.put(KEY_CATEGORY, promo.getCategory());
            map.put(KEY_PROMO_ID, String.valueOf(promo.getPromoId()));
            map.put(KEY_PROMO_CODE, promo.getPromoCode());
            map.put(KEY_USER_ID, String.valueOf(promo.getUserId()));
            map.put(KEY_USER_ID_MOD, String.valueOf(promo.getUserIdMod50()));
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
        int userId;

        public Promotion(int id, String name, String creative, int position,
                         String category, int promoId, String promoCode, int userId) {
            this.id = id;
            this.name = name;
            this.creative = creative;
            this.position = position;
            this.category = category;
            this.promoId = promoId;
            this.promoCode = promoCode;
            this.userId = userId;
        }

        /**
         * kolId
         */
        public int getId() {
            return id;
        }

        /**
         * kolId
         */
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

        public int getUserId() {
            return userId;
        }

        public int getUserIdMod50() {
            return userId % 50;
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

    public static Map<String, Object> getKolImpressionTracking(List<Promotion> listPromotion) {
        return DataLayer.mapOf(
                EVENT, Event.IMPRESSION,
                ECOMMERCE, Ecommerce.getKolContentEcommerce(listPromotion));
    }

    public static Map<String, Object> getKolClickTracking(List<Promotion> listPromotion) {
        return DataLayer.mapOf(
                EVENT, Event.CLICK,
                ECOMMERCE, Ecommerce.getKolContentEcommerce(listPromotion));
    }
}
