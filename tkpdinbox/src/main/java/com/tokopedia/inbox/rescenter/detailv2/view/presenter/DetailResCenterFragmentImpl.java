package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AcceptAdminSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AcceptSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AskHelpResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.CancelResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.FinishReturSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResCenterDetailUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.InputAddressUseCase;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.TrackAwbReturProductUseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.GetResCenterDetailSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.ResolutionActionSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.TrackAwbReturProductSubscriber;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailResCenterFragmentImpl implements DetailResCenterFragmentPresenter {

    private final DetailResCenterFragmentView fragmentView;
    private final GetResCenterDetailUseCase getResCenterDetailUseCase;
    private final TrackAwbReturProductUseCase trackAwbReturProductUseCase;
    private final CancelResolutionUseCase cancelResolutionUseCase;
    private final AskHelpResolutionUseCase askHelpResolutionUseCase;
    private final FinishReturSolutionUseCase finishReturSolutionUseCase;
    private final AcceptAdminSolutionUseCase acceptAdminSolutionUseCase;
    private final AcceptSolutionUseCase acceptSolutionUseCase;
    private final InputAddressUseCase inputAddressUseCase;

    public DetailResCenterFragmentImpl(Context context, DetailResCenterFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        String resolutionID = fragmentView.getResolutionID();
        String accessToken = new SessionHandler(context).getAccessToken(context);

        JobExecutor jobExecutor = new JobExecutor();
        UIThread uiThread = new UIThread();

        InboxResCenterService inboxResCenterService = new InboxResCenterService();

        ResCenterActService resCenterActService = new ResCenterActService();
        ResolutionService resolutionService = new ResolutionService();
        resolutionService.setToken(accessToken);

        DetailResCenterMapper detailResCenterMapper = new DetailResCenterMapper();
        HistoryAwbMapper historyAwbMapper = new HistoryAwbMapper();

        ResCenterDataSourceFactory dataSourceFactory = new ResCenterDataSourceFactory(context,
                resolutionService,
                inboxResCenterService,
                resCenterActService,
                detailResCenterMapper,
                historyAwbMapper);
        ResCenterRepository resCenterRepository
                = new ResCenterRepositoryImpl(resolutionID, dataSourceFactory);

        this.getResCenterDetailUseCase
                = new GetResCenterDetailUseCase(jobExecutor, uiThread, resCenterRepository);

        this.trackAwbReturProductUseCase
                = new TrackAwbReturProductUseCase(jobExecutor, uiThread, resCenterRepository);

        this.cancelResolutionUseCase
                = new CancelResolutionUseCase(jobExecutor, uiThread, resCenterRepository);

        this.askHelpResolutionUseCase
                = new AskHelpResolutionUseCase(jobExecutor, uiThread, resCenterRepository);

        this.finishReturSolutionUseCase
                = new FinishReturSolutionUseCase(jobExecutor, uiThread, resCenterRepository);

        this.acceptAdminSolutionUseCase
                = new AcceptAdminSolutionUseCase(jobExecutor, uiThread, resCenterRepository);

        this.acceptSolutionUseCase
                = new AcceptSolutionUseCase(jobExecutor, uiThread, resCenterRepository);

        this.inputAddressUseCase
                = new InputAddressUseCase(jobExecutor, uiThread, resCenterRepository);
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
        setOnFirstTimeLaunch();
    }

    @Override
    public void finishReturProduct() {
        fragmentView.showLoadingDialog(true);
        askHelpResolutionUseCase.execute(getFinishReturSolutionParam(),
                new ResolutionActionSubscriber(fragmentView));
    }

    private RequestParams getFinishReturSolutionParam() {
        RequestParams params = RequestParams.create();
        params.putString(CancelResolutionUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
    }

    @Override
    public void acceptSolution() {
        acceptSolutionUseCase.execute(getAcceptSolutionParam(),
                new ResolutionActionSubscriber(fragmentView));
    }

    private RequestParams getAcceptSolutionParam() {
        RequestParams params = RequestParams.create();
        params.putString(AcceptSolutionUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
    }

    @Override
    public void acceptAdminSolution() {
        acceptAdminSolutionUseCase.execute(getAcceptAdminSolutionParam(),
                new ResolutionActionSubscriber(fragmentView));
    }

    private RequestParams getAcceptAdminSolutionParam() {
        RequestParams params = RequestParams.create();
        params.putString(AcceptAdminSolutionUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
    }

    @Override
    public void cancelResolution() {
        fragmentView.showLoadingDialog(true);
        cancelResolutionUseCase.execute(getCancelResolutionParam(),
                new ResolutionActionSubscriber(fragmentView));
    }

    private RequestParams getCancelResolutionParam() {
        RequestParams params = RequestParams.create();
        params.putString(CancelResolutionUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
    }

    @Override
    public void askHelpResolution() {
        fragmentView.showLoadingDialog(true);
        askHelpResolutionUseCase.execute(getAskHelpResolutionParam(),
                new ResolutionActionSubscriber(fragmentView));
    }

    private RequestParams getAskHelpResolutionParam() {
        RequestParams params = RequestParams.create();
        params.putString(AskHelpResolutionUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
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

    @Override
    public void inputAddressAcceptSolution(String addressId) {
        fragmentView.showLoadingDialog(true);
        inputAddressUseCase.execute(getInputAddressParam(addressId, InputAddressUseCase.DEFAULT_BY_PASS),
                new ResolutionActionSubscriber(fragmentView));
    }

    @Override
    public void inputAddressAcceptAdminSolution(String addressId) {
        fragmentView.showLoadingDialog(true);
        inputAddressUseCase.execute(getInputAddressParam(addressId, InputAddressUseCase.ADMIN_BY_PASS),
                new ResolutionActionSubscriber(fragmentView));
    }

    private RequestParams getInputAddressParam(String addressId, int paramByPass) {
        RequestParams params = RequestParams.create();
        params.putString(InputAddressUseCase.PARAM_ADDRESS_ID, addressId);
        params.putInt(InputAddressUseCase.PARAM_BYPASS, paramByPass);
        params.putString(InputAddressUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        params.putInt(InputAddressUseCase.PARAM_NEW_ADDRESS, 1);
        return params;
    }

}
