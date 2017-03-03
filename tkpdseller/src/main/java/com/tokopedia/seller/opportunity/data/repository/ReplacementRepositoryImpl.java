package com.tokopedia.seller.opportunity.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.ReplacementDataSourceFactory;
import com.tokopedia.seller.opportunity.domain.ReplacementRepository;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */

public class ReplacementRepositoryImpl implements ReplacementRepository {

    private final ActionReplacementSourceFactory actionReplacementSourceFactory;

    public ReplacementRepositoryImpl(ActionReplacementSourceFactory actionReplacementSourceFactory,
                                     ReplacementDataSourceFactory replacementDataSourceFactory) {
        this.actionReplacementSourceFactory = actionReplacementSourceFactory;
    }

    @Override
    public Observable<AcceptReplacementModel> acceptReplacement(TKPDMapParam<String, Object> parameters) {
        return actionReplacementSourceFactory.createCloudActionReplacementSource()
                .acceptReplacement(parameters);
    }

}
