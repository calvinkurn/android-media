package com.tokopedia.inbox.rescenter.detailv2.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
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

public class ResCenterRepositoryImpl implements ResCenterRepository {

    private final ResCenterDataSourceFactory resCenterDataSourceFactory;
    private String resolutionID;

    public ResCenterRepositoryImpl(String resolutionID,
                                   ResCenterDataSourceFactory resCenterDataSourceFactory) {
        this.resolutionID = resolutionID;
        this.resCenterDataSourceFactory = resCenterDataSourceFactory;
    }

    @Override
    public Observable<DetailResCenter> getDetail(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getResCenterDetail(resolutionID, parameters);
    }

    @Override
    public Observable<HistoryAwbData> getHistoryAwb(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getHistoryAwb(resolutionID, parameters);
    }

    @Override
    public Observable<HistoryAddressData> getHistoryAddress(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getHistoryAddress(resolutionID, parameters);
    }

    @Override
    public Observable<HistoryActionData> getHistoryAction(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getHistoryAction(resolutionID, parameters);
    }

    @Override
    public Observable<ListProductDomainData> getListProduct(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getListProduct(resolutionID, parameters);
    }

    @Override
    public Observable<ProductDetailData> getDetailProduct(String troubleID, TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getProductDetail(resolutionID, troubleID, parameters);
    }

    @Override
    public Observable<DiscussionModel> getConversation(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getResCenterConversation(resolutionID, parameters);
    }

    @Override
    public Observable<LoadMoreModel> getConversationMore(TKPDMapParam<String, Object> parameters) {
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
    public Observable<ReplyDiscussionValidationModel> replyConversationValidation(TKPDMapParam<String, Object> parameters) {
        return resCenterDataSourceFactory.createCloudActionResCenterDataStore()
                .replyConversationValidation(parameters);
    }
}
