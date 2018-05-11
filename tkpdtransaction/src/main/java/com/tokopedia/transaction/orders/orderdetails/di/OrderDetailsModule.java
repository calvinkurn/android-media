package com.tokopedia.transaction.orders.orderdetails.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.transaction.orders.common.OrderListAuthInterceptor;
import com.tokopedia.transaction.orders.common.url.OrderURL;
import com.tokopedia.transaction.orders.orderdetails.domain.OrderDetailsRepository;
import com.tokopedia.transaction.orders.orderdetails.domain.OrderDetailsUseCase;
import com.tokopedia.transaction.orders.orderdetails.source.OrderDetailsDataApi;
import com.tokopedia.transaction.orders.orderdetails.source.OrderDetailsFactory;
import com.tokopedia.transaction.orders.orderdetails.source.OrderDetailsRepositoryImpl;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListRepository;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListUseCase;
import com.tokopedia.transaction.orders.orderlist.source.OrderListFactory;
import com.tokopedia.transaction.orders.orderlist.source.OrderListRepositoryImpl;
import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by baghira on 07/05/18.
 */

@Module
public class OrderDetailsModule {
    @Provides
    OrderDetailsRepository provideOrderDetailsRepository(OrderDetailsFactory orderDetailsFactory){
        return new OrderDetailsRepositoryImpl(orderDetailsFactory);
    }

    @Provides
    OrderDetailsUseCase provideOrderDetailsUseCase(OrderDetailsRepository orderDetailsRepository){
        return new OrderDetailsUseCase(orderDetailsRepository);
    }

    @Provides
    OrderDetailsFactory provideOrderDetailsFactory(OrderDetailsDataApi orderDetailsDataApi,Gson gson){
        return new OrderDetailsFactory(orderDetailsDataApi,gson);
    }
    @Provides
    OrderDetailsDataApi provideapi(Retrofit retrofit){ return retrofit.create(OrderDetailsDataApi.class);}

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
    OrderListAuthInterceptor provideContactUsAuthInterceptor(@ApplicationContext Context context,
                                                             AbstractionRouter abstractionRouter,
                                                             UserSession userSession) {
        return new OrderListAuthInterceptor(context,abstractionRouter,userSession);

    }
}
