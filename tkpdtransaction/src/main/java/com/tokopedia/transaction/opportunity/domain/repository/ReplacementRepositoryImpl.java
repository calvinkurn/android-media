package com.tokopedia.transaction.opportunity.domain.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.transaction.opportunity.data.model.CancelReplacementModel;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */

public class ReplacementRepositoryImpl implements ReplacementRepository {

    private final ActionReplacementSourceFactory actionReplacementSourceFactory;

    public ReplacementRepositoryImpl(ActionReplacementSourceFactory actionReplacementSourceFactory) {
        this.actionReplacementSourceFactory = actionReplacementSourceFactory;
    }

    @Override
    public Observable<CancelReplacementModel> cancelReplacement(TKPDMapParam<String, Object> parameters) {
        return actionReplacementSourceFactory.createCloudCancelReplacementSource()
                .cancelReplacement(parameters);
    }

}
