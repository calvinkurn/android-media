package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.CartAddressListScope;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.presenter.ShipmentAddressListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@Module
public class CartAddressListModule {

    @Provides
    @CartAddressListScope
    ShipmentAddressListPresenter provideCartAddressListPresenter() {
        return new ShipmentAddressListPresenter();
    }

    @Provides
    @CartAddressListScope
    ShipmentAddressListAdapter provideCartAddressListAdapter() {
        return new ShipmentAddressListAdapter();
    }


}
