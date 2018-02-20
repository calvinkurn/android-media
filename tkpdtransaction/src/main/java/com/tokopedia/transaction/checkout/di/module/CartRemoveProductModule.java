package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.CartRemoveProductScope;
import com.tokopedia.transaction.checkout.domain.CartListInteractor;
import com.tokopedia.transaction.checkout.domain.CartMapper;
import com.tokopedia.transaction.checkout.domain.ICartListInteractor;
import com.tokopedia.transaction.checkout.domain.ICartMapper;
import com.tokopedia.transaction.checkout.domain.ICartRepository;
import com.tokopedia.transaction.checkout.view.CartRemoveProductFragment;
import com.tokopedia.transaction.checkout.view.adapter.CartRemoveProductAdapter;
import com.tokopedia.transaction.checkout.view.presenter.CartRemoveProductPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@Module(includes = {DataModule.class, ConverterDataModule.class})
public class CartRemoveProductModule {

    private final CartRemoveProductAdapter.CartRemoveProductActionListener actionListener;

    public CartRemoveProductModule(CartRemoveProductFragment cartRemoveProductFragment) {
        actionListener = cartRemoveProductFragment;
    }

    @Provides
    @CartRemoveProductScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @CartRemoveProductScope
    ICartMapper provideICartMapper() {
        return new CartMapper();
    }

    @Provides
    @CartRemoveProductScope
    ICartListInteractor provideICartListInteractor(CompositeSubscription compositeSubscription,
                                                   ICartRepository cartRepository,
                                                   ICartMapper cartMapper) {
        return new CartListInteractor(compositeSubscription, cartRepository, cartMapper);
    }

    @Provides
    @CartRemoveProductScope
    CartRemoveProductPresenter provideCartRemoveProductPresenter(ICartListInteractor cartListInteractor) {
        return new CartRemoveProductPresenter(cartListInteractor);
    }

    @Provides
    @CartRemoveProductScope
    CartRemoveProductAdapter provideCartRemoveProductAdapter() {
        return new CartRemoveProductAdapter(actionListener);
    }


}
