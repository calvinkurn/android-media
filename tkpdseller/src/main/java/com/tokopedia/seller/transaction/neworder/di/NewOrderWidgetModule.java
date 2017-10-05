package com.tokopedia.seller.transaction.neworder.di;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.repository.NotificationRepositoryImpl;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.transaction.neworder.data.NewOrderApi;
import com.tokopedia.seller.transaction.neworder.data.repository.GetNewOrderRepositoryImpl;
import com.tokopedia.seller.transaction.neworder.data.source.GetNewOrderDataSource;
import com.tokopedia.seller.transaction.neworder.domain.GetNewOrderRepository;
import com.tokopedia.seller.transaction.neworder.domain.interactor.GetNewOrderWidgetUseCase;
import com.tokopedia.seller.transaction.neworder.view.presenter.GetNewOrderPresenter;
import com.tokopedia.seller.transaction.neworder.view.presenter.GetNewOrderPresenterImpl;

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
    GetNewOrderPresenter providePresenterNewOrder(GetNewOrderWidgetUseCase getNewOrderUseCase){
        return new GetNewOrderPresenterImpl(getNewOrderUseCase);
    }

    @NewOrderWidgetScope
    @Provides
    GetNewOrderRepository provideNewOrderRepository(GetNewOrderDataSource getNewOrderDataSource){
        return new GetNewOrderRepositoryImpl(getNewOrderDataSource);
    }

    @NewOrderWidgetScope
    @Provides
    NotificationRepository provideNotificationRepository(NotificationSourceFactory notificationSourceFactory){
        return new NotificationRepositoryImpl(notificationSourceFactory);
    }

    @NewOrderWidgetScope
    @Provides
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, DrawerHelper.DRAWER_CACHE);
    }

    @NewOrderWidgetScope
    @Provides
    NewOrderApi provideNewOrderApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(NewOrderApi.class);
    }
}
