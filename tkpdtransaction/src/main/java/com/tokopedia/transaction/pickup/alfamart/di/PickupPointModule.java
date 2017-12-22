package com.tokopedia.transaction.pickup.alfamart.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.transaction.pickup.alfamart.data.datastore.PickupPointDataStore;
import com.tokopedia.transaction.pickup.alfamart.data.mapper.PickupPointEntityMapper;
import com.tokopedia.transaction.pickup.alfamart.data.repository.PickupPointRepository;
import com.tokopedia.transaction.pickup.alfamart.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickup.alfamart.view.PickupPointContract;
import com.tokopedia.transaction.pickup.alfamart.view.PickupPointPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

@Module
public class PickupPointModule {

    private static final int RETRY_COUNT = 0;

    // Provide KeroAuthService
    @Provides
    @PickupPointScope
    KeroAuthService provideKeroAuthService() {
        return new KeroAuthService(RETRY_COUNT);
    }

    // Provide Data Store
    @Provides
    @PickupPointScope
    PickupPointDataStore providePickupPointDataStore(KeroAuthService keroAuthService) {
        return new PickupPointDataStore(keroAuthService);
    }

    // Provide EntityMapper
    @Provides
    @PickupPointScope
    PickupPointEntityMapper provideEntityMapper() {
        return new PickupPointEntityMapper();
    }

    // Provide Repository
    @Provides
    @PickupPointScope
    PickupPointRepository providePickupPointRepository(
            PickupPointDataStore pickupPointDataStore,
            PickupPointEntityMapper pickupPointEntityMapper
    ) {
        return new PickupPointRepository(pickupPointDataStore,
                pickupPointEntityMapper);
    }

    // Provide Use Case
    @Provides
    @PickupPointScope
    GetPickupPointsUseCase provideGetPickupPointsUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            PickupPointRepository pickupPointRepository
    ) {
        return new GetPickupPointsUseCase(threadExecutor, postExecutionThread, pickupPointRepository);
    }

    // Provide Presenter
    @Provides
    @PickupPointScope
    PickupPointContract.Presenter providePickupPointPresenter(
            GetPickupPointsUseCase getPickupPointsUseCase) {
        return new PickupPointPresenter(getPickupPointsUseCase);
    }

}
