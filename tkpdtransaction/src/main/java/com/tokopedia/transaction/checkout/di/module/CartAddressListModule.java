package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.CartAddressListScope;
import com.tokopedia.transaction.checkout.view.adapter.CartAddressListAdapter;
import com.tokopedia.transaction.checkout.view.presenter.CartAddressListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@Module
public class CartAddressListModule {

    @Provides
    @CartAddressListScope
    CartAddressListPresenter provideCartAddressListPresenter() {
        return new CartAddressListPresenter();
    }

    @Provides
    @CartAddressListScope
    CartAddressListAdapter provideCartAddressListAdapter() {
        return new CartAddressListAdapter();
    }


}
