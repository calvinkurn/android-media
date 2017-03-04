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
import com.tokopedia.seller.topads.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.seller.topads.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailEditGroupPresenterImpl<T extends TopAdsDetailEditView> extends BaseDaggerPresenter<T> implements TopAdsDetailEditGroupPresenter<T> {

    // TODO change use case for edit
    private TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase;
    private TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase;

    public TopAdsDetailEditGroupPresenterImpl(TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                              TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase) {
        this.topAdsGetDetailGroupUseCase = topAdsGetDetailGroupUseCase;
        this.topAdsSaveDetailGroupUseCase = topAdsSaveDetailGroupUseCase;
    }

    @Override
    public void saveAd(TopAdsDetailGroupViewModel topAdsDetailGroupViewModel, List<TopAdsProductViewModel> topAdsProductViewModelList) {
        topAdsSaveDetailGroupUseCase.execute(TopAdsSaveDetailProductUseCase.createRequestParams(TopAdDetailProductMapper.convertViewToDomain(topAdsDetailGroupViewModel)),
                new Subscriber<TopAdsDetailGroupDomainModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onSaveAdError(ViewUtils.getErrorMessage(e));
                    }

                    @Override
                    public void onNext(TopAdsDetailGroupDomainModel domainModel) {
                        getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(domainModel));
                    }
                });
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
                new Subscriber<TopAdsDetailGroupDomainModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
                    }

                    @Override
                    public void onNext(TopAdsDetailGroupDomainModel domainModel) {
                        getView().onDetailAdLoaded(TopAdDetailProductMapper.convertDomainToView(domainModel));
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsSaveDetailGroupUseCase.unsubscribe();
        topAdsGetDetailGroupUseCase.unsubscribe();
    }


}