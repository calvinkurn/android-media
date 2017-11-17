package com.tokopedia.transaction.purchase.detail.di;

import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderActService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderService;
import com.tokopedia.transaction.purchase.detail.domain.OrderDetailRepository;
import com.tokopedia.transaction.purchase.detail.interactor.OrderDetailInteractorImpl;
import com.tokopedia.transaction.purchase.detail.presenter.OrderDetailPresenterImpl;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 11/14/17. Tokopedia
 */
@Module
public class OrderDetailModule {

    public OrderDetailModule() {
    }

    @Provides
    @OrderDetailScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @OrderDetailScope
    OrderDetailService provideOrderDetailService() {
        return new OrderDetailService();
    }

    @Provides
    @OrderDetailScope
    TXOrderService provideTransactionOrderService() {
        return new TXOrderService();
    }

    @Provides
    @OrderDetailScope
    TXOrderActService provideTransactionActionOrderService() {
        return new TXOrderActService();
    }

    @Provides
    @OrderDetailScope
    OrderDetailRepository provideRepository() {
        return new OrderDetailRepository(
                provideOrderDetailService(),
                provideTransactionOrderService(),
                provideTransactionActionOrderService());
    }

    @Provides
    @OrderDetailScope
    OrderDetailInteractorImpl provideInteractor() {
        return new OrderDetailInteractorImpl(provideCompositeSubscription(), provideRepository());
    }

    @Provides
    @OrderDetailScope
    OrderDetailPresenterImpl providePresenter() {
        return new OrderDetailPresenterImpl(provideInteractor());
    }

}
