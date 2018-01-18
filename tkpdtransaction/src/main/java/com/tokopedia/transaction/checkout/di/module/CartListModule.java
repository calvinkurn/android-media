package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.CartListScope;
import com.tokopedia.transaction.checkout.view.CartFragment;
import com.tokopedia.transaction.checkout.view.adapter.CartListAdapter;
import com.tokopedia.transaction.checkout.view.presenter.CartListPresenter;
import com.tokopedia.transaction.checkout.view.presenter.ICartListPresenter;
import com.tokopedia.transaction.checkout.view.view.ICartListView;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

@Module
public class CartListModule {

    private final ICartListView cartListView;
    private final CartListAdapter.ActionListener cartListActionListener;

    public CartListModule(CartFragment cartFragment) {
        this.cartListView = cartFragment;
        this.cartListActionListener = cartFragment;
    }

    @Provides
    @CartListScope
    ICartListPresenter provideICartListPresenter() {
        return new CartListPresenter(cartListView);
    }

    @Provides
    @CartListScope
    CartListAdapter provideCartListAdapter() {
        return new CartListAdapter(cartListActionListener);
    }


}
