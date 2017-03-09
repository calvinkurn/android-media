package com.tokopedia.inbox.rescenter.detailv2.data;

import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.domain.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;

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
    public Observable<DetailResCenter> getDetail() {
        return resCenterDataSourceFactory
                .createCloudResCenterDataSource()
                .getResCenterDetail(resolutionID);
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
}
