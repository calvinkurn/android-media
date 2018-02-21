package com.tokopedia.transaction.checkout.di.module;

import android.support.v7.widget.RecyclerView;

import com.tokopedia.transaction.checkout.di.scope.CartListScope;
import com.tokopedia.transaction.checkout.domain.CartListInteractor;
import com.tokopedia.transaction.checkout.domain.CartMapper;
import com.tokopedia.transaction.checkout.domain.ICartListInteractor;
import com.tokopedia.transaction.checkout.domain.ICartMapper;
import com.tokopedia.transaction.checkout.domain.ICartRepository;
import com.tokopedia.transaction.checkout.domain.IShipmentMapper;
import com.tokopedia.transaction.checkout.domain.ShipmentMapper;
import com.tokopedia.transaction.checkout.view.CartFragment;
import com.tokopedia.transaction.checkout.view.activity.CartItemDecoration;
import com.tokopedia.transaction.checkout.view.adapter.CartListAdapter;
import com.tokopedia.transaction.checkout.view.presenter.CartListPresenter;
import com.tokopedia.transaction.checkout.view.presenter.ICartListPresenter;
import com.tokopedia.transaction.checkout.view.view.ICartListView;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

@Module(includes = {DataModule.class, ConverterDataModule.class})
public class CartListModule {

    private final ICartListView cartListView;
    private final CartListAdapter.ActionListener cartListActionListener;

    public CartListModule(CartFragment cartFragment) {
        this.cartListView = cartFragment;
        this.cartListActionListener = cartFragment;
    }

    @Provides
    @CartListScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @CartListScope
    ICartMapper provideICartMapper() {
        return new CartMapper();
    }

    @Provides
    @CartListScope
    IShipmentMapper provideIShipmentMapper() {
        return new ShipmentMapper();
    }

    @Provides
    @CartListScope
    ICartListInteractor provideICartListInteractor(CompositeSubscription compositeSubscription,
                                                   ICartRepository cartRepository,
                                                   ICartMapper cartMapper,
                                                   IShipmentMapper shipmentMapper) {
        return new CartListInteractor(compositeSubscription, cartRepository, cartMapper, shipmentMapper);
    }

    @Provides
    @CartListScope
    ICartListPresenter provideICartListPresenter(ICartRepository cartRepository,
                                                 ICartListInteractor cartListInteractor) {
        return new CartListPresenter(cartListView, cartListInteractor);
    }

    @Provides
    @CartListScope
    RecyclerView.ItemDecoration provideCartItemDecoration() {
        return new CartItemDecoration(20, true, 0);
    }


    @Provides
    @CartListScope
    CartListAdapter provideCartListAdapter() {
        return new CartListAdapter(cartListActionListener);
    }


}
