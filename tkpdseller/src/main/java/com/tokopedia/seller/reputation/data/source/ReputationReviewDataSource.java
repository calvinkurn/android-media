package com.tokopedia.seller.reputation.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.reputation.domain.model.SellerReputationDomain;

import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 3/16/17.
 */

public interface ReputationReviewDataSource {
    Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param);

    Observable<SellerReputationDomain> getReputationHistory(RequestParams requestParams);
}
