package com.tokopedia.home.explore.view.presentation;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.beranda.domain.interactor.GetHomeDataUseCase;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.explore.domain.GetExploreDataUseCase;
import com.tokopedia.home.explore.domain.model.ExploreDataModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public class ExplorePresenter extends BaseDaggerPresenter<ExploreContract.View> implements ExploreContract.Presenter {

    @Inject
    GetExploreDataUseCase dataUseCase;


    @Override
    public void getData() {
        dataUseCase.execute(RequestParams.EMPTY, new Subscriber<ExploreDataModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ExploreDataModel exploreDataModel) {
                if(isViewAttached()){
                    getView().renderData(exploreDataModel);
                }
            }
        });
    }
}
