package com.tokopedia.inbox.rescenter.history;

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
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.HistoryAwbUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.TrackAwbReturProductUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;
import com.tokopedia.inbox.rescenter.history.view.subscriber.HistoryAwbSubsriber;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/23/17.
 */

@SuppressWarnings("ALL")
public class HistoryShippingFragmentImpl implements HistoryShippingFragmentPresenter {

    private final HistoryShippingFragmentView fragmentView;
    private final TrackAwbReturProductUseCase trackAwbReturProductUseCase;
    private final HistoryAwbUseCase historyAwbUseCase;

    public HistoryShippingFragmentImpl(Context context, HistoryShippingFragmentView fragmentView) {
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

        this.historyAwbUseCase
                = new HistoryAwbUseCase(jobExecutor, uiThread, resCenterRepository);

        this.trackAwbReturProductUseCase
                = new TrackAwbReturProductUseCase(jobExecutor, uiThread, resCenterRepository);
    }

    @Override
    public void onFirstTimeLaunch() {
        fragmentView.setLoadingView(true);
        fragmentView.showInpuNewShippingAwb(false);
        historyAwbUseCase.execute(RequestParams.EMPTY, new HistoryAwbSubsriber(fragmentView));
    }

    @Override
    public void refreshPage() {
        onFirstTimeLaunch();
    }

    @Override
    public void doActionTrack(String shippingRefNumber, String shipmentID) {
        trackAwbReturProductUseCase.execute(
                getTrackAwbParam(shippingRefNumber, shipmentID),
                new TrackAwbSubscriber(fragmentView)
        );
    }

    private RequestParams getTrackAwbParam(String shippingRefNumber, String shipmentID) {
        RequestParams params = RequestParams.create();
        params.putString(TrackAwbReturProductUseCase.PARAM_SHIPPING_REFENCE, shippingRefNumber);
        params.putString(TrackAwbReturProductUseCase.PARAM_SHIPMENT_ID, shipmentID);
        return params;
    }

    private class TrackAwbSubscriber extends Subscriber<TrackingAwbReturProduct> {
        public TrackAwbSubscriber(HistoryShippingFragmentView fragmentView) {
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(TrackingAwbReturProduct trackingAwbReturProduct) {

        }
    }
}
