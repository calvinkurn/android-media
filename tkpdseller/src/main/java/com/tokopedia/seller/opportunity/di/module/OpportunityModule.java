package com.tokopedia.seller.opportunity.di.module;

import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.opportunity.data.source.CloudGetFilterOpportunitySource2;
import com.tokopedia.seller.opportunity.data.source.CloudGetListOpportunitySource2;
import com.tokopedia.seller.opportunity.data.source.api.ReplacementActApi;
import com.tokopedia.seller.opportunity.data.source.api.ReplacementApi;
import com.tokopedia.seller.opportunity.data.source.CloudActionReplacementSource2;
import com.tokopedia.seller.opportunity.di.scope.OpportunityScope;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFilterUseCase;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFirstTimeUseCase;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityUseCase;
import com.tokopedia.seller.opportunity.domain.repository.ReplacementRepository;
import com.tokopedia.seller.opportunity.domain.repository.ReplacementRepositoryImpl;
import com.tokopedia.seller.opportunity.presenter.OpportunityListPresenter;
import com.tokopedia.seller.opportunity.presenter.OpportunityListPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by normansyahputa on 1/10/18.
 */
@OpportunityScope
@Module
public class OpportunityModule {
    @OpportunityScope
    @Provides
    public ReplacementActApi provideReplacementActApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(ReplacementActApi.class);
    }

    @OpportunityScope
    @Provides
    public ReplacementApi provideReplacementApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(ReplacementApi.class);
    }

    @OpportunityScope
    @Provides
    public ReplacementRepository provideReplacementRepository(
            CloudGetListOpportunitySource2 cloudGetListOpportunitySource2,
            CloudGetFilterOpportunitySource2 cloudGetFilterOpportunitySource2,
            CloudActionReplacementSource2 cloudActionReplacementSource2
    ){
        return new ReplacementRepositoryImpl(cloudGetListOpportunitySource2,cloudGetFilterOpportunitySource2, cloudActionReplacementSource2);
    }

    @OpportunityScope
    @Provides
    public OpportunityListPresenter provideOpportunityListPresenter(
            GetOpportunityUseCase getOpportunityUseCase,
            GetOpportunityFilterUseCase getFilterUseCase,
            GetOpportunityFirstTimeUseCase getOpportunityFirstTimeUseCase,
            SessionHandler sessionHandler){
                return new OpportunityListPresenterImpl(getOpportunityUseCase, getFilterUseCase, getOpportunityFirstTimeUseCase, sessionHandler);
    }
}
