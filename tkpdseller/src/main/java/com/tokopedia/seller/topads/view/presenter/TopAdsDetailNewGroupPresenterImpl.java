package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.domain.interactor.TopAdsCreateExistingGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsCreateNewGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.seller.topads.utils.ViewUtils;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.view.models.TopAdsProductViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewGroupPresenterImpl <T extends TopAdsDetailNewGroupView>
        extends TopAdsDetailEditGroupPresenterImpl<T>
        implements TopAdsDetailNewGroupPresenter<T> {

    TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase;
    TopAdsCreateExistingGroupUseCase topAdsCreateExistingGroupUseCase;

    public TopAdsDetailNewGroupPresenterImpl(TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase,
                                             TopAdsCreateExistingGroupUseCase topAdsCreateExistingGroupUseCase,
                                             TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                             TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase) {
        super(topAdsGetDetailGroupUseCase, topAdsSaveDetailGroupUseCase);
        this.topAdsCreateNewGroupUseCase = topAdsCreateNewGroupUseCase;
        this.topAdsCreateExistingGroupUseCase = topAdsCreateExistingGroupUseCase;
    }

    @Override
    public void saveAdExisting(int groupId,
                               List<TopAdsProductViewModel> topAdsProductViewModelList) {
        if (topAdsProductViewModelList == null || topAdsProductViewModelList.size() == 0) {
            getView().showErrorGroupEmpty();
        }
        else {
            getView().showLoading(true);
            // TODO usecase
        }
    }

    @Override
    public void saveAdNew(String groupName,
                          TopAdsDetailGroupViewModel topAdsDetailProductViewModel,
                          List<TopAdsProductViewModel> topAdsProductViewModelList) {
        getView().showLoading(true);
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
                    }
                });
    }

    @Override
    public void getDetailAd(String groupId) {
        // no op since, it is either create or edit, the detail is not shown, so no need to retrieve
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsCreateExistingGroupUseCase.unsubscribe();
        topAdsCreateNewGroupUseCase.unsubscribe();
    }

}