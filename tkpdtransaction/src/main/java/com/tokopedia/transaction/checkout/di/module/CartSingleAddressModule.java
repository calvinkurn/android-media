package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.CartSingleAddressScope;
import com.tokopedia.transaction.checkout.view.adapter.CartSingleAddressAdapter;
import com.tokopedia.transaction.checkout.view.presenter.CartSingleAddressPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@Module(includes = {DataModule.class, ConverterDataModule.class})
public class CartSingleAddressModule {

    @Provides
    @CartSingleAddressScope
    CartSingleAddressPresenter provideCartSingleAddressPresenter() {
        return new CartSingleAddressPresenter();
    }

    @Provides
    @CartSingleAddressScope
    CartSingleAddressAdapter provideCartSingleAddressAdapter() {
        return new CartSingleAddressAdapter();
    }


}
