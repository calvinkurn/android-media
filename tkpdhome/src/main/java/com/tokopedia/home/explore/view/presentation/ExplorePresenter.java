package com.tokopedia.home.explore.view.presentation;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.home.explore.domain.GetExploreDataUseCase;
import com.tokopedia.home.explore.domain.GetExploreLocalDataUseCase;
import com.tokopedia.home.explore.domain.model.DataResponseModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.ExploreSectionViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public class ExplorePresenter extends BaseDaggerPresenter<ExploreContract.View> implements ExploreContract.Presenter {

    @Inject
    GetExploreDataUseCase dataUseCase;
    @Inject
    GetExploreLocalDataUseCase localDataUseCase;

    protected CompositeSubscription compositeSubscription;
    protected Subscription subscription;

    public ExplorePresenter() {
        compositeSubscription = new CompositeSubscription();
        subscription = Subscriptions.empty();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getData() {
        subscription = localDataUseCase.getExecuteObservable(RequestParams.EMPTY)
                .doOnNext(refreshAction())
                .onErrorResumeNext(getDataFromNetwork())
                .subscribe(getSubscriber());
        compositeSubscription.add(subscription);
    }

    @NonNull
    private Action1<List<ExploreSectionViewModel>> refreshAction() {
        return new Action1<List<ExploreSectionViewModel>>() {
            @Override
            public void call(List<ExploreSectionViewModel> list) {
                compositeSubscription.add(getDataFromNetwork().subscribe(getSubscriber()));
            }
        };
    }

    @NonNull
    private Subscriber<List<ExploreSectionViewModel>> getSubscriber() {
        return new Subscriber<List<ExploreSectionViewModel>>() {
            @Override
            public void onStart() {
                if (isViewAttached()) {
                    getView().showLoading();
                }
            }

            @Override
            public void onCompleted() {
                if (isViewAttached()) {
                    getView().hideLoading();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showNetworkError(ErrorHandler.getErrorMessage(e));
                    onCompleted();
                }
            }

            @Override
            public void onNext(List<ExploreSectionViewModel> list) {
                if(isViewAttached()){
                    getView().renderData(list);
                }
            }
        };
    }

    public Observable<List<ExploreSectionViewModel>> getDataFromNetwork() {
        return dataUseCase.getExecuteObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
