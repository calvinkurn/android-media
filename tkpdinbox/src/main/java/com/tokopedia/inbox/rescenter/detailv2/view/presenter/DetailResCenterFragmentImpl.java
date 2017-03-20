package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResCenterDetailUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.TrackAwbReturProductUseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.GetResCenterDetailSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.TrackAwbReturProductSubscriber;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailResCenterFragmentImpl implements DetailResCenterFragmentPresenter {

    private final DetailResCenterFragmentView fragmentView;
    private final GetResCenterDetailUseCase getResCenterDetailUseCase;
    private final TrackAwbReturProductUseCase trackAwbReturProductUseCase;

    public DetailResCenterFragmentImpl(Context context, DetailResCenterFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        String resolutionID = fragmentView.getResolutionID();
        String accessToken = new SessionHandler(context).getAccessToken(context);

        JobExecutor jobExecutor = new JobExecutor();
        UIThread uiThread = new UIThread();
        InboxResCenterService inboxResCenterService = new InboxResCenterService();
        ResolutionService resolutionService = new ResolutionService();
        resolutionService.setToken(accessToken);
        DetailResCenterMapper detailResCenterMapper = new DetailResCenterMapper();

        ResCenterDataSourceFactory dataSourceFactory
                = new ResCenterDataSourceFactory(context, resolutionService, inboxResCenterService, detailResCenterMapper);
        ResCenterRepository resCenterRepository
                = new ResCenterRepositoryImpl(resolutionID, dataSourceFactory);

        this.getResCenterDetailUseCase
                = new GetResCenterDetailUseCase(jobExecutor, uiThread, resCenterRepository);

        this.trackAwbReturProductUseCase
                = new TrackAwbReturProductUseCase(jobExecutor, uiThread, resCenterRepository);
    }

    @Override
    public void setOnFirstTimeLaunch() {
        fragmentView.showLoading(true);
        getResCenterDetailUseCase.execute(getInitResCenterDetailParam(),
                new GetResCenterDetailSubscriber(fragmentView));
    }

    private RequestParams getInitResCenterDetailParam() {
        return RequestParams.EMPTY;
    }

    @Override
    public void refreshPage() {

    }

    @Override
    public void finishReturProduct(String resolutionID) {

    }

    @Override
    public void acceptSolution(String resolutionID) {

    }

    @Override
    public void askHelpResolution() {

    }

    @Override
    public void trackReturProduck(String shipmentID, String shipmentRef) {
        fragmentView.showLoadingDialog(true);
        trackAwbReturProductUseCase.execute(getTrackingAwbProductParam(shipmentID, shipmentRef),
                new TrackAwbReturProductSubscriber(fragmentView));
    }

    private RequestParams getTrackingAwbProductParam(String shipmentID, String shipmentRef) {
        RequestParams params = RequestParams.create();
        params.putString(TrackAwbReturProductUseCase.PARAM_SHIPMENT_ID, shipmentID);
        params.putString(TrackAwbReturProductUseCase.PARAM_SHIPPING_REFENCE, shipmentRef);
        return params;
    }

}
