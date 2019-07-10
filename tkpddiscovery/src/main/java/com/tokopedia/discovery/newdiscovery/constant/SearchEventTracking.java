package com.tokopedia.discovery.newdiscovery.constant;

public interface SearchEventTracking {

    interface Event {
        String IMAGE_SEARCH_CLICK = "imageSearchClick";
        String EVENT_CLICK_TOP_NAV = "eventTopNav";
        String SEARCH = "clickSearch";
        String SEARCH_RESULT = "clickSearchResult";
        String PRODUCT_VIEW = "productView";
        String PRODUCT_CLICK = "productClick";
        String CLICK_WISHLIST = "clickWishlist";
    }

    interface Category {
        String IMAGE_SEARCH = "image search";
        String EVENT_TOP_NAV = "top nav";
        String SEARCH = "Search";
        String FILTER_PRODUCT = "filter product";
        String SEARCH_RESULT = "Search Result";
        String IMAGE_SEARCH_RESULT = "image search result";
        String SEARCH_SHARE = "search share";
        String GRID_MENU = "grid menu";
        String SEARCH_TAB = "search tab";
        String FILTER_JOURNEY = "filter journey";
        String SORT = "Sort";
        String FILTER = "Filter";
        String SORT_BY = "sort by";
        String EventSearchResult = "search result";
    }

    interface Action {
        String CLICK = "Click";
        String EXTERNAL_IMAGE_SEARCH = "click image search external";
        String SEARCH_PRODUCT = "search product";
        String SEARCH_SHOP = "search shop";
        String VOICE_SEARCH = "Voice Search";
        String GALLERY_SEARCH_RESULT = "query search by gallery";
        String CAMERA_SEARCH_RESULT = "query search by camera";
        String QUICK_FILTER = "quick filter";
        String CLICK_WISHLIST = "click - wishlist";
        String CLICK_PRODUCT = "click - product";
        String CLICK_SHOP = "click - shop";
        String CLICK_BAR = "click - bar - ";
        String CLICK_CHANGE_GRID = "click - ";
        String FAVORITE_SHOP_CLICK = "click - favoritkan";
        String CLICK_CATALOG = "click - catalog";
        String CLICK_TAB = "click - tab";
        String FILTER = "Filter";
        String APPLY_FILTER = "apply filter";
        String SIMPAN_ON_LIHAT_SEMUA = "click simpan on lihat semua ";
        String BACK_ON_LIHAT_SEMUA = "click back on lihat semua ";
        String CLICK_LIHAT_SEMUA = "click lihat semua ";
        String NO_SEARCH_RESULT = "no search result";
        String CLICK_FILTER = "click filter";
        String SORT_BY = "sort by";
        String EventLongPressProduct = "click - long press product";
        String SEARCH_IMAGE_PICKER_CLICK_CAMERA = "click image search by camera";
        String SEARCH_IMAGE_PICKER_CLICK_GALLERY = "click image search by gallery";
    }

    interface Label {
        String LabelKeywordProduct = "Keyword: %s - product id: %s";
    }

    interface MOENGAGE {
        String KEYWORD = "keyword";
        String IS_RESULT_FOUND = "is_result_found";
        String CATEGORY_ID_MAPPING = "category_id_mapping";
        String CATEGORY_NAME_MAPPING = "category_name_mapping";
    }

    interface EventMoEngage {
        String SEARCH_ATTEMPT = "Search_Attempt";
    }
}
