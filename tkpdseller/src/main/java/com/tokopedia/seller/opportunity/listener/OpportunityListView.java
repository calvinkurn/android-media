package com.tokopedia.seller.opportunity.listener;

import android.app.Activity;

import com.tokopedia.seller.opportunity.adapter.OpportunityListAdapter;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityFilterViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityViewModel;

/**
 * Created by nisie on 3/2/17.
 */
public interface OpportunityListView {
    void showLoadingList();

    void onSuccessGetOpportunity(OpportunityViewModel viewModel);

    String getString(int resId);

    void onErrorGetOpportunity(String errorMessage);

    OpportunityListAdapter getAdapter();

    String getSortParam();

    String getShippingParam();

    String getCategoryParam();

    Activity getActivity();

    boolean isFilterEmpty();

    int getPage();

    void onSuccessGetFilter(OpportunityFilterViewModel opportunityFilterViewModel);

    void onErrorGetFilter(String errorMessage);
}
