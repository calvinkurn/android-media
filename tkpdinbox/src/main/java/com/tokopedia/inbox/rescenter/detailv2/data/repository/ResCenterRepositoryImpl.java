package com.tokopedia.inbox.rescenter.detailv2.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;

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
    public Observable<DetailResCenter> getConversation() {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getResCenterConversation(resolutionID);
    }

    @Override
    public Observable<Object> getConversationMore() {
        return null;
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
}
