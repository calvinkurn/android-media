package com.tokopedia.seller.goldmerchant.common.di.module;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.seller.base.data.repository.DatePickerRepositoryImpl;
import com.tokopedia.seller.base.data.source.DatePickerDataSource;
import com.tokopedia.seller.base.domain.DatePickerRepository;
import com.tokopedia.seller.base.domain.interactor.ClearDatePickerUseCase;
import com.tokopedia.seller.base.domain.interactor.FetchDatePickerUseCase;
import com.tokopedia.seller.base.domain.interactor.SaveDatePickerUseCase;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenterImpl;
import com.tokopedia.seller.goldmerchant.common.di.scope.GoldMerchantScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author sebastianuskh on 4/13/17.
 */
@GoldMerchantScope
@Module
public class GoldMerchantModule {

    @GoldMerchantScope
    @Provides
    DatePickerPresenter provideDatePickerPresenter(FetchDatePickerUseCase fetchDatePickerUseCase,
                                                   SaveDatePickerUseCase saveDatePickerUseCase,
                                                   ClearDatePickerUseCase clearDatePickerUseCase) {
        return new DatePickerPresenterImpl(fetchDatePickerUseCase, saveDatePickerUseCase, clearDatePickerUseCase);
    }

    @GoldMerchantScope
    @Provides
    DatePickerRepository provideDatePickerRepository(DatePickerDataSource datePickerDataSource) {
        return new DatePickerRepositoryImpl(datePickerDataSource);
    }

    @GoldMerchantScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager(){
        return new GlobalCacheManager();
    }
}