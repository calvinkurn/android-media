package com.tokopedia.seller.common.logout;

import android.content.Context;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.common.category.di.module.CategoryPickerModule;
import com.tokopedia.seller.common.logout.di.component.DaggerTkpdSellerLogoutComponent;
import com.tokopedia.seller.common.logout.di.component.TkpdSellerLogoutComponent;
import com.tokopedia.seller.common.logout.di.module.TkpdSellerLogoutModule;
import com.tokopedia.usecase.RequestParams;

import rx.Subscriber;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class TkpdSellerLogout {
    public static void onLogOut(AppComponent appComponent, Context context) {
        TkpdSellerLogoutComponent component = DaggerTkpdSellerLogoutComponent
                .builder()
                .appComponent(appComponent)
                .categoryPickerModule(new CategoryPickerModule(context))
                .tkpdSellerLogoutModule(new TkpdSellerLogoutModule())
                .build();
        component.getClearCategoryCacheUseCase().execute(RequestParams.EMPTY, new EmptySubscriber());
    }

    public static class EmptySubscriber extends Subscriber<Boolean> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Boolean aBoolean) {

        }
    }
}
