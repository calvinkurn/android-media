package com.tokopedia.discovery.newdiscovery.constant;

public interface DiscoveryEventTracking {

    interface Event {
        String EVENT_VIEW_SEARCH_RESULT = "viewSearchResult";
        String SEARCH = "clickSearch";
        String SEARCH_RESULT = "clickSearchResult";
    }

    interface Category {
        String IMAGE_SEARCH = "image search";
        String EVENT_TOP_NAV = "top nav";
        String SEARCH = "Search";
        String SEARCH_RESULT = "Search Result";
        String IMAGE_SEARCH_RESULT = "image search result";
        String SEARCH_SHARE = "search share";
        String GRID_MENU = "grid menu";
        String SORT = "Sort";
    }

    interface Action {
        String EXTERNAL_IMAGE_SEARCH = "click image search external";
        String VOICE_SEARCH = "Voice Search";
        String GALLERY_SEARCH_RESULT = "query search by gallery";
        String CAMERA_SEARCH_RESULT = "query search by camera";
        String CLICK_PRODUCT = "click - product";
        String CLICK_BAR = "click - bar - ";
        String CLICK_CHANGE_GRID = "click - ";
        String CLICK_CATALOG = "click - catalog";
        String NO_SEARCH_RESULT_WITH_TAB = "no search result - tab: %s";
        String SEARCH_IMAGE_PICKER_CLICK_CAMERA = "click image search by camera";
        String SEARCH_IMAGE_PICKER_CLICK_GALLERY = "click image search by gallery";
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
