package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.core.network.apiservices.transaction.TXActService;
import com.tokopedia.transaction.checkout.data.repository.ICartRepository;
import com.tokopedia.transaction.checkout.data.repository.ITopPayRepository;
import com.tokopedia.transaction.checkout.data.repository.TopPayRepository;
import com.tokopedia.transaction.checkout.domain.mapper.CheckoutMapper;
import com.tokopedia.transaction.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.transaction.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.transaction.checkout.domain.mapper.ITopPayMapper;
import com.tokopedia.transaction.checkout.domain.mapper.TopPayMapper;
import com.tokopedia.transaction.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.transaction.checkout.domain.usecase.GetThanksToppayUseCase;
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
    ITopPayMapper provideITopPayMapper() {
        return new TopPayMapper();
    }

    @Provides
    @CartShipmentActivityScope
    TXActService provideTXActService() {
        return new TXActService();
    }

    @Provides
    @CartShipmentActivityScope
    ITopPayRepository provideITopPayRepository(TXActService txActService) {
        return new TopPayRepository(txActService);
    }

    @Provides
    @CartShipmentActivityScope
    CheckoutUseCase provideCheckoutUseCase(ICartRepository cartRepository, ICheckoutMapper checkoutMapper) {
        return new CheckoutUseCase(cartRepository, checkoutMapper);
    }

    @Provides
    @CartShipmentActivityScope
    GetThanksToppayUseCase provideGetThanksToppayUseCase(ITopPayRepository topPayRepository, ITopPayMapper topPayMapper) {
        return new GetThanksToppayUseCase(topPayRepository, topPayMapper);
    }

    @Provides
    @CartShipmentActivityScope
    ICartShipmentPresenter provideICartShipmentPresenter(CheckoutUseCase checkoutUseCase,
                                                         GetThanksToppayUseCase getThanksToppayUseCase,
                                                         CompositeSubscription compositeSubscription) {
        return new CartShipmentPresenter(compositeSubscription, checkoutUseCase, getThanksToppayUseCase, viewListener);
    }

}
