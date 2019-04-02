package com.tokopedia.seller.opportunity.presenter;

import android.support.annotation.Nullable;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;

/**
 * Created by nisie on 3/2/17.
 */

public abstract class OpportunityListPresenter extends BaseDaggerPresenter<OpportunityListView> {
    public abstract void getOpportunity(@Nullable String query,
                        @Nullable ArrayList<FilterPass> listFilter);

    public abstract void unsubscribeObservable();

    public abstract void initOpportunityForFirstTime(@Nullable String query,
                                     @Nullable ArrayList<FilterPass> listFilter);
}
