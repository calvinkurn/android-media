package com.tokopedia.inbox.rescenter.historyawb.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProductHistory;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingHistoryDialogViewModel;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.GetHistoryAwbUseCase;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.TrackAwbReturProductUseCase;
import com.tokopedia.inbox.rescenter.historyawb.view.subscriber.HistoryAwbSubsriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/23/17.
 */

@SuppressWarnings("ALL")
public class HistoryShippingFragmentImpl implements HistoryShippingFragmentPresenter {

    private HistoryShippingFragmentView fragmentView;
    private TrackAwbReturProductUseCase trackAwbReturProductUseCase;
    private GetHistoryAwbUseCase getHistoryAwbUseCase;

    @Inject
    public HistoryShippingFragmentImpl(HistoryShippingFragmentView fragmentView,
                                       GetHistoryAwbUseCase getHistoryAwbUseCase,
                                       TrackAwbReturProductUseCase trackAwbReturProductUseCase) {
        this.fragmentView = fragmentView;
        this.getHistoryAwbUseCase = getHistoryAwbUseCase;
        this.trackAwbReturProductUseCase = trackAwbReturProductUseCase;
    }

    @Override
    public void onFirstTimeLaunch() {
        fragmentView.setLoadingView(true);
        fragmentView.showInpuNewShippingAwb(false);
        getHistoryAwbUseCase.execute(getHistoryAwbParams(), new HistoryAwbSubsriber(fragmentView));
    }

    private RequestParams getHistoryAwbParams() {
        RequestParams params = RequestParams.create();
        params.putString(GetHistoryAwbUseCase.PARAM_RESOLUTION_ID, fragmentView.getResolutionID());
        return params;
    }

    @Override
    public void refreshPage() {
        fragmentView.resetList();
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
                model.setTrackHistory(
                        domainData.getTrackingHistory() != null ?
                                mappingTrackHistory(domainData.getTrackingHistory()) : null
                );
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

    @Override
    public void setOnDestroyView() {
        unSubscibeObservable();
    }

    private void unSubscibeObservable() {
        getHistoryAwbUseCase.unsubscribe();
        trackAwbReturProductUseCase.unsubscribe();
    }

}
