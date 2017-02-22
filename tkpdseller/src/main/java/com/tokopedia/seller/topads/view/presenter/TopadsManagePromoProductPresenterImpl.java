package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.topads.domain.interactor.TopAdsCheckExistGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopadsSearchGroupAdsNameUseCase;
import com.tokopedia.seller.topads.domain.model.data.GroupAd;
import com.tokopedia.seller.topads.view.listener.TopAdsManagePromoProductView;

import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 2/16/17.
 */
public class TopAdsManagePromoProductPresenterImpl extends BaseDaggerPresenter<TopAdsManagePromoProductView> implements TopAdsManagePromoProductPresenter {

    private final TopadsSearchGroupAdsNameUseCase topadsSearchGroupAdsNameUseCase;
    private final TopAdsCheckExistGroupUseCase topAdsCheckExistGroupUseCase;

    public TopAdsManagePromoProductPresenterImpl(TopadsSearchGroupAdsNameUseCase topadsSearchGroupAdsNameUseCase,
                                                 TopAdsCheckExistGroupUseCase topAdsCheckExistGroupUseCase) {
        this.topadsSearchGroupAdsNameUseCase = topadsSearchGroupAdsNameUseCase;
        this.topAdsCheckExistGroupUseCase = topAdsCheckExistGroupUseCase;
    }


    @Override
    public void checkIsGroupExist(String keyword) {
        topAdsCheckExistGroupUseCase.execute(TopadsSearchGroupAdsNameUseCase.createRequestParams(keyword)
                , getSubscriberCheckGroupExist());
    }

    @Override
    public void searchGroupName(String keyword) {
        topadsSearchGroupAdsNameUseCase.execute(TopadsSearchGroupAdsNameUseCase.createRequestParams(keyword)
                , getSubscriberSearchGroupName());
    }

    private Subscriber<List<GroupAd>> getSubscriberSearchGroupName() {
        return new Subscriber<List<GroupAd>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<GroupAd> groupAds) {

            }
        };
    }

    private Subscriber<Boolean> getSubscriberCheckGroupExist() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onCheckGroupExistError(e.getMessage());
            }

            @Override
            public void onNext(Boolean isExist) {
                if(isExist) {
                    getView().onGroupExist();
                }else{
                    getView().onGroupNotExist();
                }
            }
        };
    }
}
