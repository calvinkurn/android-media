package com.tokopedia.seller.opportunity.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.OpportunityDataSourceFactory;
import com.tokopedia.seller.opportunity.data.repository.ReplacementRepositoryImpl;
import com.tokopedia.seller.opportunity.domain.interactor.AcceptReplacementUseCase;
import com.tokopedia.seller.opportunity.listener.OppurtunityView;

/**
 * Created by hangnadi on 2/27/17.
 */
public class OppurtunityImpl implements OppurtunityPresenter {

    private final OppurtunityView view;
    private final AcceptReplacementUseCase acceptReplacementUseCase;

    public OppurtunityImpl(Context context, OppurtunityView view) {
        this.view = view;
        ReplacementRepositoryImpl repository = new ReplacementRepositoryImpl(
                new ActionReplacementSourceFactory(context),
                new OpportunityDataSourceFactory(context)
        );
        this.acceptReplacementUseCase = new AcceptReplacementUseCase(
                new JobExecutor(), new UIThread(), repository
        );
    }
}
