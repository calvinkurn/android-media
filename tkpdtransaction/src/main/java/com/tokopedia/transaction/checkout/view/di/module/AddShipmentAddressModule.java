package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.transaction.checkout.view.di.scope.AddShipmentAddressScope;
import com.tokopedia.transaction.checkout.view.view.addressoptions.ShipmentAddressListFragment;
import com.tokopedia.transaction.checkout.view.view.multipleaddressform.AddShipmentAddressPresenter;
import com.tokopedia.transaction.checkout.view.view.multipleaddressform.IAddShipmentAddressPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kris on 3/1/18. Tokopedia
 */

@Module
public class AddShipmentAddressModule {


    @Provides
    @AddShipmentAddressScope
    ShipmentAddressListFragment provideAddressFragment() {
        return ShipmentAddressListFragment.newInstance();
    }

    @Provides
    @AddShipmentAddressScope
    IAddShipmentAddressPresenter providePresenter() {
        return new AddShipmentAddressPresenter();
    }

    @Provides
    @AddShipmentAddressScope

}
