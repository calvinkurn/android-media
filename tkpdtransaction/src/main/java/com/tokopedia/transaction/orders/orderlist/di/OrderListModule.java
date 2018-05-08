package com.tokopedia.transaction.orders.orderlist.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.transaction.orders.common.url.OrderURL;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListRepository;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListUseCase;
import com.tokopedia.transaction.orders.orderlist.source.OrderListFactory;
import com.tokopedia.transaction.orders.orderlist.source.OrderListRepositoryImpl;
import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return builder
                .baseUrl(OrderURL.BASE_URL)
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TkpdResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    OkHttpClient provideOkHttpClient() {
        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy())
                .buildClientDefaultAuth();
    }

    @Provides
    OrderListDataApi provide(Retrofit retrofit){
        return retrofit.create(OrderListDataApi.class);
    }

    @Provides
    OrderListFactory provideOrderListFactory(OrderListDataApi orderListDataApi){
        return new OrderListFactory(orderListDataApi);
    }
}
