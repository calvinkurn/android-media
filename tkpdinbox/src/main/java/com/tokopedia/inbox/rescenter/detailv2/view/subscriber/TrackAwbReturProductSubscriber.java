package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProductHistory;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingHistoryDialogViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/16/17.
 */

public class TrackAwbReturProductSubscriber extends rx.Subscriber<TrackingAwbReturProduct>{

    private final DetailResCenterFragmentView fragmentView;

    public TrackAwbReturProductSubscriber(DetailResCenterFragmentView fragmentView) {
        this.fragmentView = fragmentView;
    }

    @Override
    public void onCompleted() {
        fragmentView.setOnRequestTrackingComplete();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof IOException) {
            fragmentView.setTrackingData(mappingTimeOutViewModel());
        } else {
            fragmentView.setTrackingData(mappingDefaultErrorViewModel());
        }
    }

    private TrackingDialogViewModel mappingTimeOutViewModel() {
        TrackingDialogViewModel model = new TrackingDialogViewModel();
        model.setSuccess(false);
        model.setTimeOut(true);
        return model;
    }

    private TrackingDialogViewModel mappingDefaultErrorViewModel() {
        return mappingTimeOutViewModel();
    }

    @Override
    public void onNext(TrackingAwbReturProduct object) {
        fragmentView.setTrackingData(mappingViewModel(object));
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

