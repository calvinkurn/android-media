package com.tokopedia.discovery.intermediary.view;

import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.discovery.intermediary.domain.interactor.GetIntermediaryCategoryUseCase;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;

/**
 * Created by alifa on 3/24/17.
 */

public class IntermediaryPresenter extends BaseDaggerPresenter<IntermediaryContract.View>
        implements  IntermediaryContract.Presenter{

    private final GetIntermediaryCategoryUseCase getIntermediaryCategoryUseCase;

    public IntermediaryPresenter(GetIntermediaryCategoryUseCase getIntermediaryCategoryUseCase) {
        this.getIntermediaryCategoryUseCase = getIntermediaryCategoryUseCase;
    }


    @Override
    public void getIntermediaryCategory(String categoryId) {
        getIntermediaryCategoryUseCase.setCategoryId(categoryId);
        getView().showLoading();
        getIntermediaryCategoryUseCase.execute( RequestParams.EMPTY,
                new IntermediarySubscirber());
    }

    private class IntermediarySubscirber extends DefaultSubscriber<IntermediaryCategoryDomainModel> {
        @Override
        public void onCompleted() {
            getView().hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            getView().emptyState();
            e.printStackTrace();
        }

        @Override
        public void onNext(IntermediaryCategoryDomainModel domainModel) {
            if (isViewAttached()) {
                getView().renderHeader(domainModel.getHeaderModel());
            }
        }

    }
}
