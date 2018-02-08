package com.tokopedia.seller.opportunity.analytics;

/**
 * @author by nisie on 9/18/17.
 */

public interface OpportunityTrackingEventLabel {

    interface EventName {
        String SCROLL_OPPORTUNITY = "scrollPeluang";
        String SUBMIT_OPPORTUNITY = "submitPeluang";
        String CLICK_OPPORTUNITY_TAKE = "clickPeluang";
        String CLICK_OPPORTUNITY_PRODUCT = "clickPeluang";
        String CLICK_OPPORTUNITY_TAKE_YES = "clickPeluang";
        String CLICK_OPPORTUNITY_TAKE_NO = "clickPeluang";
        String LOAD_OPPORTUNITY_PRODUCT = "loadPeluang";
    }

    interface EventCategory {
        String OPPORTUNITY_FILTER = "Peluang Filter";
    }

    interface EventLabel {
        String NAVIGATE_PAGE = "Navigate page";
        String TAKE_OPPORTUNITY = "Ambil Peluang";
        String SEE_PRODUCT = "Lihat Produk";
        String YES = "Ya";
        String NO = "Tidak";

        String SEARCH = "searchPeluang";
    }

    interface CustomDimension {
        String SEARCH = "search";
    }
}
