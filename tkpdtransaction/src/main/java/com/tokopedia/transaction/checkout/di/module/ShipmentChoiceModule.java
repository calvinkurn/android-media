package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.ShipmentChoiceScope;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentChoiceAdapter;
import com.tokopedia.transaction.checkout.view.presenter.IShipmentChoicePresenter;
import com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentChoicePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 09/02/18.
 */

@Module
public class ShipmentChoiceModule {

    @Provides
    @ShipmentChoiceScope
    ShipmentChoiceAdapter provideShipmentChoiceadapter() {
        return new ShipmentChoiceAdapter();
    }

    @Provides
    @ShipmentChoiceScope
    IShipmentChoicePresenter provideShipmentChoicePresenter() {
        return new ShipmentChoicePresenter();
    }
}
