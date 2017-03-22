package com.tokopedia.seller.opportunity.listener;

import com.tokopedia.seller.opportunity.presentation.ActionViewData;

/**
 * Created by hangnadi on 2/27/17.
 */
public interface OppurtunityView {

    void onActionDeleteClicked();

    void onActionSubmitClicked();

    void onActionSeeDetailProduct(String productId);

    ActionViewData getActionViewData();

    void setActionViewData(ActionViewData actionViewData);

    void setOnAcceptOppurtunityComplete();

}
