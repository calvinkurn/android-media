package com.tokopedia.seller.opportunity.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.opportunity.listener.OpportunityView;

/**
 * Created by hangnadi on 2/27/17.
 */
public abstract class OpportunityPresenter extends BaseDaggerPresenter<OpportunityView>{
    public abstract void getNewPriceInfo();

    public abstract void acceptOpportunity();

    public abstract void unsubscribeObservable();

}
