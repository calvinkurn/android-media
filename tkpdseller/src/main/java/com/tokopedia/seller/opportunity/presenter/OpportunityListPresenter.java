package com.tokopedia.seller.opportunity.presenter;

/**
 * Created by nisie on 3/2/17.
 */

public interface OpportunityListPresenter {
    void getOpportunity();

    void loadMore(int lastItemPosition, int visibleItem);

    void onRefresh();
}
