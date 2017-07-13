package com.tokopedia.seller.selling.appwidget.di;

import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.selling.appwidget.data.NewOrderApi;
import com.tokopedia.seller.selling.appwidget.data.repository.GetNewOrderRepositoryImpl;
import com.tokopedia.seller.selling.appwidget.data.source.GetNewOrderDataSource;
import com.tokopedia.seller.selling.appwidget.domain.GetNewOrderRepository;
import com.tokopedia.seller.selling.appwidget.domain.interactor.GetNewOrderUseCase;
import com.tokopedia.seller.selling.appwidget.presenter.GetNewOrderPresenter;
import com.tokopedia.seller.selling.appwidget.presenter.GetNewOrderPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

@NewOrderWidgetScope
@Module
public class NewOrderWidgetModule {

    @NewOrderWidgetScope
    @Provides
    GetNewOrderPresenter providePresenterNewOrder(GetNewOrderUseCase getNewOrderUseCase){
        return new GetNewOrderPresenterImpl(getNewOrderUseCase);
    }

    @NewOrderWidgetScope
    @Provides
    GetNewOrderRepository provideNewOrderRepository(GetNewOrderDataSource getNewOrderDataSource){
        return new GetNewOrderRepositoryImpl(getNewOrderDataSource);
    }

    @NewOrderWidgetScope
    @Provides
    NewOrderApi provideNewOrderApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(NewOrderApi.class);
    }
}
