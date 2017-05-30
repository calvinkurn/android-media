package com.tokopedia.seller.topads.keyword.view.presenter;

import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.keyword.domain.interactor.TopAdsKeywordActionBulkUseCase;
import com.tokopedia.seller.topads.keyword.domain.interactor.TopAdsKeywordGetDetailUseCase;
import com.tokopedia.seller.topads.keyword.domain.model.KeywordDetailDomain;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordDetailViewListener;
import com.tokopedia.seller.topads.keyword.view.mapper.TopAdsKeywordDetailMapperView;

import java.util.Date;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailPresenterImpl extends TopadsKeywordDetailPresenter<TopAdsKeywordDetailViewListener> {

    private final TopAdsKeywordGetDetailUseCase topAdsKeywordGetDetailUseCase;
    private final TopAdsKeywordActionBulkUseCase topAdsKeywordActionBulkUseCase;

    public TopAdsKeywordDetailPresenterImpl(TopAdsKeywordGetDetailUseCase topAdsKeywordGetDetailUseCase,
                                            TopAdsKeywordActionBulkUseCase topAdsKeywordActionBulkUseCase) {
        this.topAdsKeywordGetDetailUseCase = topAdsKeywordGetDetailUseCase;
        this.topAdsKeywordActionBulkUseCase = topAdsKeywordActionBulkUseCase;
    }

    @Override
    public void refreshAd(Date startDate, Date endDate, String id, int isPositive, String shopId) {
        topAdsKeywordGetDetailUseCase.execute(TopAdsKeywordGetDetailUseCase.createRequestParams(startDate,
                endDate, id, isPositive, shopId), getSubscriberRefreshAd());
    }

    @Override
    public void deleteAd(String id, String groupId, String shopId) {
        topAdsKeywordActionBulkUseCase.execute(TopAdsKeywordActionBulkUseCase.createRequestParams(id, groupId,
                shopId, TopAdsNetworkConstant.ACTION_BULK_DELETE_AD),
                getSubscriberDeleteAd());
    }

    @Override
    public void turnOnAd(String id, String groupId, String shopID) {
        topAdsKeywordActionBulkUseCase.execute(TopAdsKeywordActionBulkUseCase.createRequestParams(id, groupId,
                shopID, TopAdsNetworkConstant.ACTION_BULK_ON_AD),
                getSubscriberTurnOnAd());
    }

    @Override
    public void turnOffAd(String id, String groupId, String shopID) {
        topAdsKeywordActionBulkUseCase.execute(TopAdsKeywordActionBulkUseCase.createRequestParams(id, groupId,
                shopID, TopAdsNetworkConstant.ACTION_BULK_OFF_AD),
                getSubscriberTurnOffAd());
    }

    private Subscriber<KeywordDetailDomain> getSubscriberRefreshAd() {
        return new Subscriber<KeywordDetailDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadAdError();
            }

            @Override
            public void onNext(KeywordDetailDomain keywordDetailDomain) {
                getView().onAdLoaded(TopAdsKeywordDetailMapperView.mapDomainToView(keywordDetailDomain));
            }
        };
    }

    public Subscriber<Boolean> getSubscriberDeleteAd() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onDeleteAdError();
            }

            @Override
            public void onNext(Boolean isSuccess) {
                if (isSuccess) {
                    getView().onDeleteAdSuccess();
                } else {
                    getView().onDeleteAdError();
                }
            }
        };
    }

    public Subscriber<Boolean> getSubscriberTurnOnAd() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onTurnOnAdError();
            }

            @Override
            public void onNext(Boolean isSuccess) {
                if(isSuccess){
                    getView().onTurnOnAdSuccess();
                }else {
                    getView().onTurnOnAdError();
                }
            }
        };
    }

    public Subscriber<Boolean> getSubscriberTurnOffAd() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onTurnOffAdError();
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().onTurnOffAdSuccess();
            }
        };
    }

    @Override
    public void unSubscribe() {
        topAdsKeywordActionBulkUseCase.unsubscribe();
        topAdsKeywordGetDetailUseCase.unsubscribe();
    }
}
