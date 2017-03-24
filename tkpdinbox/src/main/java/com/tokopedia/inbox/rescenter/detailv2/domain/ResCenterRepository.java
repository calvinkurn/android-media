package com.tokopedia.inbox.rescenter.detailv2.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;
import com.tokopedia.inbox.rescenter.historyawb.domain.model.HistoryAwbData;

import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public interface ResCenterRepository {

    Observable<DetailResCenter> getDetail(TKPDMapParam<String, Object> parameters);

    Observable<DetailResCenter> getConversation(TKPDMapParam<String, Object> parameters);

    Observable<DetailResCenter> getConversationMore(String conversationID, TKPDMapParam<String, Object> parameters);

    Observable<HistoryAwbData> getHistoryAwb(TKPDMapParam<String, Object> parameters);

    Observable<TrackingAwbReturProduct> getTrackingAwbReturProduct(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> cancelResolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> askHelpResolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> finishReturSolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> acceptAdminSolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> acceptSolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> inputAddress(TKPDMapParam<String, Object> parameters);
}
