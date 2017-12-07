package com.tokopedia.seller.opportunity.listener;

import com.tokopedia.seller.opportunity.presentation.ActionViewData;

/**
 * Created by hangnadi on 2/27/17.
 */
public interface OpportunityView {

    void onActionDeleteClicked();

    void onReputationLabelClicked();

    void onActionSeeDetailProduct(String productId);

    String getOpportunityId();

    void showLoadingProgress();

    String getString(int resId);

    void onSuccessTakeOpportunity(ActionViewData actionViewData);

    void onErrorTakeOpportunity(String errorMessage);
}
