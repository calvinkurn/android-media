package com.tokopedia.transaction.purchase.detail.di;

import com.tokopedia.core.network.apiservices.replacement.ReplacementActService;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderActService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderService;
import com.tokopedia.transaction.network.MyShopOrderActService;
import com.tokopedia.transaction.network.ProductChangeService;
import com.tokopedia.transaction.purchase.detail.domain.OrderDetailRepository;
import com.tokopedia.transaction.purchase.detail.domain.mapper.OrderDetailMapper;
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
    OrderDetailMapper provideOrderDetailMapper() {
        return new OrderDetailMapper();
    }

    @Provides
    @OrderDetailScope
    TXOrderService provideTransactionOrderService() {
        return new TXOrderService();
    }

    @Provides
    @OrderDetailScope
    ReplacementActService provideReplacementService() {
        return new ReplacementActService();
    }

    @Provides
    @OrderDetailScope
    TXOrderActService provideTransactionActionOrderService() {
        return new TXOrderActService();
    }

    @Provides
    @OrderDetailScope
    MyShopOrderActService provideShopActService() {
        return new MyShopOrderActService();
    }

    @Provides
    @OrderDetailScope
    ProductChangeService provideproductActService() {
        return new ProductChangeService();
    }

    @Provides
    @OrderDetailScope
    OrderDetailRepository provideRepository() {
        return new OrderDetailRepository(
                provideOrderDetailMapper(),
                provideOrderDetailService(),
                provideReplacementService(),
                provideTransactionActionOrderService(),
                provideShopActService(),
                provideproductActService());
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
