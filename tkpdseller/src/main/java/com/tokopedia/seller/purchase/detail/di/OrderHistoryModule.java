package com.tokopedia.seller.purchase.detail.di;

import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.seller.purchase.detail.domain.OrderHistoryRepository;
import com.tokopedia.seller.purchase.detail.domain.mapper.OrderDetailMapper;
import com.tokopedia.seller.purchase.detail.interactor.OrderHistoryInteractorImpl;
import com.tokopedia.seller.purchase.detail.presenter.OrderHistoryPresenterImpl;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 11/17/17. Tokopedia
 */
@Module
public class OrderHistoryModule {

    public OrderHistoryModule() {
    }

    @Provides
    @OrderHistoryScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @OrderHistoryScope
    OrderDetailService provideOrderDetailService() {
        return new OrderDetailService();
    }

    @Provides
    @OrderHistoryScope
    OrderDetailMapper provideOrderDetailMapper() {
        return new OrderDetailMapper();
    }

    @Provides
    @OrderHistoryScope
    OrderHistoryRepository provideOrderHistoryRepository() {
        return new OrderHistoryRepository(
                provideOrderDetailService(),
                provideOrderDetailMapper());
    }

    @Provides
    @OrderHistoryScope
    OrderHistoryInteractorImpl provideOrderHistoryInteractor() {
        return new OrderHistoryInteractorImpl(provideOrderHistoryRepository(),
                provideCompositeSubscription());
    }

    @Provides
    @OrderHistoryScope
    OrderHistoryPresenterImpl provideOrderHistoryPresenter() {
        return new OrderHistoryPresenterImpl(provideOrderHistoryInteractor());
    }
}
