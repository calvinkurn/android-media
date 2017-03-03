package com.tokopedia.seller.opportunity.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */
public interface ReplacementRepository {

    Observable<AcceptReplacementModel> acceptReplacement(TKPDMapParam<String, Object> parameters);

}
