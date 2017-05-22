package com.tokopedia.seller.logout;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.logout.di.component.DaggerTkpdSellerLogoutComponent;
import com.tokopedia.seller.logout.di.component.TkpdSellerLogoutComponent;
import com.tokopedia.seller.logout.di.module.TkpdSellerLogoutModule;

import rx.Subscriber;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class TkpdSellerLogout {
    public static void onLogOut(AppComponent appComponent) {
        TkpdSellerLogoutComponent component = DaggerTkpdSellerLogoutComponent
                .builder()
                .appComponent(appComponent)
                .tkpdSellerLogoutModule(new TkpdSellerLogoutModule())
                .build();
        component.getClearAllDraftProductUseCase().execute(RequestParams.EMPTY, new EmptySubscriber());
        component.getClearCategoryCacheUseCase().execute(RequestParams.EMPTY, new EmptySubscriber());
    }

    private static class EmptySubscriber extends Subscriber<Boolean> {
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
