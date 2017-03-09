package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.inbox.rescenter.detailv2.data.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.domain.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.GetResCenterDetailUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailResCenterFragmentImpl implements DetailResCenterFragmentPresenter {

    private final DetailResCenterFragmentView fragmentView;
    private final GetResCenterDetailUseCase getResCenterDetailUseCase;

    public DetailResCenterFragmentImpl(Context context, DetailResCenterFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        String resolutionID = fragmentView.getResolutionID();

        JobExecutor jobExecutor = new JobExecutor();
        UIThread uiThread = new UIThread();
        ResolutionService resolutionService = new ResolutionService();
        DetailResCenterMapper detailResCenterMapper = new DetailResCenterMapper();

        ResCenterDataSourceFactory dataSourceFactory
                = new ResCenterDataSourceFactory(context, resolutionService, detailResCenterMapper);
        ResCenterRepository resCenterRepository
                = new ResCenterRepositoryImpl(resolutionID, dataSourceFactory);

        this.getResCenterDetailUseCase
                = new GetResCenterDetailUseCase(jobExecutor, uiThread, resCenterRepository);
    }

    @Override
    public void setOnFirstTimeLaunch() {
        getResCenterDetailUseCase.execute(null, new GetResCenterDetailSubscriber());
    }

    private class GetResCenterDetailSubscriber extends rx.Subscriber<DetailResCenter> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(DetailResCenter detailResCenter) {

        }
    }
}
