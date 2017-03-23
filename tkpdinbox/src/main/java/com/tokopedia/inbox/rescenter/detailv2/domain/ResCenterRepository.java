package com.tokopedia.inbox.rescenter.detailv2.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;

import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public interface ResCenterRepository {

    Observable<DetailResCenter> getDetail(TKPDMapParam<String, Object> parameters);

    Observable<DetailResCenter> getConversation();

    Observable<Object> getConversationMore();

    Observable<TrackingAwbReturProduct> getTrackingAwbReturProduct(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> cancelResolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> askHelpResolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> finishReturSolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> acceptAdminSolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> acceptSolution(TKPDMapParam<String, Object> parameters);
}
