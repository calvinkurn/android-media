package com.tokopedia.home.explore.view.presentation;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.home.explore.domain.GetExploreDataUseCase;
import com.tokopedia.home.explore.domain.GetExploreLocalDataUseCase;
import com.tokopedia.home.explore.domain.model.ExploreDataModel;
import com.tokopedia.usecase.RequestParams;

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

        dataUseCase.execute(RequestParams.EMPTY, getSubscriber());
    }

    @NonNull
    private Action1<ExploreDataModel> refreshAction() {
        return new Action1<ExploreDataModel>() {
            @Override
            public void call(ExploreDataModel dataModel) {
                compositeSubscription.add(getDataFromNetwork().subscribe(getSubscriber()));
            }
        };
    }

    @NonNull
    private Subscriber<ExploreDataModel> getSubscriber() {
        return new Subscriber<ExploreDataModel>() {
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
            public void onNext(ExploreDataModel exploreDataModel) {
                if(isViewAttached()){
                    getView().renderData(exploreDataModel);
                }
            }
        };
    }

    public Observable<ExploreDataModel> getDataFromNetwork() {
        return dataUseCase.getExecuteObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
