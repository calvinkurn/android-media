package com.tokopedia.inbox.rescenter.detailv2.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.DetailResponseData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationListDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;
import com.tokopedia.inbox.rescenter.discussion.domain.model.NewReplyDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.getdiscussion.DiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore.LoadMoreModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionValidationModel;
import com.tokopedia.inbox.rescenter.historyaction.domain.model.HistoryActionData;
import com.tokopedia.inbox.rescenter.historyaddress.domain.model.HistoryAddressData;
import com.tokopedia.inbox.rescenter.historyawb.domain.model.HistoryAwbData;
import com.tokopedia.inbox.rescenter.product.domain.model.ListProductDomainData;
import com.tokopedia.inbox.rescenter.product.domain.model.ProductDetailData;

import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public class ResCenterRepositoryImpl implements ResCenterRepository {

    private final ResCenterDataSourceFactory resCenterDataSourceFactory;

    public ResCenterRepositoryImpl(ResCenterDataSourceFactory resCenterDataSourceFactory) {
        this.resCenterDataSourceFactory = resCenterDataSourceFactory;
    }

    @Override
    public Observable<DetailResCenter> getDetail(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getResCenterDetail(resolutionID, parameters);
    }

    @Override
    public Observable<HistoryAwbData> getHistoryAwb(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getHistoryAwb(resolutionID, parameters);
    }

    @Override
    public Observable<HistoryAddressData> getHistoryAddress(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getHistoryAddress(resolutionID, parameters);
    }

    @Override
    public Observable<HistoryActionData> getHistoryAction(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getHistoryAction(resolutionID, parameters);
    }

    @Override
    public Observable<HistoryActionData> getHistoryActionV2(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getHistoryActionV2(resolutionID, parameters);
    }

    @Override
    public Observable<ListProductDomainData> getListProduct(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getListProduct(resolutionID, parameters);
    }

    @Override
    public Observable<ProductDetailData> getDetailProduct(String resolutionID, String troubleID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getProductDetail(resolutionID, troubleID, parameters);
    }

    @Override
    public Observable<DiscussionModel> getConversation(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getResCenterConversation(resolutionID, parameters);
    }

    @Override
    public Observable<LoadMoreModel> getConversationMore(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getResCenterConversationMore(resolutionID, parameters);
    }

    @Override
    public Observable<TrackingAwbReturProduct> getTrackingAwbReturProduct(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory.createCloudInboxResCenterDataSource()
                .trackAwbReturProduct(parameters);
    }

    @Override
    public Observable<ResolutionActionDomainData> cancelResolution(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory.createCloudActionResCenterDataStore()
                .cancelResolution(parameters);
    }

    @Override
    public Observable<ResolutionActionDomainData> askHelpResolution(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory.createCloudActionResCenterDataStore()
                .reportResolution(parameters);
    }

    @Override
    public Observable<ResolutionActionDomainData> finishReturSolution(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory.createCloudActionResCenterDataStore()
                .finishReturSolution(parameters);
    }

    @Override
    public Observable<ResolutionActionDomainData> acceptAdminSolution(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory.createCloudActionResCenterDataStore()
                .acceptAdminSolution(parameters);
    }

    @Override
    public Observable<ResolutionActionDomainData> acceptSolution(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory.createCloudActionResCenterDataStore()
                .acceptSolution(parameters);
    }

    @Override
    public Observable<ResolutionActionDomainData> inputAddress(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory.createCloudActionResCenterDataStore()
                .inputAddress(parameters);
    }

    @Override
    public Observable<ResolutionActionDomainData> editAddress(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory.createCloudActionResCenterDataStore()
                .editAddress(parameters);
    }

    @Override
    public Observable<ReplyDiscussionValidationModel> replyConversationValidation(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory.createCloudActionResCenterDataStore()
                .replyConversationValidation(parameters);
    }

    @Override
    public Observable<NewReplyDiscussionModel> replyResolution(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .replyResolution(resolutionID, parameters);
    }

    @Override
    public Observable<NewReplyDiscussionModel> replyResolutionSubmit(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .replyResolutionSubmit(resolutionID, parameters);
    }

    @Override
    public Observable<GenerateHostModel> generateToken(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudActionResCenterDataStore()
                .generateTokenHost(parameters);
    }

    //chat section


    @Override
    public Observable<DetailResponseData> getDetailV2(RequestParams requestParams) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getResCenterDetailV2(requestParams);
    }

    @Override
    public Observable<DetailResChatDomain> getConversation(RequestParams requestParams) {
        return resCenterDataSourceFactory
                .createResChatCloudSource()
                .getDetailResChatCloud(requestParams);
    }

    @Override
    public Observable<ConversationListDomain> getConversationMore(RequestParams requestParams) {
        return resCenterDataSourceFactory
                .createResChatMoreCloudSource()
                .getDetailResChatMoreCloud(requestParams);
    }

    @Override
    public Observable<ResolutionActionDomainData> finishResolution(RequestParams params) {
        return resCenterDataSourceFactory
                .createCloudActionResCenterDataStore()
                .finishResolution(params);
    }

    @Override
    public Observable<ResolutionActionDomainData> cancelResolutionV2(RequestParams params) {
        return resCenterDataSourceFactory
                .createCloudActionResCenterDataStore()
                .cancelResolution(params);
    }

    @Override
    public Observable<ResolutionActionDomainData> askHelpResolutionV2(RequestParams requestParams) {
        return resCenterDataSourceFactory
                .createCloudActionResCenterDataStore()
                .askHelpResolutionV2(requestParams);
    }

    @Override
    public Observable<NextActionDomain> getNextAction(RequestParams requestParams) {
        return resCenterDataSourceFactory
                .createNextActionCloudSource()
                .getNextAction(requestParams);
    }

    @Override
    public Observable<ResolutionActionDomainData> inputAddressV2(RequestParams requestParams) {
        return resCenterDataSourceFactory.createCloudActionResCenterDataStore()
                .inputAddressV2(requestParams);
    }

    @Override
    public Observable<ResolutionActionDomainData> editAddressV2(RequestParams requestParams) {
        return resCenterDataSourceFactory.createCloudActionResCenterDataStore()
                .editAddressV2(requestParams);
    }
}
