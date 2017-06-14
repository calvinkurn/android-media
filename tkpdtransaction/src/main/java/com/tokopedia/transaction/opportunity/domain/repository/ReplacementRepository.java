package com.tokopedia.transaction.opportunity.domain.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.opportunity.data.model.CancelReplacementModel;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */
public interface ReplacementRepository {

    Observable<CancelReplacementModel> cancelReplacement(TKPDMapParam<String, Object> parameters);
}
