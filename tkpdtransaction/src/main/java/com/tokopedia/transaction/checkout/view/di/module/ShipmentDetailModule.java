package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.transaction.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.transaction.checkout.data.mapper.ShipmentRatesDataMapperV2;
import com.tokopedia.transaction.checkout.data.repository.RatesDataStore;
import com.tokopedia.transaction.checkout.data.repository.RatesDataStoreV2;
import com.tokopedia.transaction.checkout.data.repository.RatesRepository;
import com.tokopedia.transaction.checkout.data.service.RatesService;
import com.tokopedia.transaction.checkout.domain.usecase.GetRatesUseCase;
import com.tokopedia.transaction.checkout.view.adapter.CourierChoiceAdapter;
import com.tokopedia.transaction.checkout.view.di.scope.ShipmentDetailScope;
import com.tokopedia.transaction.checkout.view.view.shippingoptions.IShipmentDetailPresenter;
import com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 09/02/18.
 */
@Module
public class ShipmentDetailModule {

    private static final int RETRY_COUNT = 0;


    @Provides
    @ShipmentDetailScope
    RatesService provideRatesService() {
        return new RatesService();
    }

    @Provides
    @ShipmentDetailScope
    RatesDataStoreV2 provideRatesDataStoreV2(RatesService ratesService) {
        return new RatesDataStoreV2(ratesService);
    }

    @Provides
    @ShipmentDetailScope
    ShipmentRatesDataMapperV2 provideShipmentRatesDatamapperV2() {
        return new ShipmentRatesDataMapperV2();
    }


    @Provides
    @ShipmentDetailScope
    KeroAuthService provideKeroAuthService() {
        return new KeroAuthService(RETRY_COUNT);
    }

    @Provides
    @ShipmentDetailScope
    RatesDataStore provideRatesDataStore(KeroAuthService keroAuthService) {
        return new RatesDataStore(keroAuthService);
    }

    @Provides
    @ShipmentDetailScope
    ShipmentRatesDataMapper provideShipmentRatesDatamapper() {
        return new ShipmentRatesDataMapper();
    }

    @Provides
    @ShipmentDetailScope
    RatesRepository provideRatesRepository(RatesDataStore ratesDataStore,
                                           RatesDataStoreV2 ratesDataStoreV2,
                                           ShipmentRatesDataMapper shipmentRatesDataMapper,
                                           ShipmentRatesDataMapperV2 shipmentRatesDataMapperV2) {
        return new RatesRepository(ratesDataStore, ratesDataStoreV2, shipmentRatesDataMapper, shipmentRatesDataMapperV2);
    }

    @Provides
    @ShipmentDetailScope
    GetRatesUseCase provideGetRatesUseCase(RatesRepository ratesRepository) {
        return new GetRatesUseCase(ratesRepository);
    }

    @Provides
    @ShipmentDetailScope
    IShipmentDetailPresenter provideShipmentDetailPresenter(GetRatesUseCase getRatesUseCase) {
        return new ShipmentDetailPresenter(getRatesUseCase);
    }

    @Provides
    @ShipmentDetailScope
    CourierChoiceAdapter provideCourierChoiceAdapter() {
        return new CourierChoiceAdapter();
    }
}
