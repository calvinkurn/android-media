package com.tokopedia.transaction.checkout.view.di.module;

import android.support.v7.widget.RecyclerView;

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
import com.tokopedia.transaction.checkout.view.adapter.CartListAdapter;
import com.tokopedia.transaction.checkout.view.di.scope.CartListScope;
import com.tokopedia.transaction.checkout.view.view.cartlist.CartFragment;
import com.tokopedia.transaction.checkout.view.view.cartlist.CartItemDecoration;
import com.tokopedia.transaction.checkout.view.view.cartlist.CartListPresenter;
import com.tokopedia.transaction.checkout.view.view.cartlist.ICartListPresenter;
import com.tokopedia.transaction.checkout.view.view.cartlist.ICartListView;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

@Module(includes = {DataModule.class, ConverterDataModule.class, UtilModule.class})
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
    ICartMapper provideICartMapper(IMapperUtil mapperUtil) {
        return new CartMapper(mapperUtil);
    }

    @Provides
    @CartListScope
    IShipmentMapper provideIShipmentMapper(IMapperUtil mapperUtil) {
        return new ShipmentMapper(mapperUtil);
    }

    @Provides
    @CartListScope
    IVoucherCouponMapper provideIVoucherCouponMapper(IMapperUtil mapperUtil) {
        return new VoucherCouponMapper(mapperUtil);
    }

    @Provides
    @CartListScope
    ICartListInteractor provideICartListInteractor(CompositeSubscription compositeSubscription,
                                                   ICartRepository cartRepository,
                                                   ICartMapper cartMapper,
                                                   IShipmentMapper shipmentMapper,
                                                   IVoucherCouponMapper voucherCouponMapper) {
        return new CartListInteractor(compositeSubscription, cartRepository, cartMapper, shipmentMapper, voucherCouponMapper);
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
        return new CartItemDecoration(40, false, 0);
    }


    @Provides
    @CartListScope
    CartListAdapter provideCartListAdapter() {
        return new CartListAdapter(cartListActionListener);
    }


}
