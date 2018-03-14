package com.tokopedia.tkpd.tkpdreputation.analytic;

import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;

import java.util.HashMap;

/**
 * Created by zulfikarrahman on 3/13/18.
 */

public class ReputationTracking {

    private ReputationRouter reputationRouter;

    public ReputationTracking(ReputationRouter reputationRouter) {
        this.reputationRouter = reputationRouter;
    }

    private void eventShopPageOfficialStore(String category, String action, String label, String shopId){
        HashMap<String, Object> eventMap = createEventMap(ReputationTrackingConstant.CLICK_OFFICIAL_STORE, category,
                action, label);
        eventMap.put(ReputationTrackingConstant.SHOP_ID, shopId);
        reputationRouter.sendEventTrackingShopPage(eventMap);
    }

    private void eventShopPageOfficialStoreProductId(String category, String action, String label, String productId){
        HashMap<String, Object> eventMap = createEventMap(ReputationTrackingConstant.CLICK_OFFICIAL_STORE, category,
                action, label);
        eventMap.put(ReputationTrackingConstant.PRODUCT_ID, productId);
        reputationRouter.sendEventTrackingShopPage(eventMap);
    }

    private HashMap<String, Object> createEventMap(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(ReputationTrackingConstant.EVENT, event);
        eventMap.put(ReputationTrackingConstant.EVENT_CATEGORY, category);
        eventMap.put(ReputationTrackingConstant.EVENT_ACTION, action);
        eventMap.put(ReputationTrackingConstant.EVENT_LABEL, label);
        return eventMap;
    }

    public void eventClickLikeDislikeReview(String titlePage, boolean status, int position, String shopId) {
        eventShopPageOfficialStore(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_REVIEW + (status ? ReputationTrackingConstant.NEUTRAL : ReputationTrackingConstant.HELPING) + "-" + String.valueOf(position),
                shopId);
    }

    public void eventClickProductPictureOrName(String titlePage, int position, String productId) {
        eventShopPageOfficialStore(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_PRODUCT_PICTURE_OR_NAME +  String.valueOf(position),
                productId);
    }

    public void eventClickUserAccount(String titlePage, int position, String shopId) {
        eventShopPageOfficialStore(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_USER_ACCOUNT +  String.valueOf(position),
                shopId);
    }

    public void eventCLickThreeDotMenu(String titlePage, int position, String shopId) {
        eventShopPageOfficialStore(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_THREE_DOTTED +  String.valueOf(position),
                shopId);
    }

    public void eventClickChooseThreeDotMenu(String titlePage, int position, String typeAction, String shopId) {
        eventShopPageOfficialStore(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ReputationTrackingConstant.TOP_CONTENT_THREE_DOTTED_CLICK,
                ReputationTrackingConstant.CLICK_THREE_DOTTED +  String.valueOf(position),
                shopId);
    }

    public void eventClickSeeReplies(String titlePage, int position, String shopId) {
        eventShopPageOfficialStore(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_SEE_REPLIES +  String.valueOf(position),
                shopId);
    }

    public void eventClickLikeDislikeReviewPage(String titlePage, boolean status, int position, String shopId) {
        eventShopPageOfficialStore(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_REVIEW + (status ? ReputationTrackingConstant.NEUTRAL : ReputationTrackingConstant.HELPING) + "-" + String.valueOf(position),
                shopId);
    }

    public void eventClickProductPictureOrNamePage(String titlePage, int position, String productId) {
        eventShopPageOfficialStoreProductId(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_PRODUCT_PICTURE_OR_NAME +  String.valueOf(position),
                productId);
    }

    public void eventCLickThreeDotMenuPage(String titlePage, int position, String shopId) {
        eventShopPageOfficialStore(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_THREE_DOTTED +  String.valueOf(position),
                shopId);
    }

    public void eventClickChooseThreeDotMenuPage(String titlePage, int position, String report, String shopId) {
        eventShopPageOfficialStore(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_DOTTED_MENU_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_THREE_DOTTED +  String.valueOf(position),
                shopId);
    }

    public void eventClickSeeRepliesPage(String titlePage, int position, String shopId) {
        eventShopPageOfficialStore(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_DOTTED_MENU_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_SEE_REPLIES +  String.valueOf(position),
                shopId);
    }

    public void eventClickSeeMoreReview(String titlePage, String shopId) {
        eventShopPageOfficialStore(ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ReputationTrackingConstant.BOTTOM_NAVIGATION_CLICK,
                ReputationTrackingConstant.CLICK_SEE_MORE,
                shopId);
    }
}
