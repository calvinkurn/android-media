package com.tokopedia.transaction.insurance.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.transaction.insurance.data.InsuranceTnCDataStore;
import com.tokopedia.transaction.insurance.data.InsuranceTnCRepository;
import com.tokopedia.transaction.insurance.data.network.InsuranceWebViewService;
import com.tokopedia.transaction.insurance.domain.InsuranceTnCUseCase;
import com.tokopedia.transaction.insurance.view.InsuranceTnCContract;
import com.tokopedia.transaction.insurance.view.InsuranceTnCPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

@Module
public class InsuranceTnCModule {

    public InsuranceTnCModule() {
    }

    // Provide InsuranceWebViewService
    @Provides
    @InsuranceTnCScope
    InsuranceWebViewService provideInsuranceWebViewService() {
        return new InsuranceWebViewService();
    }

    // Provide Data Store
    @Provides
    @InsuranceTnCScope
    InsuranceTnCDataStore provideInsuranceTnCDataStore(InsuranceWebViewService insuranceWebViewService) {
        return new InsuranceTnCDataStore(insuranceWebViewService);
    }

    // Provide Repository
    @Provides
    @InsuranceTnCScope
    InsuranceTnCRepository provideInsuranceTnCRepository(InsuranceTnCDataStore insuranceTnCDataStore) {
        return new InsuranceTnCRepository(insuranceTnCDataStore);
    }

    // Provide Use Case
    @Provides
    @InsuranceTnCScope
    InsuranceTnCUseCase provideGetDistrictRequestUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            InsuranceTnCRepository insuranceTnCRepository
    ) {
        return new InsuranceTnCUseCase(threadExecutor, postExecutionThread,
                insuranceTnCRepository);
    }

    // Provide Presenter
    @Provides
    @InsuranceTnCScope
    InsuranceTnCContract.Presenter provideInsuranceTnCPresenter(
            InsuranceTnCUseCase getDistrictRequestUseCase) {
        return new InsuranceTnCPresenter(getDistrictRequestUseCase);
    }

}
