package com.tokopedia.seller.base.di.module;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.seller.base.data.repository.DatePickerRepositoryImpl;
import com.tokopedia.seller.base.data.source.DatePickerDataSource;
import com.tokopedia.seller.base.di.scope.DatePickerScope;
import com.tokopedia.seller.base.domain.DatePickerRepository;
import com.tokopedia.seller.base.domain.interactor.ClearDatePickerUseCase;
import com.tokopedia.seller.base.domain.interactor.FetchDatePickerUseCase;
import com.tokopedia.seller.base.domain.interactor.SaveDatePickerUseCase;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * @author sebastianuskh on 4/13/17.
 */
@DatePickerScope
@Module
public class DatePickerModule {

    @DatePickerScope
    @Provides
    DatePickerPresenter provideDatePickerPresenter(FetchDatePickerUseCase fetchDatePickerUseCase,
                                                   SaveDatePickerUseCase saveDatePickerUseCase,
                                                   ClearDatePickerUseCase clearDatePickerUseCase) {
        return new DatePickerPresenterImpl(fetchDatePickerUseCase, saveDatePickerUseCase, clearDatePickerUseCase);
    }

    @DatePickerScope
    @Provides
    DatePickerRepository provideDatePickerRepository(DatePickerDataSource datePickerDataSource) {
        return new DatePickerRepositoryImpl(datePickerDataSource);
    }

    @DatePickerScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager(){
        return new GlobalCacheManager();
    }
}