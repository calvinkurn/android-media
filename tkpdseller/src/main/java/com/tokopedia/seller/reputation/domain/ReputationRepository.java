package com.tokopedia.seller.reputation.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.reputation.domain.model.SellerReputationDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ReputationRepository {
    Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param);

    Observable<SellerReputationDomain> getReputationHistory(RequestParams requestParams);
}
