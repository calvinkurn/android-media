package com.tokopedia.seller.opportunity.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.opportunity.data.OpportunityNewPriceData;
import com.tokopedia.seller.opportunity.presentation.ActionViewData;

/**
 * Created by hangnadi on 2/27/17.
 */
public interface OpportunityView extends CustomerView {

    void onActionDeleteClicked();

    void onReputationLabelClicked();

    void onActionSeeDetailProduct(String productId);

    String getOpportunityId();

    void showLoadingProgress();

    String getString(int resId);

    void onSuccessTakeOpportunity(ActionViewData actionViewData);

    void onSuccessNewPrice(OpportunityNewPriceData opportunityNewPriceData);

    void onErrorTakeOpportunity(String errorMessage);

    void onErrorPriceInfo(String errorMessage);
}
