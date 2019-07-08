package com.tokopedia.seller.purchase.detail.di;

import com.tokopedia.core.network.apiservices.replacement.ReplacementActService;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderActService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderService;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.seller.purchase.network.MyShopOrderActService;
import com.tokopedia.seller.purchase.network.ProductChangeService;
import com.tokopedia.seller.purchase.detail.domain.OrderDetailRepository;
import com.tokopedia.seller.purchase.detail.domain.mapper.OrderDetailMapper;
import com.tokopedia.seller.purchase.detail.interactor.OrderDetailInteractorImpl;
import com.tokopedia.seller.purchase.detail.presenter.OrderDetailPresenterImpl;

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
        return new OrderDetailPresenterImpl(provideInteractor(), new GraphqlUseCase());
    }

}
