package com.tokopedia.transaction.purchase.detail.di;

import com.tokopedia.transaction.purchase.detail.interactor.OrderCourierInteractorImpl;
import com.tokopedia.transaction.purchase.detail.presenter.OrderCourierPresenterImpl;

import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public class OrderCourierModule {

    public OrderCourierModule() {
    }

    @Provides
    @OrderCourierScope
    CompositeSubscription provideCompositeSubsrciption() {
        return new CompositeSubscription();
    }

    @Provides
    @OrderCourierScope
    OrderCourierInteractorImpl provideOrderCourierInteractor() {
        return new OrderCourierInteractorImpl();
    }

    @Provides
    @OrderCourierScope
    OrderCourierPresenterImpl provideOrderCourierPresenter() {
        return new OrderCourierPresenterImpl();
    }

}
