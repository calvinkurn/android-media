package com.tokopedia.discovery.newdiscovery.constant;

public interface SearchEventTracking {

    interface Event {
        String IMAGE_SEARCH_CLICK = "imageSearchClick";
        String EVENT_CLICK_TOP_NAV = "eventTopNav";
        String SEARCH = "clickSearch";
    }

    interface Category {
        String IMAGE_SEARCH = "image search";
        String EVENT_TOP_NAV = "top nav";
        String SEARCH = "Search";
    }

    interface Action {
        String EXTERNAL_IMAGE_SEARCH = "click image search external";
        String SEARCH_PRODUCT = "search product";
        String SEARCH_SHOP = "search shop";
        String VOICE_SEARCH = "Voice Search";
        String GALLERY_SEARCH_RESULT = "query search by gallery";
        String CAMERA_SEARCH_RESULT = "query search by camera";
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
