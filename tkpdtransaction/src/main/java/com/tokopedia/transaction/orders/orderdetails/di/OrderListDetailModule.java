package com.tokopedia.transaction.orders.orderdetails.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;
import com.tokopedia.transaction.orders.orderdetails.domain.ErrorResponse;
import com.tokopedia.transaction.orders.orderdetails.domain.FinishOrderUseCase;
import com.tokopedia.transaction.orders.orderdetails.domain.PostCancelReasonUseCase;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;

@Module
class OrderListDetailModule {

    @Provides
    PostCancelReasonUseCase providePostCancelUseCase(@ApplicationContext Context context){
        List<Interceptor> interceptorList = new ArrayList<>(2);
        interceptorList.add(new TkpdOldAuthInterceptor(context,
                (NetworkRouter) context, new UserSession(context)));
        ErrorResponseInterceptor errorResponseInterceptor = new ErrorResponseInterceptor(ErrorResponse.class);
        interceptorList.add(errorResponseInterceptor);
        return new PostCancelReasonUseCase(interceptorList, context);
    }

    @Provides
    FinishOrderUseCase provideFinishOrderUseCasee(@ApplicationContext Context context){
        List<Interceptor> interceptorList = new ArrayList<>(2);
        interceptorList.add(new TkpdOldAuthInterceptor(context,
                (NetworkRouter) context, new UserSession(context)));
        ErrorResponseInterceptor errorResponseInterceptor = new ErrorResponseInterceptor(ErrorResponse.class);
        interceptorList.add(errorResponseInterceptor);
        return new FinishOrderUseCase(interceptorList, context);
    }
}
