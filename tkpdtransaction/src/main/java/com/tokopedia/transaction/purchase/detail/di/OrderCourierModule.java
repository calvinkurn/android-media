package com.tokopedia.transaction.purchase.detail.di;

import com.tokopedia.core.network.apiservices.shop.MyShopOrderService;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.transaction.network.MyShopOrderActService;
import com.tokopedia.transaction.purchase.detail.domain.OrderCourierRepository;
import com.tokopedia.transaction.purchase.detail.domain.mapper.OrderDetailMapper;
import com.tokopedia.transaction.purchase.detail.interactor.OrderCourierInteractorImpl;
import com.tokopedia.transaction.purchase.detail.presenter.OrderCourierPresenterImpl;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 1/3/18. Tokopedia
 */
@Module
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
    OrderDetailMapper provideOrderDetailMapper() {
        return new OrderDetailMapper();
    }

    @Provides
    @OrderCourierScope
    MyShopOrderActService provideOrderActService() {
        return new MyShopOrderActService();
    }

    @Provides
    @OrderCourierScope
    OrderDetailService provideOrderDetailService() {
        return new OrderDetailService();
    }

    @Provides
    @OrderCourierScope
    MyShopOrderService provideOrderService() {
        return new MyShopOrderService();
    }

    @Provides
    @OrderCourierScope
    OrderCourierRepository provideOrderCourierRepository() {
        return new OrderCourierRepository(
                provideOrderDetailMapper(),
                provideOrderService(),
                provideOrderActService(),
                provideOrderDetailService());
    }

    @Provides
    @OrderCourierScope
    OrderCourierInteractorImpl provideOrderCourierInteractor() {
        return new OrderCourierInteractorImpl(provideCompositeSubsrciption(),
                provideOrderCourierRepository());
    }

    @Provides
    @OrderCourierScope
    OrderCourierPresenterImpl provideOrderCourierPresenter() {
        return new OrderCourierPresenterImpl(provideOrderCourierInteractor());
    }

}
