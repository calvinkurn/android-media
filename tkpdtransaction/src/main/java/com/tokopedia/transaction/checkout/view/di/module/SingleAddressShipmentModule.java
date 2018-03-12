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
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.di.scope.SingleAddressShipmentScope;
import com.tokopedia.transaction.checkout.view.mapper.ShipmentDataRequestConverter;
import com.tokopedia.transaction.checkout.view.view.shipmentform.ICartSingleAddressView;
import com.tokopedia.transaction.checkout.view.view.shipmentform.SingleAddressShipmentFragment;
import com.tokopedia.transaction.checkout.view.view.shipmentform.SingleAddressShipmentPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module(includes = {DataModule.class, ConverterDataModule.class, PeopleAddressModule.class, UtilModule.class})
public class SingleAddressShipmentModule {

    private SingleAddressShipmentAdapter.ActionListener adapterActionListener;
    private ICartSingleAddressView viewListener;

    public SingleAddressShipmentModule(SingleAddressShipmentFragment singleAddressShipmentFragment) {
        adapterActionListener = singleAddressShipmentFragment;
        viewListener = singleAddressShipmentFragment;
    }

    @Provides
    @SingleAddressShipmentScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @SingleAddressShipmentScope
    ICartMapper provideICartMapper(IMapperUtil mapperUtil) {
        return new CartMapper(mapperUtil);
    }

    @Provides
    @SingleAddressShipmentScope
    IShipmentMapper provideIShipmentMapper(IMapperUtil mapperUtil) {
        return new ShipmentMapper(mapperUtil);
    }

    @Provides
    @SingleAddressShipmentScope
    IVoucherCouponMapper provideIVoucherCouponMapper(IMapperUtil mapperUtil) {
        return new VoucherCouponMapper(mapperUtil);
    }

    @Provides
    @SingleAddressShipmentScope
    ICartListInteractor provideICartListInteractor(CompositeSubscription compositeSubscription,
                                                   ICartRepository cartRepository,
                                                   ICartMapper cartMapper,
                                                   IShipmentMapper shipmentMapper,
                                                   IVoucherCouponMapper voucherCouponMapper) {
        return new CartListInteractor(compositeSubscription, cartRepository, cartMapper, shipmentMapper, voucherCouponMapper);
    }

    @Provides
    @SingleAddressShipmentScope
    SingleAddressShipmentPresenter provideCartSingleAddressPresenter(ICartListInteractor cartListInteractor) {
        return new SingleAddressShipmentPresenter(viewListener, cartListInteractor);
    }

    @Provides
    @SingleAddressShipmentScope
    ShipmentDataRequestConverter provideShipmentDataRequestConverter() {
        return new ShipmentDataRequestConverter();
    }

    @Provides
    @SingleAddressShipmentScope
    SingleAddressShipmentAdapter provideCartSingleAddressAdapter(ShipmentDataRequestConverter shipmentDataRequestConverter) {
        return new SingleAddressShipmentAdapter(adapterActionListener, shipmentDataRequestConverter);
    }

}