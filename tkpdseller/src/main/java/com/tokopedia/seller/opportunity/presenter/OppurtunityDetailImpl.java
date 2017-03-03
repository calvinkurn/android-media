package com.tokopedia.seller.opportunity.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.ReplacementDataSourceFactory;
import com.tokopedia.seller.opportunity.data.repository.ReplacementRepositoryImpl;
import com.tokopedia.seller.opportunity.domain.interactor.AcceptReplacementUseCase;
import com.tokopedia.seller.opportunity.listener.OppurtunityDetailView;

/**
 * Created by hangnadi on 2/27/17.
 */
public class OppurtunityDetailImpl implements OppurtunityDetailPresenter {

    private final OppurtunityDetailView viewListener;
    private final AcceptReplacementUseCase acceptReplacementUseCase;

    public OppurtunityDetailImpl(Context context, OppurtunityDetailView viewListener) {
        this.viewListener = viewListener;
        ReplacementRepositoryImpl repository = new ReplacementRepositoryImpl(
                new ActionReplacementSourceFactory(context),
                new ReplacementDataSourceFactory(context)
        );
        this.acceptReplacementUseCase = new AcceptReplacementUseCase(
                new JobExecutor(), new UIThread(), repository
        );
    }
}
