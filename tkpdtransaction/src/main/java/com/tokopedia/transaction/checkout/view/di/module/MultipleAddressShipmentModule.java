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
import com.tokopedia.transaction.checkout.view.di.scope.MultipleAddressShipmentScope;
import com.tokopedia.transaction.checkout.view.view.shipmentform.IMultipleAddressShipmentPresenter;
import com.tokopedia.transaction.checkout.view.view.shipmentform.IMultipleAddressShipmentView;
import com.tokopedia.transaction.checkout.view.view.shipmentform.MultipleAddressShipmentPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@Module(includes = {DataModule.class, UtilModule.class})
public class MultipleAddressShipmentModule {

    private IMultipleAddressShipmentView view;

    public MultipleAddressShipmentModule(IMultipleAddressShipmentView view) {
        this.view = view;
    }

    @Provides
    @MultipleAddressShipmentScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @MultipleAddressShipmentScope
    ICartMapper provideICartMapper(IMapperUtil mapperUtil) {
        return new CartMapper(mapperUtil);
    }

    @Provides
    @MultipleAddressShipmentScope
    IShipmentMapper provideIShipmentMapper(IMapperUtil mapperUtil) {
        return new ShipmentMapper(mapperUtil);
    }

    @Provides
    @MultipleAddressShipmentScope
    IVoucherCouponMapper provideIVoucherCouponMapper(IMapperUtil mapperUtil) {
        return new VoucherCouponMapper(mapperUtil);
    }

    @Provides
    @MultipleAddressShipmentScope
    ICartListInteractor provideICartListInteractor(CompositeSubscription compositeSubscription,
                                                   ICartRepository cartRepository,
                                                   ICartMapper cartMapper,
                                                   IShipmentMapper shipmentMapper,
                                                   IVoucherCouponMapper voucherCouponMapper) {
        return new CartListInteractor(compositeSubscription, cartRepository, cartMapper, shipmentMapper, voucherCouponMapper);
    }

    @MultipleAddressShipmentScope
    @Provides
    IMultipleAddressShipmentPresenter providePresenter(ICartListInteractor cartListInteractor) {
        return new MultipleAddressShipmentPresenter(view, cartListInteractor);
    }

}
