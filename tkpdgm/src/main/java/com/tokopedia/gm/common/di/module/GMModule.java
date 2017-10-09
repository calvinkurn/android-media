package com.tokopedia.gm.common.di.module;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.seller.base.data.repository.DatePickerRepositoryImpl;
import com.tokopedia.seller.base.data.source.DatePickerDataSource;
import com.tokopedia.seller.base.domain.DatePickerRepository;
import com.tokopedia.seller.base.domain.interactor.ClearDatePickerUseCase;
import com.tokopedia.seller.base.domain.interactor.FetchDatePickerUseCase;
import com.tokopedia.seller.base.domain.interactor.SaveDatePickerUseCase;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenterImpl;
import com.tokopedia.gm.common.di.scope.GMScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author sebastianuskh on 4/13/17.
 */
@GMScope
@Module
public class GMModule {

    @GMScope
    @Provides
    DatePickerPresenter provideDatePickerPresenter(FetchDatePickerUseCase fetchDatePickerUseCase,
                                                   SaveDatePickerUseCase saveDatePickerUseCase,
                                                   ClearDatePickerUseCase clearDatePickerUseCase) {
        return new DatePickerPresenterImpl(fetchDatePickerUseCase, saveDatePickerUseCase, clearDatePickerUseCase);
    }

    @GMScope
    @Provides
    DatePickerRepository provideDatePickerRepository(DatePickerDataSource datePickerDataSource) {
        return new DatePickerRepositoryImpl(datePickerDataSource);
    }

    @GMScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager(){
        return new GlobalCacheManager();
    }
}