package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.ShipmentDetailScope;
import com.tokopedia.transaction.checkout.view.adapter.CourierChoiceAdapter;
import com.tokopedia.transaction.checkout.view.presenter.IShipmentDetailPresenter;
import com.tokopedia.transaction.checkout.view.presenter.ShipmentDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 09/02/18.
 */
@Module
public class ShipmentDetailModule {

    @Provides
    @ShipmentDetailScope
    IShipmentDetailPresenter provideShipmentDetailPresenter() {
        return new ShipmentDetailPresenter();
    }

    @Provides
    @ShipmentDetailScope
    CourierChoiceAdapter provideCourierChoiceAdapter() {
        return new CourierChoiceAdapter();
    }
}
