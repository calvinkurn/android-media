package com.tokopedia.seller.opportunity.listener;

import android.app.Activity;

import com.tokopedia.seller.opportunity.adapter.OpportunityListAdapter;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityListPageViewModel;

/**
 * Created by nisie on 3/2/17.
 */
public interface OpportunityListView {
    void showLoadingList();

    void onSuccessGetOpportunity(OpportunityListPageViewModel viewModel);

    String getString(int resId);

    void onErrorGetOpportunity(String errorMessage);

    OpportunityListAdapter getAdapter();

    String getSortParam();

    String getShippingParam();

    String getCategoryParam();

    Activity getActivity();
}
