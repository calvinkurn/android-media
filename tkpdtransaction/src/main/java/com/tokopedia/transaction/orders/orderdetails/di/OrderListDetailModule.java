package com.tokopedia.transaction.orders.orderdetails.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;
import com.tokopedia.transaction.orders.orderdetails.domain.FinishOrderUseCase;
import com.tokopedia.transaction.orders.orderdetails.domain.PostCancelReasonUseCase;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;

@Module
class OrderListDetailModule {

    @Provides
    PostCancelReasonUseCase providePostCancelUseCase(@ApplicationContext Context context){
        return new PostCancelReasonUseCase(new TkpdOldAuthInterceptor(context,
                (NetworkRouter) context, new UserSession(context)), context);
    }

    @Provides
    FinishOrderUseCase provideFinishOrderUseCasee(@ApplicationContext Context context){
        return new FinishOrderUseCase(new TkpdOldAuthInterceptor(context,
                (NetworkRouter) context, new UserSession(context)), context);
    }
}
