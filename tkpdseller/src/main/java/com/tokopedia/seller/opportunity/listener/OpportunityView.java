package com.tokopedia.seller.opportunity.listener;

import com.tokopedia.seller.opportunity.presentation.ActionViewData;

/**
 * Created by hangnadi on 2/27/17.
 */
public interface OpportunityView {

    void onActionDeleteClicked();

    void onActionConfirmClicked();

    void onActionSeeDetailProduct(String productId);

    String getOpportunityId();

    void showLoadingProgress();

    void onSuccessTakeOpportunity(ActionViewData actionViewData);

    String getString(int resId);

    void onErrorTakeOpportunity(String errorMessage);
}
