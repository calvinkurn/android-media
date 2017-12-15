package com.tokopedia.inbox.rescenter.detailv2.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.DetailResponseData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationListDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;
import com.tokopedia.inbox.rescenter.discussion.domain.model.NewReplyDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionValidationModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;
import com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore.LoadMoreModel;
import com.tokopedia.inbox.rescenter.historyaction.domain.model.HistoryActionData;
import com.tokopedia.inbox.rescenter.historyaddress.domain.model.HistoryAddressData;
import com.tokopedia.inbox.rescenter.historyawb.domain.model.HistoryAwbData;
import com.tokopedia.inbox.rescenter.product.domain.model.ListProductDomainData;
import com.tokopedia.inbox.rescenter.product.domain.model.ProductDetailData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.getdiscussion.DiscussionModel;

import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public interface ResCenterRepository {

    Observable<DetailResCenter> getDetail(String resolutionID, TKPDMapParam<String, Object> parameters);

    Observable<DiscussionModel> getConversation(String resolutionID, TKPDMapParam<String, Object> parameters);

    Observable<LoadMoreModel> getConversationMore(String resolutionID, TKPDMapParam<String, Object> parameters);

    Observable<HistoryAwbData> getHistoryAwb(String resolutionID, TKPDMapParam<String, Object> parameters);

    Observable<HistoryAddressData> getHistoryAddress(String resolutionID, TKPDMapParam<String, Object> parameters);

    Observable<HistoryActionData> getHistoryAction(String resolutionID, TKPDMapParam<String, Object> parameters);

    Observable<HistoryActionData> getHistoryActionV2(String resolutionID, TKPDMapParam<String, Object> parameters);

    Observable<ListProductDomainData> getListProduct(String resolutionID, TKPDMapParam<String, Object> parameters);

    Observable<ProductDetailData> getDetailProduct(String resolutionID, String troubleID, TKPDMapParam<String, Object> parameters);

    Observable<TrackingAwbReturProduct> getTrackingAwbReturProduct(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> cancelResolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> askHelpResolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> finishReturSolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> acceptAdminSolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> acceptSolution(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> inputAddress(TKPDMapParam<String, Object> parameters);

    Observable<ResolutionActionDomainData> editAddress(TKPDMapParam<String, Object> parameters);

    Observable<ReplyDiscussionValidationModel> replyConversationValidation(TKPDMapParam<String, Object> parameters);

    Observable<NewReplyDiscussionModel> replyResolution(String resolutionID, TKPDMapParam<String, Object> parameters);

    Observable<NewReplyDiscussionModel> replyResolutionSubmit(String resolutionID, TKPDMapParam<String, Object> parameters);

    Observable<GenerateHostModel> generateToken(TKPDMapParam<String, Object> parameters);

    Observable<DetailResponseData> getDetailV2(RequestParams requestParams);

    Observable<DetailResChatDomain> getConversation(RequestParams requestParams);

    Observable<ConversationListDomain> getConversationMore(RequestParams requestParams);

    Observable<ResolutionActionDomainData> finishResolution(RequestParams requestParams);

    Observable<ResolutionActionDomainData> cancelResolutionV2(RequestParams requestParams);

    Observable<ResolutionActionDomainData> askHelpResolutionV2(RequestParams requestParams);

    Observable<NextActionDomain> getNextAction(RequestParams requestParams);

    Observable<ResolutionActionDomainData> inputAddressV2(RequestParams requestParams);

    Observable<ResolutionActionDomainData> editAddressV2(RequestParams requestParams);
}
