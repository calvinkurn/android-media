package com.tokopedia.shop.analytic;

import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoDetail;
import com.tokopedia.shop.common.util.TextApiUtils;
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

    private void eventShopPageOfficialStore(String action, String label, String shopId, boolean myShop, int shopType) {
        HashMap<String, Object> eventMap = createEventMap(getEventNameCLick(myShop, shopType), getEventCategory(myShop, shopType),
                action, label, shopType);
        eventMap.put(ShopPageTrackingConstant.SHOP_ID, shopId);
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    private void eventShopPageOfficialStoreProductId(String action, String label, String productId, boolean myShop, int shopType) {
        HashMap<String, Object> eventMap = createEventMap(getEventNameCLick(myShop, shopType), getEventCategory(myShop, shopType),
                action, label, shopType);
        eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, productId);
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    private void eventShopPageOfficialStoreView(String action, String label, String shopId, boolean myShop, int shopType) {
        HashMap<String, Object> eventMap = createEventMap(getEventNameView(myShop, shopType), getEventCategory(myShop, shopType),
                action, label, shopType);
        eventMap.put(ShopPageTrackingConstant.SHOP_ID, shopId);
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    private HashMap<String, Object> createEventMap(String event, String category, String action, String label, int shopType) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(ShopPageTrackingConstant.EVENT, event);
        eventMap.put(ShopPageTrackingConstant.EVENT_CATEGORY, category);
        eventMap.put(ShopPageTrackingConstant.EVENT_ACTION, action);
        eventMap.put(ShopPageTrackingConstant.EVENT_LABEL, label);
        eventMap.put(ShopPageTrackingConstant.SHOP_TYPE, getShopTypeName(shopType));
        return eventMap;
    }

    private String getShopTypeName(int shopType) {
        switch (shopType) {
            case ShopPageTrackingConstant.OFFICIAL_STORE:
                return ShopPageTrackingConstant.OFFICIAL_STORE_NAME;
            case ShopPageTrackingConstant.GOLD_MERCHANT:
                return ShopPageTrackingConstant.GOLD_MERCHANT_NAME;
            case ShopPageTrackingConstant.REGULAR_MERCHANT:
                return ShopPageTrackingConstant.REGULAR_MERCHANT_NAME;
            default:
                return ShopPageTrackingConstant.REGULAR_MERCHANT_NAME;
        }
    }

    public void eventBackPressed(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_ARROW_BACK,
                shopId, myShop, shopType);
    }

    public void eventClickShopLogo(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_LOGO, shopId, myShop, shopType);
    }

    public void eventClickShopName(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_NAME, shopId, myShop, shopType);
    }

    public void eventClickShareShop(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHARE_SHOP, shopId, myShop, shopType);
    }

    public void eventClickMessageShop(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_MESSAGE_BRAND, shopId, myShop, shopType);
    }

    public void eventClickFavouriteShop(String titlePage, String shopId, boolean favouriteShop, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_FAVOURITE_SHOP +
                        (favouriteShop ? ShopPageTrackingConstant.UNFAVOURITE : ShopPageTrackingConstant.FAVOURITE),
                shopId, myShop, shopType);
    }

    public void eventClickListFavourite(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_LIST_OF_FAVOURITE,
                shopId, myShop, shopType);
    }

    public void eventClickUserFavouritingShop(String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                ShopPageTrackingConstant.TOP_SECTION_LIST_FAVOURITE_CLICK,
                ShopPageTrackingConstant.CLICK_USER_FAVOURITING_SHOP,
                shopId, myShop, shopType);
    }

    public void eventCloseListFavourite(String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                ShopPageTrackingConstant.TOP_SECTION_LIST_FAVOURITE_CLICK,
                ShopPageTrackingConstant.CLICK_CLOSE_FAVOURITE,
                shopId, myShop, shopType);
    }

    public void eventClickTotalProduct(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_TOTAL_PRODUCTS, shopId, myShop, shopType);
    }

    public void eventClickShopSpeed(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_SPEED, shopId, myShop, shopType);
    }

    public void eventClickShopSpeedInfo(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_SHOP_SPEED_CLICK,
                ShopPageTrackingConstant.CLICK_SEE_SHOP_INFO, shopId, myShop, shopType);
    }

    public void eventClickShopInfo(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_INFO,
                shopId, myShop, shopType);
    }

    public void eventClickTabShopInfo(CharSequence titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_TOP_TAB + titlePage,
                shopId, myShop, shopType);
    }


    public void eventClickNoteList(long position, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_NOTE_LIST + String.valueOf(position + 1),
                shopId, myShop, shopType);
    }

    public void eventBackPressedShopInfo(String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_ARROW_BACK_TO_SHOP,
                shopId, myShop, shopType);
    }

    public void eventClickShareShopNotePage(String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_SHARE_NOTE_LIST,
                shopId, myShop, shopType);
    }

    public void eventClickTabShopPage(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_TOP_CONTENT_TAB + titlePage,
                shopId, myShop, shopType);
    }

    public void eventClickSearchProduct(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.ACTION_SEARCH_BAR_CLICK,
                ShopPageTrackingConstant.ClICK_SEARCH_BAR,
                shopId, myShop, shopType);
    }

    public void eventTypeKeywordSearchProduct(String titlePage, String keyword, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.ACTION_SEARCH_BAR_TYPE,
                ShopPageTrackingConstant.TYPE_KEYWORDS + keyword,
                shopId, myShop, shopType);
    }

    public void eventClickWishlistShopPageFeatured(String titlePage, boolean wishList, String producId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_WISHLIST + (wishList ? ShopPageTrackingConstant.ADD_WISHLIST : ShopPageTrackingConstant.REMOVE_WISHLIST),
                producId, myShop, shopType);
    }

    public void eventViewShopPage(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_IMPRESSION,
                "", shopId, myShop, shopType);
    }

    public void eventClickWishlistShop(String titlePage, boolean wishList, boolean isFromHomeShop, String productId, boolean myShop, int shopType) {
        eventShopPageOfficialStoreProductId(
                titlePage + (isFromHomeShop ? ShopPageTrackingConstant.PRODUCT_LIST : ShopPageTrackingConstant.PRODUCT_PAGE) + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_WISHLIST + (wishList ? ShopPageTrackingConstant.ADD_WISHLIST : ShopPageTrackingConstant.REMOVE_WISHLIST),
                productId, myShop, shopType);
    }

    public void eventClickEtalaseShop(String titlePage, boolean isFromHomeShop, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + (isFromHomeShop ? ShopPageTrackingConstant.PRODUCT_LIST : ShopPageTrackingConstant.PRODUCT_PAGE) + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_ETALASE,
                shopId, myShop, shopType);
    }

    public void eventClickEtalaseShopChoose(String titlePage, boolean isFromHomeShop, String etalaseName, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + (isFromHomeShop ? ShopPageTrackingConstant.PRODUCT_LIST : ShopPageTrackingConstant.PRODUCT_PAGE) + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_MENU + etalaseName,
                shopId, myShop, shopType);
    }

    public void eventClickSortProductList(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.BOTTOM_NAVIGATION_CLICK,
                ShopPageTrackingConstant.CLICK_SORT,
                shopId, myShop, shopType);
    }


    public void eventClickViewTypeProduct(String titlePage, int index, String shopId, boolean myShop, int shopType) {
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
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.BOTTOM_NAVIGATION_CLICK,
                ShopPageTrackingConstant.CLICK_PRODUCT_VIEW + viewTypeName,
                shopId, myShop, shopType);
    }

    public void eventClickChooseSort(String titlePage, String sortName, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.BOTTOM_NAVIGATION_PRODUCT_SORT_CLICK,
                ShopPageTrackingConstant.CLICK_SORT_BY + sortName,
                shopId, myShop, shopType);
    }

    public void eventClickSeeMoreProduct(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.BOTTOM_NAVIGATION_CLICK,
                ShopPageTrackingConstant.CLICK_VIEW_MORE_PRODUCT,
                shopId, myShop, shopType);
    }

    public void eventViewBottomNavigation(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStoreView(
                titlePage + ShopPageTrackingConstant.BOTTOM_NAVIGATION_IMPRESSION,
                "",
                shopId, myShop, shopType);
    }

    public void eventClickAddProduct(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_ADD_PRODUCT,
                shopId, myShop, shopType);
    }

    public void eventClickShopSetting(String titlePage, String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_SETTING,
                shopId, myShop, shopType);
    }

    public void eventClickAddNote(String shopId, boolean myShop, int shopType) {
        eventShopPageOfficialStore(
                ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_ADD_NOTE,
                shopId, myShop, shopType);
    }

    public void eventClickProductPictureFeaturedImpression(String titlePage, String name, String id, String price, int adapterPosition, boolean myShop, int shopType) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.PRODUCT_CLICK,
                getEventCategory(myShop, shopType),
                titlePage + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_PRODUCT_PICTURE, shopType);
        eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, id);
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapProductClickImpression(name, id, price, adapterPosition, false));
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    public void eventViewProductFeaturedImpression(String titlePage, List<ShopProductViewModel> shopProductViewModelList, boolean myShop, int shopType, boolean isGrid) {
        try {
            HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.PRODUCT_VIEW,
                    getEventCategory(myShop, shopType),
                    titlePage + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                    ShopPageTrackingConstant.IMPRESSION_OF_TOP_PRODUCT_LIST, shopType);
            eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, "");
            eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapProductViewImpression(shopProductViewModelList, isGrid));
            shopModuleRouter.sendEventTrackingShopPage(eventMap);
        } catch (Exception e) {
            e.printStackTrace();
            CommonUtils.dumper("GAv4 " + e.getMessage());
        }
    }

    public void eventViewProductImpression(String titlePage, List<ShopProductViewModel> shopProductViewModelList, boolean isFromHomeShop, boolean myShop, int shopType, boolean isGrid) {
        try {
            HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.PRODUCT_VIEW,
                    getEventCategory(myShop, shopType),
                    titlePage + " - " + (isFromHomeShop ? ShopPageTrackingConstant.PRODUCT_LIST : ShopPageTrackingConstant.PRODUCT_PAGE)
                            + " - " + ShopPageTrackingConstant.IMPRESSION,
                    ShopPageTrackingConstant.IMPRESSION_OF_PRODUCT_PICTURES, shopType);
            eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, "");
            eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapProductViewImpression(shopProductViewModelList, isGrid));
            shopModuleRouter.sendEventTrackingShopPage(eventMap);
        } catch (Exception e) {
            e.printStackTrace();
            CommonUtils.dumper("GAv4 " + e.getMessage());
        }
    }

    public void eventClickProductTitleFeaturedImpression(String titlePage, String name, String id, String price, int adapterPosition, boolean myShop, int shopType) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.PRODUCT_CLICK,
                getEventCategory(myShop, shopType),
                titlePage + ShopPageTrackingConstant.TOP_PRODUCTS_CLICK,
                ShopPageTrackingConstant.CLICK_PRODUCT_NAME, shopType);
        eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, id);
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapProductClickImpression(name, id, price, adapterPosition, false));
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }


    public void eventClickProductImpression(String titlePage, String name, String id, String price, int adapterPosition, boolean isFromHomeShop, boolean myShop, int shopType, boolean isGrid) {
        try {
            HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.PRODUCT_CLICK,
                    getEventCategory(myShop, shopType),
                    titlePage + " - " + (isFromHomeShop ? ShopPageTrackingConstant.PRODUCT_LIST : ShopPageTrackingConstant.PRODUCT_PAGE)
                            + " - " + ShopPageTrackingConstant.CLICK,
                    ShopPageTrackingConstant.CLICK_PRODUCT_PICTURE, shopType);
            eventMap.put(ShopPageTrackingConstant.PRODUCT_ID, id);
            eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapProductClickImpression(name, id, price, adapterPosition, isGrid));
            shopModuleRouter.sendEventTrackingShopPage(eventMap);
        } catch (Exception e) {
            e.printStackTrace();
            CommonUtils.dumper("GAv4 " + e.getMessage());
        }
    }

    public void eventClickBannerImpression(String titlePage, String shopName, String shopId, boolean myShop, int shopType) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.PROMO_CLICK,
                getEventCategory(myShop, shopType),
                titlePage + ShopPageTrackingConstant.TOP_CONTENT_CLICK,
                ShopPageTrackingConstant.CLICK_TOP_CONTENT, shopType);
        eventMap.put(ShopPageTrackingConstant.SHOP_ID, shopId);
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapBannerClickImpression(shopName));
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    public void eventViewBannerImpression(String titlePage, String shopName, String shopId, boolean myShop, int shopType) {
        HashMap<String, Object> eventMap = createEventMap(ShopPageTrackingConstant.PROMO_VIEW,
                getEventCategory(myShop, shopType),
                titlePage + ShopPageTrackingConstant.TOP_CONTENT_IMPRESSION,
                "", shopType);
        eventMap.put(ShopPageTrackingConstant.SHOP_ID, shopId);
        eventMap.put(ShopPageTrackingConstant.ECOMMERCE, createMapBannerViewImpression(shopName));
        shopModuleRouter.sendEventTrackingShopPage(eventMap);
    }

    private Map<String, Object> createMapBannerViewImpression(String shopName) {
        return DataLayer.mapOf(
                ShopPageTrackingConstant.PROMO_VIEW, DataLayer.mapOf(
                        ShopPageTrackingConstant.PROMOTIONS, DataLayer.listOf(
                                DataLayer.mapOf(
                                        ShopPageTrackingConstant.NAME, ShopPageTrackingConstant.SHOP_PAGE_PROMO_WEBVIEW,
                                        ShopPageTrackingConstant.CREATIVE, shopName,
                                        ShopPageTrackingConstant.POSITION, 1
                                )
                        )
                )
        );
    }

    private Map<String, Object> createMapBannerClickImpression(String shopName) {
        return DataLayer.mapOf(
                ShopPageTrackingConstant.PROMO_CLICK, DataLayer.mapOf(
                        ShopPageTrackingConstant.PROMOTIONS, DataLayer.listOf(
                                DataLayer.mapOf(
                                        ShopPageTrackingConstant.NAME, ShopPageTrackingConstant.SHOP_PAGE_PROMO_WEBVIEW,
                                        ShopPageTrackingConstant.CREATIVE, shopName,
                                        ShopPageTrackingConstant.POSITION, 1
                                )
                        )
                )
        );
    }

    private Map<String, Object> createMapProductViewImpression(List<ShopProductViewModel> shopProductViewModelList, boolean isGrid) {
        List<Object> list = getListProductAsObjectDataLayer(shopProductViewModelList, isGrid);
        return DataLayer.mapOf(
                ShopPageTrackingConstant.CURRENCY_CODE, ShopPageTrackingConstant.IDR,
                ShopPageTrackingConstant.IMPRESSIONS, DataLayer.listOf(
                        list.toArray(new Object[list.size()])
                ));
    }

    public List<Object> getListProductAsObjectDataLayer(List<ShopProductViewModel> shopProductViewModelList, boolean isGrid) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < shopProductViewModelList.size(); i++) {
            ShopProductViewModel viewModel = shopProductViewModelList.get(i);
            list.add(
                    DataLayer.mapOf(
                            ShopPageTrackingConstant.NAME, viewModel.getName(),
                            ShopPageTrackingConstant.ID, viewModel.getId(),
                            ShopPageTrackingConstant.PRICE, formatPrice(viewModel.getDisplayedPrice()),
                            ShopPageTrackingConstant.BRAND, ShopPageTrackingConstant.NONE_OTHER,
                            ShopPageTrackingConstant.CATEGORY, ShopPageTrackingConstant.NONE_OTHER,
                            ShopPageTrackingConstant.VARIANT, ShopPageTrackingConstant.NONE_OTHER,
                            ShopPageTrackingConstant.LIST, ShopPageTrackingConstant.SHOPPAGE_PRODUCT + (isGrid ? Math.ceil((i + 1) / 2) : i + 1),
                            ShopPageTrackingConstant.POSITION, (isGrid ? Math.ceil((i + 1) / 2) : i + 1)
                    )
            );
        }
        return list;
    }

    private String formatPrice(String displayedPrice) {
        if(!TextUtils.isEmpty(displayedPrice)){
            displayedPrice.replace(".", "");
            displayedPrice.replace("Rp", "");
            return displayedPrice;
        }else{
            return "";
        }
    }

    private Map<String, Object> createMapProductClickImpression(String name, String id, String price, int adapterPosition, boolean isGrid) {
        return DataLayer.mapOf(
                ShopPageTrackingConstant.CLICK, DataLayer.mapOf(
                        ShopPageTrackingConstant.ACTION_FIELD, DataLayer.mapOf(ShopPageTrackingConstant.LIST, ShopPageTrackingConstant.SHOPPAGE_PRODUCT + (adapterPosition + 1)),
                        ShopPageTrackingConstant.PRODUCTS, DataLayer.listOf(
                                DataLayer.mapOf(
                                        ShopPageTrackingConstant.NAME, name,
                                        ShopPageTrackingConstant.ID, id,
                                        ShopPageTrackingConstant.PRICE, formatPrice(price),
                                        ShopPageTrackingConstant.BRAND, ShopPageTrackingConstant.NONE_OTHER,
                                        ShopPageTrackingConstant.CATEGORY, ShopPageTrackingConstant.NONE_OTHER,
                                        ShopPageTrackingConstant.VARIANT, ShopPageTrackingConstant.NONE_OTHER,
                                        ShopPageTrackingConstant.LIST, ShopPageTrackingConstant.SHOPPAGE_PRODUCT + (isGrid ? Math.ceil((adapterPosition + 1) / 2) : adapterPosition + 1),
                                        ShopPageTrackingConstant.POSITION, (isGrid ? Math.ceil((adapterPosition + 1) / 2) : adapterPosition + 1)
                                )
                        )
                )
        );
    }

    public static int getShopType(ShopInfoDetail info) {
        if (info != null) {
            if (TextApiUtils.isValueTrue(info.getShopIsOfficial())) {
                return ShopPageTrackingConstant.OFFICIAL_STORE;
            } else if (info.isShopIsGoldBadge()) {
                return ShopPageTrackingConstant.GOLD_MERCHANT;
            } else {
                return ShopPageTrackingConstant.REGULAR_MERCHANT;
            }
        } else {
            return 0;
        }
    }

    private String getEventNameView(boolean myShop, int shopType) {
        switch (shopType) {
            case ShopPageTrackingConstant.OFFICIAL_STORE:
                return ShopPageTrackingConstant.VIEW_OFFICIAL_STORE;
            case ShopPageTrackingConstant.GOLD_MERCHANT:
            case ShopPageTrackingConstant.REGULAR_MERCHANT:
            default:
                return ShopPageTrackingConstant.VIEW_SHOP_PAGE;
        }
    }

    private String getEventNameCLick(boolean myShop, int shopType) {
        switch (shopType) {
            case ShopPageTrackingConstant.OFFICIAL_STORE:
                return ShopPageTrackingConstant.CLICK_OFFICIAL_STORE;
            case ShopPageTrackingConstant.GOLD_MERCHANT:
            case ShopPageTrackingConstant.REGULAR_MERCHANT:
            default:
                return ShopPageTrackingConstant.CLICK_SHOP_PAGE;
        }
    }

    private String getEventCategory(boolean myShop, int shopType) {
        if (myShop) {
            switch (shopType) {
                case ShopPageTrackingConstant.OFFICIAL_STORE:
                    return ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BRAND;
                case ShopPageTrackingConstant.GOLD_MERCHANT:
                case ShopPageTrackingConstant.REGULAR_MERCHANT:
                default:
                    return ShopPageTrackingConstant.SHOP_PAGE_SELLER;
            }
        } else {
            switch (shopType) {
                case ShopPageTrackingConstant.OFFICIAL_STORE:
                    return ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER;
                case ShopPageTrackingConstant.GOLD_MERCHANT:
                case ShopPageTrackingConstant.REGULAR_MERCHANT:
                default:
                    return ShopPageTrackingConstant.SHOP_PAGE_BUYER;
            }
        }
    }
}
