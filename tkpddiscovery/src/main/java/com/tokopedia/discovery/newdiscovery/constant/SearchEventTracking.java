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
}
