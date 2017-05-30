package com.tokopedia.seller.topads.keyword.view.presenter;

import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.data.mapper.TopAdsKeywordDetailMapperToDomain;
import com.tokopedia.seller.topads.keyword.domain.interactor.TopAdsKeywordDeleteUseCase;
import com.tokopedia.seller.topads.keyword.domain.interactor.TopAdsKeywordGetDetailUseCase;
import com.tokopedia.seller.topads.keyword.domain.model.KeywordDetailDomain;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordDetailViewListener;
import com.tokopedia.seller.topads.keyword.view.mapper.TopAdsKeywordDetailMapperView;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailPresenterImpl extends TopadsKeywordDetailPresenter<TopAdsKeywordDetailViewListener>{

    private final TopAdsKeywordGetDetailUseCase topAdsKeywordGetDetailUseCase;
    private final TopAdsKeywordDeleteUseCase topAdsKeywordDeleteUseCase;

    public TopAdsKeywordDetailPresenterImpl(TopAdsKeywordGetDetailUseCase topAdsKeywordGetDetailUseCase,
                                            TopAdsKeywordDeleteUseCase topAdsKeywordDeleteUseCase) {
        this.topAdsKeywordGetDetailUseCase = topAdsKeywordGetDetailUseCase;
        this.topAdsKeywordDeleteUseCase = topAdsKeywordDeleteUseCase;
    }

    @Override
    public void refreshAd(Date startDate, Date endDate, String id, int isPositive) {
        topAdsKeywordGetDetailUseCase.execute(TopAdsKeywordGetDetailUseCase.createRequestParams(startDate,
                endDate, id, isPositive), getSubscriberRefreshAd());
    }

    @Override
    public void deleteAd(String id) {
        topAdsKeywordDeleteUseCase.execute(TopAdsKeywordDeleteUseCase.createRequestParams(id), getSubscriberDeleteAd());
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
                if(isSuccess){
                    getView().onDeleteAdSuccess();
                }else{
                    getView().onDeleteAdError();
                }
            }
        };
    }

    @Override
    public void unSubscribe() {
        topAdsKeywordDeleteUseCase.unsubscribe();
        topAdsKeywordGetDetailUseCase.unsubscribe();
    }
}
