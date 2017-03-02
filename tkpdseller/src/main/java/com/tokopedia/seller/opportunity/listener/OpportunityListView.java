package com.tokopedia.seller.opportunity.listener;

import com.tokopedia.seller.opportunity.adapter.OpportunityListAdapter;

/**
 * Created by nisie on 3/2/17.
 */
public interface OpportunityListView {
    void showLoadingList();

    void onSuccessGetOpportunity();

    String getString(int resId);

    void onErrorGetOpportunity(String errorMessage);

    OpportunityListAdapter getAdapter();

    String getSortParam();

    String getShippingParam();

    String getCategoryParam();
}
