package com.tokopedia.transaction.orders.orderlist.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.transaction.orders.common.OrderListAuthInterceptor;
import com.tokopedia.transaction.orders.common.url.OrderURL;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListRepository;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListUseCase;
import com.tokopedia.transaction.orders.orderlist.source.OrderListFactory;
import com.tokopedia.transaction.orders.orderlist.source.OrderListRepositoryImpl;
import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by baghira on 07/05/18.
 */

@Module
public class OrderListModule {
    @Provides
    OrderListRepository provideOrderListRepository(OrderListFactory orderListFactory){
        return new OrderListRepositoryImpl(orderListFactory);
    }

    @Provides
    OrderListUseCase provideOrderListUseCase(OrderListRepository orderListRepository){
        return new OrderListUseCase(orderListRepository);
    }

    @Provides
    Retrofit provideRetrofit(Retrofit.Builder builder, OkHttpClient okHttpClient){
            return builder.baseUrl(OrderURL.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    OkHttpClient provideOkHttpClient(OrderListAuthInterceptor authInterceptor, HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(authInterceptor)
                .build();
    }

    @Provides
    OrderListDataApi provide(Retrofit retrofit){
        return retrofit.create(OrderListDataApi.class);
    }

    @Provides
    OrderListFactory provideOrderListFactory(OrderListDataApi orderListDataApi,@ApplicationContext Context context){
        return new OrderListFactory(orderListDataApi,context);
    }

    @Provides
    OrderListAuthInterceptor provideContactUsAuthInterceptor(@ApplicationContext Context context,
                                                             AbstractionRouter abstractionRouter,
                                                             UserSession userSession) {
        return new OrderListAuthInterceptor(context,abstractionRouter,userSession);

    }
}
