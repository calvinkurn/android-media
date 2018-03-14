package com.tokopedia.shop.analytic;

import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 3/12/18.
 */

public class ShopPageTracking {

    private final ShopModuleRouter shopModuleRouter;

    public ShopPageTracking(ShopModuleRouter shopModuleRouter) {
        this.shopModuleRouter = shopModuleRouter;
    }

    private void eventShopPageOfficialStore(String category, String action, String label, String shopId) {
        shopModuleRouter.sendEventTrackingShopPage(
                ShopPageTrackingConstant.CLICK_OFFICIAL_STORE,
                category,
                action,
                label,
                shopId
        );
    }

    private void eventShopPageOfficialStoreProductId(String category, String action, String label, String productId) {
        shopModuleRouter.sendEventTrackingShopPage(
                ShopPageTrackingConstant.CLICK_OFFICIAL_STORE,
                category,
                action,
                label,
                productId
        );
    }

    private void eventShopPageOfficialStoreView(String category, String action, String label, String shopId) {
        shopModuleRouter.sendEventTrackingShopPage(
                ShopPageTrackingConstant.VIEW_OFFICIAL_STORE,
                category,
                action,
                label,
                shopId
        );
    }

    public void eventBackPressed(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_ARROW_BACK,
                shopId);
    }

    public void eventClickShopLogo(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_LOGO, shopId);
    }

    public void eventClickShopName(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_NAME, shopId);
    }

    public void eventClickShareShop(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHARE_SHOP, shopId);
    }

    public void eventClickMessageShop(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_MESSAGE_BRAND, shopId);
    }

    public void eventClickFavouriteShop(String titlePage, String shopId, boolean favouriteShop) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_FAVOURITE_SHOP +
                        (favouriteShop ? ShopPageTrackingConstant.UNFAVOURITE : ShopPageTrackingConstant.FAVOURITE),
                shopId);
    }

    public void eventClickListFavourite(String titlePage, String shopId) {
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

    public void eventClickTotalProduct(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_TOTAL_PRODUCTS, shopId);
    }

    public void eventClickShopSpeed(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_GENERAL_CLICK,
                ShopPageTrackingConstant.CLICK_SHOP_SPEED, shopId);
    }

    public void eventClickShopSpeedInfo(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.TOP_SECTION_SHOP_SPEED_CLICK,
                ShopPageTrackingConstant.CLICK_SEE_SHOP_INFO, shopId);
    }

    public void eventClickShopInfo(String titlePage, String shopId) {
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

    public void eventClickShareShopNotePage(String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.TOP_SECTION_SHOP_INFORMATION_CLICK,
                ShopPageTrackingConstant.CLICK_SHARE_NOTE_LIST,
                shopId);
    }

    public void eventClickTabShopPage(String titlePage, String shopId) {
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
                ShopPageTrackingConstant.CLICK_ETALASE,
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

    public void eventClickSeeMoreReview(String titlePage, String shopId) {
        eventShopPageOfficialStore(ShopPageTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER,
                titlePage + ShopPageTrackingConstant.BOTTOM_NAVIGATION_CLICK,
                ShopPageTrackingConstant.CLICK_SEE_MORE,
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

    public void eventClickProductPictureFeaturedImpression(String name, String id, String price, int adapterPosition) {

    }

    public void eventViewProductFeaturedImpression(List<ShopProductViewModel> shopProductViewModelList) {

    }

    public void eventClickProductTitleFeaturedImpression(String name, String id, String price, int adapterPosition) {

    }
}
