package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.CartRemoveProductScope;
import com.tokopedia.transaction.checkout.view.CartRemoveProductFragment;
import com.tokopedia.transaction.checkout.view.adapter.CartRemoveProductAdapter;
import com.tokopedia.transaction.checkout.view.presenter.CartRemoveProductPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@Module
public class CartRemoveProductModule {

    private final CartRemoveProductAdapter.CartRemoveProductActionListener actionListener;

    public CartRemoveProductModule(CartRemoveProductFragment cartRemoveProductFragment) {
        actionListener = cartRemoveProductFragment;
    }

    @Provides
    @CartRemoveProductScope
    CartRemoveProductPresenter provideCartRemoveProductPresenter() {
        return new CartRemoveProductPresenter();
    }

    @Provides
    @CartRemoveProductScope
    CartRemoveProductAdapter provideCartRemoveProductAdapter() {
        return new CartRemoveProductAdapter(actionListener);
    }


}
