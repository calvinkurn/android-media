package com.tokopedia.seller.goldmerchant.statistic.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.seller.goldmerchant.statistic.data.repository.GMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.data.repository.GMStatRepositoryImpl;
import com.tokopedia.seller.goldmerchant.statistic.data.source.GMStatDataSource;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatApi;
import com.tokopedia.seller.goldmerchant.statistic.di.scope.GMStatisticScope;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionTableUseCase;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTablePresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTablePresenterImpl;
import com.tokopedia.seller.topads.dashboard.domain.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.dashboard.domain.interactor.DashboardTopadsInteractorImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author normansyahputa on 7/6/17.
 */
@GMStatisticScope
@Module
public class GMStatisticModule {

    @GMStatisticScope
    @Provides
    GMStatApi provideGmStatisticTransactionApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(GMStatApi.class);
    }

    @GMStatisticScope
    @Provides
    GMStatRepository provideGMStatRepository(
            GMStatDataSource gmStatDataSource) {
        return new GMStatRepositoryImpl(gmStatDataSource);
    }

    @GMStatisticScope
    @Provides
    GMStatisticTransactionTablePresenter provideGmStatisticTransactionTablePresenter(
            GMStatGetTransactionTableUseCase gmStatGetTransactionTableUseCase
    ) {
        return new GMStatisticTransactionTablePresenterImpl(gmStatGetTransactionTableUseCase);
    }

    @GMStatisticScope
    @Provides
    DashboardTopadsInteractor provideDashboardTopadsInteractor(@ApplicationContext Context context) {
        return new DashboardTopadsInteractorImpl(context);
    }

}
