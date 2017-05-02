package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsCreateNewGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.topads.utils.ViewUtils;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailGroupMapper;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.seller.topads.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewGroupPresenterImpl<T extends TopAdsDetailNewGroupView>
        extends TopAdsDetailEditGroupPresenterImpl<T>
        implements TopAdsDetailNewGroupPresenter<T> {

    private TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase;
    private TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase;

    public TopAdsDetailNewGroupPresenterImpl(TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase,
                                             TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                             TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase,
                                             TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase) {
        super(topAdsGetDetailGroupUseCase, topAdsSaveDetailGroupUseCase);
        this.topAdsCreateNewGroupUseCase = topAdsCreateNewGroupUseCase;
        this.topAdsCreateDetailProductListUseCase = topAdsCreateDetailProductListUseCase;
    }

    @Override
    public void saveAdExisting(String groupId, final List<TopAdsProductViewModel> topAdsProductViewModelList) {
        super.getDetailAd(groupId, new Subscriber<TopAdsDetailGroupDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailGroupDomainModel topAdsDetailGroupDomainModel) {
                // get the latest domain from API, then pass it to re-save it
                topAdsCreateDetailProductListUseCase.execute(
                        TopAdsCreateDetailProductListUseCase.createRequestParams(
                                topAdsDetailGroupDomainModel,
                                topAdsProductViewModelList
                        ),
                        getSaveProductSubscriber()
                );
            }
        });
    }


    protected Subscriber<TopAdsDetailProductDomainModel> getSaveProductSubscriber(){
        return new Subscriber<TopAdsDetailProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailProductDomainModel domainModel) {
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(domainModel));
                getView().goToGroupDetail(domainModel.getGroupId());
            }
        };
    }


    @Override
    public void saveAdNew(String groupName,
                          TopAdsDetailGroupViewModel topAdsDetailProductViewModel,
                          List<TopAdsProductViewModel> topAdsProductViewModelList) {
        topAdsCreateNewGroupUseCase.execute(
                TopAdsCreateNewGroupUseCase.createRequestParams(groupName, topAdsDetailProductViewModel, topAdsProductViewModelList),
                new Subscriber<TopAdsDetailGroupViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onSaveAdError(ViewUtils.getErrorMessage(e));
                    }

                    @Override
                    public void onNext(TopAdsDetailGroupViewModel topAdsDetailGroupViewModel) {
                        getView().onSaveAdSuccess(topAdsDetailGroupViewModel);
                        getView().goToGroupDetail(String.valueOf(topAdsDetailGroupViewModel.getGroupId()));
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsCreateNewGroupUseCase.unsubscribe();
        topAdsCreateDetailProductListUseCase.unsubscribe();
    }

}