package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.transaction.checkout.data.repository.ICartRepository;
import com.tokopedia.transaction.checkout.domain.mapper.CartMapper;
import com.tokopedia.transaction.checkout.domain.mapper.ICartMapper;
import com.tokopedia.transaction.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.transaction.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.transaction.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.transaction.checkout.domain.mapper.ShipmentMapper;
import com.tokopedia.transaction.checkout.domain.mapper.VoucherCouponMapper;
import com.tokopedia.transaction.checkout.domain.usecase.CartListInteractor;
import com.tokopedia.transaction.checkout.domain.usecase.ICartListInteractor;
import com.tokopedia.transaction.checkout.view.adapter.CartRemoveProductAdapter;
import com.tokopedia.transaction.checkout.view.di.scope.CartRemoveProductScope;
import com.tokopedia.transaction.checkout.view.view.cartlist.CartRemoveProductFragment;
import com.tokopedia.transaction.checkout.view.view.cartlist.CartRemoveProductPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@Module(includes = {DataModule.class, ConverterDataModule.class, UtilModule.class})
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
    ICartMapper provideICartMapper(IMapperUtil mapperUtil) {
        return new CartMapper(mapperUtil);
    }

    @Provides
    @CartRemoveProductScope
    IShipmentMapper provideIShipmentMapper(IMapperUtil mapperUtil) {
        return new ShipmentMapper(mapperUtil);
    }

    @Provides
    @CartRemoveProductScope
    IVoucherCouponMapper provideIVoucherCouponMapper(IMapperUtil mapperUtil) {
        return new VoucherCouponMapper(mapperUtil);
    }

    @Provides
    @CartRemoveProductScope
    ICartListInteractor provideICartListInteractor(CompositeSubscription compositeSubscription,
                                                   ICartRepository cartRepository,
                                                   ICartMapper cartMapper,
                                                   IShipmentMapper shipmentMapper,
                                                   IVoucherCouponMapper voucherCouponMapper) {
        return new CartListInteractor(compositeSubscription, cartRepository, cartMapper, shipmentMapper, voucherCouponMapper);
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
