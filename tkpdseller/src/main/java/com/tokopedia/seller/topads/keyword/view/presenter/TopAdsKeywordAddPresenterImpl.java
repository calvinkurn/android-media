package com.tokopedia.seller.topads.keyword.view.presenter;

import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.seller.topads.keyword.domain.interactor.KeywordAddUseCase;
import com.tokopedia.seller.topads.keyword.domain.interactor.KeywordDashboardUseCase;
import com.tokopedia.seller.topads.keyword.domain.model.KeywordDashboardDomain;
import com.tokopedia.seller.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordAddView;
import com.tokopedia.seller.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.seller.topads.keyword.view.model.KeywordNegativeParam;
import com.tokopedia.seller.topads.keyword.view.model.KeywordPositiveParam;
import com.tokopedia.seller.topads.view.presenter.TopAdsAdListPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author normansyahputa on 5/17/17.
 */

public class TopAdsKeywordAddPresenterImpl extends TopAdsKeywordAddPresenter {

    private KeywordAddUseCase keywordAddUseCase;

    @Inject
    public TopAdsKeywordAddPresenterImpl(KeywordAddUseCase keywordAddUseCase) {
        this.keywordAddUseCase = keywordAddUseCase;
    }

    public void addKeyword (String groupId,
                            int keywordType,
                            ArrayList<String> keywordList){
        keywordAddUseCase.execute(
                KeywordAddUseCase.createRequestParam(groupId, keywordType, keywordList),
                getAddKeywordSubscriber());
    }

    private Subscriber<AddKeywordDomainModel> getAddKeywordSubscriber(){
        return new Subscriber<AddKeywordDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onFailedSaveKeyword(e);
                }
            }

            @Override
            public void onNext(AddKeywordDomainModel addKeywordDomainModel) {
                getView().onSuccessSaveKeyword();
            }
        };
    }

    public void unsubscribe(){
        keywordAddUseCase.unsubscribe();
    }
}
