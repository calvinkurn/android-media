package com.tokopedia.seller.opportunity.presenter;

import com.tokopedia.seller.opportunity.domain.param.GetOpportunityListParam;

/**
 * Created by nisie on 3/2/17.
 */

public interface OpportunityListPresenter {
    void getOpportunity();

    void unsubscribeObservable();

    GetOpportunityListParam getPass();

    void getFilter();
}
