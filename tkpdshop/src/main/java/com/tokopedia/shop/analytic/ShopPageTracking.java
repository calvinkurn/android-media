package com.tokopedia.shop.analytic;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoDetail;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zulfikarrahman on 3/12/18.
 */

public class ShopPageTracking {
    private final ShopModuleRouter shopModuleRouter;

    public ShopPageTracking(ShopModuleRouter shopModuleRouter) {
        this.shopModuleRouter = shopModuleRouter;
    }

    private void eventShopPageOfficialStore(String category, String action, String label, String shopId) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.CLICK_OFFICIAL_STORE, category,
                action, label);
        eventMap.put(ShopPageTrackingConstant.SHOP_ID, shopId);
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    private void eventShopPageOfficialStoreProductId(String category, String action, String label, String productId) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.CLICK_OFFICIAL_STORE, category,
                action, label);
        eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, productId);
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    private void eventShopPageOfficialStoreView(String category, String action, String label, String shopId) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.VIEW_OFFICIAL_STORE, category,
                action, label);
        eventMap.put(ShopPageTrackingConstant.SHOP_ID, shopId);
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    private HashMap<String, Object> createEventMap(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(ShopPageTrackingConstant.EVENT, event);
        eventMap.put(ShopPageTrackingConstant.EVENT_CATEGORY, category);
        eventMap.put(ShopPageTrackingConstant.EVENT_ACTION, action);
        eventMap.put(ShopPageTrackingConstant.EVENT_LABEL, label);
        return eventMap;
    }

    public void eventBackPressed(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_ARROW_BACK,
                shopId);
    }

    public void eventClickShopLogo(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_LOGO, shopId);
    }

    public void eventClickShopName(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_NAME, shopId);
    }

    public void eventClickShareShop(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHARE_SHOP, shopId);
    }

    public void eventClickMessageShop(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_MESSAGE_BRAND, shopId);
    }

    public void eventClickFavouriteShop(String titlePage, String shopId, boolean favouriteShop, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_FAVOURITE_SHOP +
                        (favouriteShop ? ShopPageTrackingConstant.UNFAVOURITE : ShopPageTrackingConstant.FAVOURITE),
                shopId);
    }

    public void eventClickListFavourite(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_LIST_OF_FAVOURITE,
                shopId);
    }

    public void eventClickUserFavouritingShop(String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.TOP_SECTION_LIST_FAVOURITE_CLICK,
                ShopPageTrackingConstant.CLICK_USER_FAVOURITING_SHOP,
                shopId);
    }

    public void eventCloseListFavourite(String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.TOP_SECTION_LIST_FAVOURITE_CLICK,
                ShopPageTrackingConstant.CLICK_CLOSE_FAVOURITE,
                shopId);
    }

    public void eventClickTotalProduct(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_TOTAL_PRODUCTS, shopId);
    }

    public void eventClickShopSpeed(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_SPEED, shopId);
    }

    public void eventClickShopSpeedInfo(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_SHOP_SPEED_CLICK,
                ShopPageTrackingConstant.CLICK_SEE_SHOP_INFO, shopId);
    }

    public void eventClickShopInfo(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_INFO,
                shopId);
    }

    public void eventClickTabShopInfo(CharSequence titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_TOP_TAB + titlePage,
                shopId);
    }


    public void eventClickNoteList(long position, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_NOTE_LIST + String.valueOf(position),
                shopId);
    }

    public void eventBackPressedShopInfo(String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_ARROW_BACK_TO_SHOP,
                shopId);
    }

    public void eventClickShareShopNotePage(String shopId, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_SHARE_NOTE_LIST,
                shopId);
    }

    public void eventClickTabShopPage(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_TOP_CONTENT_TAB + titlePage,
                shopId);
    }

    public void eventClickSearchProduct(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.ACTION_SEARCH_BAR_CLICK,
                ShopPageTrackingConstant.ClICK_SEARCH_BAR,
                shopId);
    }

    public void eventTypeKeywordSearchProduct(String titlePage, String keyword, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.ACTION_SEARCH_BAR_TYPE,
                ShopPageTrackingConstant.TYPE_KEYWORDS + keyword,
                shopId);
    }

    public void eventClickWishlistShopPageFeatured(String titlePage, boolean wishList, String producId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_WISHLIST + (wishList ? ShopPageTrackingConstant.ADD_WISHLIST : ShopPageTrackingConstant.REMOVE_WISHLIST),
                producId);
    }

    public void eventViewShopPage(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_IMPRESSION,
                "", shopId);
    }

    public void eventClickWishlistShop(String titlePage, boolean wishList, boolean isFromHomeShop, String productId) {
        eventShopPageOfficialStoreProductId(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + (isFromHomeShop ? ShopPageTrackingConstant.PRODUCT_LIST : ShopPageTrackingConstant.PRODUCT_PAGE) + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_WISHLIST + (wishList ? ShopPageTrackingConstant.ADD_WISHLIST : ShopPageTrackingConstant.REMOVE_WISHLIST),
                productId);
    }

    public void eventClickEtalaseShop(String titlePage, boolean isFromHomeShop, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + (isFromHomeShop ? ShopPageTrackingConstant.PRODUCT_LIST : ShopPageTrackingConstant.PRODUCT_PAGE) + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_ETALASE,
                shopId);
    }

    public void eventClickEtalaseShopChoose(String titlePage, boolean isFromHomeShop, String etalaseName, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + (isFromHomeShop ? ShopPageTrackingConstant.PRODUCT_LIST : ShopPageTrackingConstant.PRODUCT_PAGE) + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_MENU + etalaseName,
                shopId);
    }

    public void eventClickSortProductList(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.BOTTOM_NAVIGATION_CLICK,
                ShopPageTrackingConstant.CLICK_SORT,
                shopId);
    }


    public void eventClickViewTypeProduct(String titlePage, int index, String shopId) {
        String viewTypeName;
        switch (index) {
            case 0:
                viewTypeName = ShopPageTrackingConstant.GRID;
                break;
            case 1:
                viewTypeName = ShopPageTrackingConstant.FULL_IMAGE;
                break;
            case 2:
                viewTypeName = ShopPageTrackingConstant.LIST;
                break;
            default:
                viewTypeName = ShopPageTrackingConstant.GRID;
                break;
        }
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.BOTTOM_NAVIGATION_CLICK,
                ShopPageTrackingConstant.CLICK_PRODUCT_VIEW + viewTypeName,
                shopId);
    }

    public void eventClickChooseSort(String titlePage, String sortName, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.BOTTOM_NAVIGATION_PRODUCT_SORT_CLICK,
                ShopPageTrackingConstant.CLICK_SORT_BY + sortName,
                shopId);
    }

    public void eventClickSeeMoreProduct(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.BOTTOM_NAVIGATION_CLICK,
                ShopPageTrackingConstant.CLICK_VIEW_MORE_PRODUCT,
                shopId);
    }

    public void eventViewBottomNavigation(String titlePage, String shopId) {
        eventShopPageOfficialStoreView(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.BOTTOM_NAVIGATION_IMPRESSION,
                "",
                shopId);
    }

    public void eventClickAddProduct(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BRAND,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_ADD_PRODUCT,
                shopId);
    }

    public void eventClickShopSetting(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BRAND,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_SETTING,
                shopId);
    }

    public void eventClickAddNote(String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BRAND,
                ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_ADD_NOTE,
                shopId);
    }

    public void eventClickProductPictureFeaturedImpression(String titlePage, String name, String id, String price, int adapterPosition) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.CLICK_OFFICIAL_STORE,
                ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_PRODUCT_PICTURE);
        eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, id);
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapProductClickImpression(name, id, price, adapterPosition));
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    public void eventViewProductFeaturedImpression(String titlePage, List<ShopProductViewModel> shopProductViewModelList) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.VIEW_OFFICIAL_STORE,
                ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.IMPRESSION_OF_TOP_PRODUCT_LIST);
        eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, "");
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapProductViewImpression(shopProductViewModelList));
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    public void eventViewProductImpression(String titlePage, List<ShopProductViewModel> shopProductViewModelList, boolean isFromHomeShop) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.VIEW_OFFICIAL_STORE,
                ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + " - " + (isFromHomeShop ? ShopPageTrackingConstant.PRODUCT_LIST : ShopPageTrackingConstant.PRODUCT_PAGE)
                        + " - " + ShopPageTrackingConstant.IMPRESSION,
                ShopPageTrackingConstant.IMPRESSION_OF_PRODUCT_PICTURES);
        eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, "");
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapProductViewImpression(shopProductViewModelList));
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    public void eventClickProductTitleFeaturedImpression(String titlePage, String name, String id, String price, int adapterPosition) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.CLICK_OFFICIAL_STORE,
                ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_PRODUCT_NAME);
        eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, id);
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapProductClickImpression(name, id, price, adapterPosition));
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }


    public void eventClickProductImpression(String titlePage, String name, String id, String price, int adapterPosition, boolean isFromHomeShop) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.CLICK_OFFICIAL_STORE,
                ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + " - " + (isFromHomeShop ? ShopPageTrackingConstant.PRODUCT_LIST : ShopPageTrackingConstant.PRODUCT_PAGE)
                        + " - " + ShopPageTrackingConstant.CLICK,
                ShopPageTrackingConstant.CLICK_PRODUCT_PICTURE);
        eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, id);
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapProductClickImpression(name, id, price, adapterPosition));
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    private Map<String, Object> createMapProductViewImpression(List<ShopProductViewModel> shopProductViewModelList) {
        List<Object> list = getListProductAsObjectDataLayer(shopProductViewModelList);
        return  DataLayer.mapOf(
                ShopPageTrackingConstant.CURRENCY_CODE, ShopPageTrackingConstant.IDR,
                ShopPageTrackingConstant.IMPRESSIONS, DataLayer.listOf(
                                list.toArray(new Object[list.size()])
                        ));
    }

    public List<Object> getListProductAsObjectDataLayer(List<ShopProductViewModel> shopProductViewModelList) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < shopProductViewModelList.size(); i++) {
            ShopProductViewModel viewModel = shopProductViewModelList.get(i);
            list.add(
                    DataLayer.mapOf(
                            ShopPageTrackingConstant.NAME, viewModel.getName(),
                            ShopPageTrackingConstant.ID, viewModel.getId(),
                            ShopPageTrackingConstant.PRICE, viewModel.getPrice(),
                            ShopPageTrackingConstant.BRAND, ShopPageTrackingConstant.NONE_OTHER,
                            ShopPageTrackingConstant.CATEGORY, ShopPageTrackingConstant.NONE_OTHER,
                            ShopPageTrackingConstant.VARIANT, ShopPageTrackingConstant.NONE_OTHER,
                            ShopPageTrackingConstant.LIST, ShopPageTrackingConstant.SHOPPAGE_PRODUCT + i,
                            ShopPageTrackingConstant.POSITION, i
                    )
            );
        }
        return list;
    }

    private Map<String, Object> createMapProductClickImpression(String name, String id, String price, int adapterPosition) {
        return DataLayer.mapOf(
                ShopPageTrackingConstant.CLICK, DataLayer.mapOf(
                        ShopPageTrackingConstant.ACTION_FIELD, DataLayer.mapOf(ShopPageTrackingConstant.LIST, ShopPageTrackingConstant.SHOPPAGE_PRODUCT + adapterPosition),
                        ShopPageTrackingConstant.PRODUCTS, DataLayer.listOf(
                                DataLayer.mapOf(
                                        ShopPageTrackingConstant.NAME, name,
                                        ShopPageTrackingConstant.ID, id,
                                        ShopPageTrackingConstant.PRICE, price,
                                        ShopPageTrackingConstant.BRAND, ShopPageTrackingConstant.NONE_OTHER,
                                        ShopPageTrackingConstant.CATEGORY, ShopPageTrackingConstant.NONE_OTHER,
                                        ShopPageTrackingConstant.VARIANT, ShopPageTrackingConstant.NONE_OTHER,
                                        ShopPageTrackingConstant.LIST, ShopPageTrackingConstant.SHOPPAGE_PRODUCT + adapterPosition,
                                        ShopPageTrackingConstant.POSITION, adapterPosition
                                )
                        )
                )
        );
    }

    public static int getShopType(ShopInfoDetail info) {
        return 0;
    }
}
