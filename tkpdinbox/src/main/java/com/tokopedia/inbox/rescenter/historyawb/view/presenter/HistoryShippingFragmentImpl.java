package com.tokopedia.inbox.rescenter.historyawb.view.presenter;

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
import com.tokopedia.inbox.rescenter.discussion.data.mapper.DiscussionResCenterMapper;
import com.tokopedia.inbox.rescenter.historyaction.data.mapper.HistoryActionMapper;
import com.tokopedia.inbox.rescenter.historyaddress.data.mapper.HistoryAddressMapper;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.HistoryAwbUseCase;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.TrackAwbReturProductUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProductHistory;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingHistoryDialogViewModel;
import com.tokopedia.inbox.rescenter.historyawb.view.subscriber.HistoryAwbSubsriber;
import com.tokopedia.inbox.rescenter.product.data.mapper.ListProductMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ProductDetailMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        ResolutionService resolutionService = new ResolutionService(accessToken);

        DetailResCenterMapper detailResCenterMapper = new DetailResCenterMapper();
        HistoryAwbMapper historyAwbMapper = new HistoryAwbMapper();
        HistoryAddressMapper historyAddressMapper = new HistoryAddressMapper();
        HistoryActionMapper historyActionMapper = new HistoryActionMapper();
        ListProductMapper listProductMapper = new ListProductMapper();
        ProductDetailMapper productDetailMapper = new ProductDetailMapper();
        DiscussionResCenterMapper discussionResCenterMapper = new DiscussionResCenterMapper();

        ResCenterDataSourceFactory dataSourceFactory = new ResCenterDataSourceFactory(context,
                resolutionService,
                inboxResCenterService,
                resCenterActService,
                detailResCenterMapper,
                historyAwbMapper,
                historyAddressMapper,
                historyActionMapper,
                listProductMapper,
                productDetailMapper,
                discussionResCenterMapper
        );

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
        fragmentView.showLoadingDialog(true);
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
            if (e instanceof IOException) {
                fragmentView.doOnTrackingTimeOut();
            } else {
                fragmentView.doOnTrackingFailed();
            }
        }

        @Override
        public void onNext(TrackingAwbReturProduct object) {
            if (object.isSuccess()) {
                fragmentView.doOnTrackingSuccess(mappingViewModel(object));
            } else {
                fragmentView.doOnTrackingError(object.getMessageError());
            }
        }

        private TrackingDialogViewModel mappingViewModel(TrackingAwbReturProduct domainData) {
            TrackingDialogViewModel model = new TrackingDialogViewModel();
            if (domainData != null && domainData.isSuccess()) {
                model.setSuccess(true);
                model.setDelivered(domainData.isDelivered());
                model.setReceiverName(domainData.getReceiverName());
                model.setShippingRefNum(domainData.getShippingRefNum());
                model.setTrackHistory(mappingTrackHistory(domainData.getTrackingHistory()));
            } else {
                model.setSuccess(false);
                model.setMessageError(domainData != null ? domainData.getMessageError() : null);
            }
            return model;
        }

        private List<TrackingHistoryDialogViewModel> mappingTrackHistory(List<TrackingAwbReturProductHistory> domainModels) {
            List<TrackingHistoryDialogViewModel> viewModels = new ArrayList<>();
            for (TrackingAwbReturProductHistory items : domainModels) {
                TrackingHistoryDialogViewModel model = new TrackingHistoryDialogViewModel();
                model.setCity(items.getCity());
                model.setDate(items.getDate());
                model.setStatus(items.getStatus());
                viewModels.add(model);
            }
            return viewModels;
        }
    }
}
