package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.topads.utils.ViewUtils;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailGroupMapper;
import com.tokopedia.seller.topads.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailEditGroupPresenterImpl<T extends TopAdsDetailEditView> extends BaseDaggerPresenter<T> implements TopAdsDetailEditGroupPresenter<T> {

    protected TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase;
    protected TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase;

    public TopAdsDetailEditGroupPresenterImpl(TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                              TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase) {
        this.topAdsGetDetailGroupUseCase = topAdsGetDetailGroupUseCase;
        this.topAdsSaveDetailGroupUseCase = topAdsSaveDetailGroupUseCase;
    }

    @Override
    public void saveAd(TopAdsDetailGroupViewModel topAdsDetailGroupViewModel, List<TopAdsProductViewModel> topAdsProductViewModelList) {
        topAdsSaveDetailGroupUseCase.execute(TopAdsSaveDetailGroupUseCase.createRequestParams(
                TopAdDetailGroupMapper.convertViewToDomain(topAdsDetailGroupViewModel)),
                getSaveGroupSubscriber());
    }

    /**
     * retrieve to populate the fields of groups ad config to the view
     *
     * @param adId adId here is group ID
     */
    @Override
    public void getDetailAd(String adId) {
        topAdsGetDetailGroupUseCase.execute(
                TopAdsGetDetailGroupUseCase.createRequestParams(adId),
                getDetailAdSubscriber());
    }

    public void getDetailAd(String adId, Subscriber<TopAdsDetailGroupDomainModel> subscriber) {
        topAdsGetDetailGroupUseCase.execute(
                TopAdsGetDetailGroupUseCase.createRequestParams(adId),
                subscriber);
    }

    private Subscriber<TopAdsDetailGroupDomainModel> getDetailAdSubscriber(){
        return new Subscriber<TopAdsDetailGroupDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailGroupDomainModel domainModel) {
                getView().onDetailAdLoaded(TopAdDetailGroupMapper.convertDomainToView(domainModel));
            }
        };
    }

    private Subscriber<TopAdsDetailGroupDomainModel> getSaveGroupSubscriber(){
        return new Subscriber<TopAdsDetailGroupDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailGroupDomainModel domainModel) {
                getView().onSaveAdSuccess(TopAdDetailGroupMapper.convertDomainToView(domainModel));
            }
        };
    }


    @Override
    public void detachView() {
        super.detachView();
        topAdsSaveDetailGroupUseCase.unsubscribe();
        topAdsGetDetailGroupUseCase.unsubscribe();
    }


}