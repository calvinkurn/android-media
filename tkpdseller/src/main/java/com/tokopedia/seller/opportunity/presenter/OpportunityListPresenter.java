package com.tokopedia.seller.opportunity.presenter;

import android.support.annotation.Nullable;

import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;

/**
 * Created by nisie on 3/2/17.
 */

public interface OpportunityListPresenter {
    void getOpportunity(@Nullable String query,
                        @Nullable ArrayList<FilterPass> listFilter);

    void unsubscribeObservable();

    void initOpportunityForFirstTime(@Nullable String query,
                                     @Nullable ArrayList<FilterPass> listFilter);
}
