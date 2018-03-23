package com.tokopedia.tkpdtrain.search.di;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.search.domain.GetAvailabilityScheduleUseCase;
import com.tokopedia.tkpdtrain.search.domain.GetDetailScheduleUseCase;
import com.tokopedia.tkpdtrain.search.domain.GetFilterSearchParamDataUseCase;
import com.tokopedia.tkpdtrain.search.domain.GetFilteredAndSortedScheduleUseCase;
import com.tokopedia.tkpdtrain.search.domain.GetScheduleUseCase;
import com.tokopedia.tkpdtrain.search.domain.GetTotalScheduleUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nabilla on 3/9/18.
 */
@Module
public class TrainSearchModule {

    @Provides
    GetScheduleUseCase provideGetScheduleUseCase(TrainRepository trainRepository) {
        return new GetScheduleUseCase(trainRepository);
    }

    @Provides
    GetAvailabilityScheduleUseCase provideGetAvailabilityScheduleUseCase(TrainRepository trainRepository) {
        return new GetAvailabilityScheduleUseCase(trainRepository);
    }

    @Provides
    GetFilteredAndSortedScheduleUseCase provideGetFilteredAndSortedScheduleUseCase(TrainRepository trainRepository) {
        return new GetFilteredAndSortedScheduleUseCase(trainRepository);
    }

    @Provides
    GetDetailScheduleUseCase provideGetDetailScheduleUseCase(TrainRepository trainRepository) {
        return new GetDetailScheduleUseCase(trainRepository);
    }

    @Provides
    GetTotalScheduleUseCase provideGetTotalScheduleUseCase(TrainRepository trainRepository) {
        return new GetTotalScheduleUseCase(trainRepository);
    }

    @Provides
    GetFilterSearchParamDataUseCase provideGetFilterSearchParamDataUseCase(TrainRepository trainRepository) {
        return new GetFilterSearchParamDataUseCase(trainRepository);
    }
}
