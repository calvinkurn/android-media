package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.transaction.checkout.data.repository.ICartRepository;
import com.tokopedia.transaction.checkout.domain.mapper.CheckoutMapper;
import com.tokopedia.transaction.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.transaction.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.transaction.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.transaction.checkout.view.di.scope.CartShipmentActivityScope;
import com.tokopedia.transaction.checkout.view.view.shipmentform.CartShipmentActivity;
import com.tokopedia.transaction.checkout.view.view.shipmentform.CartShipmentPresenter;
import com.tokopedia.transaction.checkout.view.view.shipmentform.ICartShipmentActivity;
import com.tokopedia.transaction.checkout.view.view.shipmentform.ICartShipmentPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

@Module(includes = {DataModule.class, ConverterDataModule.class, UtilModule.class})
public class CartShipmentModule {

    private final ICartShipmentActivity viewListener;

    public CartShipmentModule(CartShipmentActivity cartShipmentActivity) {
        this.viewListener = cartShipmentActivity;
    }

    @Provides
    @CartShipmentActivityScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @CartShipmentActivityScope
    ICheckoutMapper provideICheckoutMapper(IMapperUtil mapperUtil) {
        return new CheckoutMapper(mapperUtil);
    }

    @Provides
    @CartShipmentActivityScope
    CheckoutUseCase provideCheckoutUseCase(ICartRepository cartRepository, ICheckoutMapper checkoutMapper) {
        return new CheckoutUseCase(cartRepository, checkoutMapper);
    }

    @Provides
    @CartShipmentActivityScope
    ICartShipmentPresenter provideICartShipmentPresenter(CheckoutUseCase checkoutUseCase,
                                                         CompositeSubscription compositeSubscription) {
        return new CartShipmentPresenter(compositeSubscription, checkoutUseCase, viewListener);
    }

}
