package com.tokopedia.transaction.pickupbooth.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.transaction.pickupbooth.data.datastore.PickupPointDataStore;
import com.tokopedia.transaction.pickupbooth.domain.mapper.PickupPointEntityMapper;
import com.tokopedia.transaction.pickupbooth.data.repository.PickupPointRepository;
import com.tokopedia.transaction.pickupbooth.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickupbooth.view.contract.PickupPointContract;
import com.tokopedia.transaction.pickupbooth.view.mapper.PickupPointViewModelMapper;
import com.tokopedia.transaction.pickupbooth.view.presenter.PickupPointPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

@Module
public class PickupPointModule {

    private static final int RETRY_COUNT = 0;

    @Provides
    @PickupPointScope
    KeroAuthService provideKeroAuthService() {
        return new KeroAuthService(RETRY_COUNT);
    }

    @Provides
    @PickupPointScope
    PickupPointDataStore providePickupPointDataStore(KeroAuthService keroAuthService) {
        return new PickupPointDataStore(keroAuthService);
    }

    @Provides
    @PickupPointScope
    PickupPointEntityMapper provideEntityMapper() {
        return new PickupPointEntityMapper();
    }

    @Provides
    @PickupPointScope
    PickupPointViewModelMapper provideViewModelMapper() {
        return new PickupPointViewModelMapper();
    }

    @Provides
    @PickupPointScope
    PickupPointRepository providePickupPointRepository(
            PickupPointDataStore pickupPointDataStore,
            PickupPointEntityMapper pickupPointEntityMapper
    ) {
        return new PickupPointRepository(pickupPointDataStore,
                pickupPointEntityMapper);
    }

    @Provides
    @PickupPointScope
    GetPickupPointsUseCase provideGetPickupPointsUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            PickupPointRepository pickupPointRepository
    ) {
        return new GetPickupPointsUseCase(threadExecutor, postExecutionThread, pickupPointRepository);
    }

    @Provides
    @PickupPointScope
    PickupPointContract.Presenter providePickupPointPresenter(
            GetPickupPointsUseCase getPickupPointsUseCase,
            PickupPointViewModelMapper pickupPointViewModelMapper
    ) {
        return new PickupPointPresenter(getPickupPointsUseCase, pickupPointViewModelMapper);
    }

}
