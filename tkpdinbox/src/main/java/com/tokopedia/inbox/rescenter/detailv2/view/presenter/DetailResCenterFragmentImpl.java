package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResCenterDetailUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;

import java.io.IOException;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailResCenterFragmentImpl implements DetailResCenterFragmentPresenter {

    private final DetailResCenterFragmentView fragmentView;
    private final GetResCenterDetailUseCase getResCenterDetailUseCase;

    public DetailResCenterFragmentImpl(Context context, DetailResCenterFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        String resolutionID = fragmentView.getResolutionID();
        String accessToken = new SessionHandler(context).getAccessToken(context);

        JobExecutor jobExecutor = new JobExecutor();
        UIThread uiThread = new UIThread();
        ResolutionService resolutionService = new ResolutionService();
        resolutionService.setToken(accessToken);
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
        fragmentView.showLoading(true);
        getResCenterDetailUseCase.execute(getInitResCenterDetailParam(), new GetResCenterDetailSubscriber());
    }

    private RequestParams getInitResCenterDetailParam() {
        return RequestParams.EMPTY;
    }

    private class GetResCenterDetailSubscriber extends rx.Subscriber<DetailResCenter> {
        @Override
        public void onCompleted() {
            fragmentView.setOnInitResCenterDetailComplete();
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof IOException) {
                fragmentView.setViewData(mappingTimeOutViewModel());
            } else {
                fragmentView.setViewData(mappingDefaultErrorViewModel());
            }
        }

        @Override
        public void onNext(DetailResCenter detailResCenter) {
            fragmentView.setViewData(mappingViewModel(detailResCenter));
        }
    }

    private DetailViewModel mappingDefaultErrorViewModel() {
        return mappingTimeOutViewModel();
    }

    private DetailViewModel mappingTimeOutViewModel() {
        DetailViewModel model = new DetailViewModel();
        model.setSuccess(false);
        model.setTimeOut(true);
        return model;
    }

    private DetailViewModel mappingViewModel(DetailResCenter detailResCenter) {
        DetailViewModel model = new DetailViewModel();
        if (detailResCenter != null && detailResCenter.isSuccess()) {
            model.setSuccess(true);
        } else {
            model.setSuccess(false);
            model.setMessageError(detailResCenter != null ? detailResCenter.getMessageError() : null);
        }
        return model;
    }
}
