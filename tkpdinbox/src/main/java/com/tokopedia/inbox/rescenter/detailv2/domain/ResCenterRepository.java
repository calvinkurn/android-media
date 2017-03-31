package com.tokopedia.inbox.rescenter.detailv2.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;
import com.tokopedia.inbox.rescenter.discussion.domain.model.ActionDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.LoadMoreModel;
import com.tokopedia.inbox.rescenter.historyaction.domain.model.HistoryActionData;
import com.tokopedia.inbox.rescenter.historyaddress.domain.model.HistoryAddressData;
import com.tokopedia.inbox.rescenter.historyawb.domain.model.HistoryAwbData;
import com.tokopedia.inbox.rescenter.product.domain.model.ListProductDomainData;
import com.tokopedia.inbox.rescenter.product.domain.model.ProductDetailData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.DiscussionModel;

import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public interface ResCenterRepository {

    Observable<DetailResCenter> getDetail(TKPDMapParam<String, Object> parameters);

    Observable<DiscussionModel> getConversation(TKPDMapParam<String, Object> parameters);

    Observable<LoadMoreModel> getConversationMore(TKPDMapParam<String, Object> parameters);

    Observable<HistoryAwbData> getHistoryAwb(TKPDMapParam<String, Object> parameters);

    Observable<HistoryAddressData> getHistoryAddress(TKPDMapParam<String, Object> parameters);

    Observable<HistoryActionData> getHistoryAction(TKPDMapParam<String, Object> parameters);

    Observable<ListProductDomainData> getListProduct(TKPDMapParam<String, Object> parameters);

    Observable<ProductDetailData> getDetailProduct(String troubleID, TKPDMapParam<String, Object> parameters);

    Observable<TrackingAwbReturProduct> getTrackingAwbReturProduct(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> cancelResolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> askHelpResolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> finishReturSolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> acceptAdminSolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> acceptSolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> inputAddress(TKPDMapParam<String, Object> parameters);

    Observable<ActionDiscussionModel> replyConversationValidation(TKPDMapParam<String, Object> parameters);
}
