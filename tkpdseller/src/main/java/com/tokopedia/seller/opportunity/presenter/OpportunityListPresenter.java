package com.tokopedia.seller.opportunity.presenter;

/**
 * Created by nisie on 3/2/17.
 */

public interface OpportunityListPresenter {
    void getOpportunity();

    void loadMore();

    void onRefresh();

    void onDestroyView();

    void setParamQuery(String query);

    void setParamSort(String sortParam);

    void setParamCategory(String categoryParam);

    void getParamShippingType(String shippingParam);

}
